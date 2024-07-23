package com.programix.testing;

import java.lang.reflect.*;
import java.util.*;

import com.programix.thread.*;
import com.programix.util.*;

public class StandardTestChunk implements TestChunk {
    private final BaseTest baseTest;
    private final StateMonitor<TestState> stateMonitor;
    private final ThreadManager threadManager;
    private final BroadcastManager broadcastManager;
    private final TestChunk.Listener broadcastListener;
    private final BooleanState cancelRequested;

    public StandardTestChunk(BaseTest baseTest) {
        ObjectTools.paramNullCheck(baseTest, "baseTest");
        this.baseTest = baseTest;
        cancelRequested = new BooleanState(false);
        stateMonitor = new StateMonitor<>(TestState.NEVER_STARTED);
        broadcastManager = new BroadcastManager();
        broadcastListener = broadcastManager.getBroadcastListener();
        threadManager = new ThreadManager(this);

        stateMonitor.addListener(new StateMonitor.Listener<TestState>() {
            @Override
            public void stateChanged(TestState oldState, TestState newState) {
                broadcastListener.testStateChanged(
                    new TestChunk.ListenerEventMeta(), oldState, newState);
            }
        });
    }

    public static StandardTestChunk[] createAll(BaseTest[] baseTests) {
        ObjectTools.paramNullCheck(baseTests, "baseTests");
        StandardTestChunk[] testChunks = new StandardTestChunk[baseTests.length];
        for ( int i = 0; i < testChunks.length; i++ ) {
            testChunks[i] = new StandardTestChunk(baseTests[i]);
        }
        return testChunks;
    }

    @Override
    public String getDisplayName() {
        return baseTest.getDisplayName();
    }

    @Override
    public ScoringInfo getScoringInfo() {
        return baseTest.getScoringInfo();
    }

    @Override
    public int getActiveThreadCount() {
        return threadManager.getActiveThreadCount();
    }

    @Override
    public TestState getTestState() {
        return stateMonitor.getState();
    }

    @Override
    public boolean kickoffTestsAsync() {
        return cancelRequested.isFalse() ?
            threadManager.kickoffTestsIfNeverStarted() : false;
    }

    @Override
    public void cancelAllTests() {
        boolean firstCancelRequest = cancelRequested.ifFalseSetTrue();
        if ( firstCancelRequest ) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadManager.cancelIfRunning();
                }
            }).start();
        }
    }

    @Override
    public void addListener(TestChunk.Listener listener) {
        broadcastManager.addSubscribedListener(listener);
    }

    @Override
    public void removeListener(TestChunk.Listener listener) {
        broadcastManager.removeSubscribedListener(listener);
    }

    private static class ThreadManager {
        private static long nextThreadGroupNumber = 1;

        private final StandardTestChunk owner;
        private final DisconnectableListener runTestsOutputDisconnectableListener;
        private final DisconnectableListener broadcastInputDisconnectableListener;
        private final BufferedListener bufferedListener;

        private final BooleanState everStarted;
        private final BooleanState stillRunning;
        private final BooleanState cancelled;

        private volatile ThreadGroup activeThreadGroup;
        private volatile Thread testMainThread;

        public ThreadManager(StandardTestChunk owner) {
            ObjectTools.paramNullCheck(owner, "owner");
            this.owner = owner;

            broadcastInputDisconnectableListener = new DisconnectableListener(owner.broadcastListener);
            bufferedListener = new BufferedListener(100, broadcastInputDisconnectableListener.getDisconnectableListener());
            runTestsOutputDisconnectableListener = new DisconnectableListener(bufferedListener.getBufferFillingListener());

            everStarted = new BooleanState(false);
            stillRunning = new BooleanState(false);
            cancelled = new BooleanState(false);
        }

        private static synchronized long uniqueThreadGroupNumber() {
            long number = nextThreadGroupNumber;
            nextThreadGroupNumber++;
            return number;
        }

        public int getActiveThreadCount() {
            ThreadGroup group = activeThreadGroup;
            return group != null ? group.activeCount() : 0;
        }

        public boolean kickoffTestsIfNeverStarted() {
            boolean shouldStart = everStarted.ifFalseSetTrue();
            if ( !shouldStart ) return false;

            owner.stateMonitor.transitionTo(TestState.RUNNING);
            stillRunning.setState(true);

            activeThreadGroup =
                new ThreadGroup("TC-" + uniqueThreadGroupNumber());

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    runWork();
                }
            };

            testMainThread = new Thread(
                activeThreadGroup,
                r,
                "main-" + owner.getDisplayName());

            testMainThread.start();
            return true;
        }

        @SuppressWarnings("deprecation")
        public synchronized void cancelIfRunning() {
            if ( cancelled.isFalse() ) {

                cancelled.setState(true);
                runTestsOutputDisconnectableListener.disconnect();

                bufferedListener.requestShutdownAsap();
                // wait a bit, but proceed anyway if still not shutdown
                bufferedListener.waitUntilShutdown(1000L);

                broadcastInputDisconnectableListener.disconnect();

                owner.stateMonitor.transitionToIfPermitted(TestState.CANCELLED);

                interruptAndStopAllButMainThread();

                try {
//ThreadTools.outln("inside cancelIfRunning(), trying to join testMainThread for 2 seconds");
                    testMainThread.join(2000);
                } catch ( InterruptedException x ) {
                    // ignore
                }


                for ( int i = 0; i < 5; i++ ) {
                    if ( i > 0 ) ThreadTools.nap(200); // pause briefly

                    if ( !testMainThread.isAlive() ) break;
                    testMainThread.interrupt();
                }

                if ( testMainThread.isAlive() ) {
                    testMainThread.stop();
                }
            }
        }

        private void runWork() {
            try {
                bufferedListener.kickoff();
                owner.baseTest.runTests(
                    runTestsOutputDisconnectableListener.getDisconnectableListener());

                runTestsOutputDisconnectableListener.disconnect();

                bufferedListener.requestShutdownWhenDrained();

                // wait a short while, but proceed anyway if still not shutdown
//owner.broadcastListener.outln(new ListenerEventMeta(), "waiting for the buffer to drain...");
                boolean timedOut =
                    bufferedListener.waitUntilShutdown(10000L) == ThreadTools.TIMED_OUT;

                broadcastInputDisconnectableListener.disconnect();

                if ( timedOut ) {
                    ListenerEventMeta meta = new ListenerEventMeta();
                    owner.broadcastListener.incrementFailedCount(meta);
                    owner.broadcastListener.outlnErrorText(meta,
                        "Timed out waiting for message buffer to empty - " +
                        "unknown number of messages is missing");
                }

                TestState finalState = TestState.FAILED; // a decent default
                if ( cancelled.isTrue() ) {
                    finalState = TestState.CANCELLED;
                } else if ( owner.broadcastManager.hasAnyTestFailures() ) {
                    finalState = TestState.FAILED;
                } else {
                    finalState = TestState.SUCCEEDED;
                }
                owner.stateMonitor.transitionToIfPermitted(finalState);
            } finally {
                Thread.interrupted(); // clear the interrupt flag

                bufferedListener.requestShutdownAsap(); // just to be sure
                // wait a short while, but proceed anyway if still not shutdown
                bufferedListener.waitUntilShutdown(500L);

                interruptAndStopAllButMainThread();
                activeThreadGroup = null;
                stillRunning.setState(false);
            }
        }

        private Thread[] findAllThreadsInTreeExceptMain() {
            ThreadGroup tg = activeThreadGroup;
            if ( tg == null ) return new Thread[0];

            Thread[] threadsInTree = new Thread[0];
            int threadFoundCount = 0;
            do {
                threadsInTree = new Thread[tg.activeCount() * 2];
                threadFoundCount = tg.enumerate(threadsInTree, true);
            } while ( threadFoundCount >= threadsInTree.length );

            List<Thread> list = new ArrayList<>();
            for ( Thread thread : threadsInTree ) {
                if ( thread != null && !thread.equals(testMainThread) ) {
                    list.add(thread);
                }

            }
            return list.toArray(new Thread[0]);
        }

        @SuppressWarnings("deprecation")
        private void interruptAndStopAllButMainThread() {
            for ( int i = 0; i < 5; i++ ) {
                if ( i > 0 ) ThreadTools.nap(200); // pause to give a chance

                Thread[] otherThreads = findAllThreadsInTreeExceptMain();
                if ( otherThreads.length < 1 ) break;

                for ( Thread thread : otherThreads ) {
                    thread.interrupt();
                }
            }

            Thread[] otherThreads = findAllThreadsInTreeExceptMain();
            if ( otherThreads.length > 0 ) {
                for ( Thread thread : otherThreads ) {
                    thread.stop();
                }
                ThreadTools.nap(200); // pause to give a chance
            }
        }
    } // type ThreadManager

    private static class BroadcastManager {
        private final TestChunk.Listener broadcastListener;
        private final BroadcastProxyHandler proxyHandler;
        private final BooleanState anyFailures;

        public BroadcastManager() {
            anyFailures = new BooleanState(false);

            proxyHandler = new BroadcastProxyHandler();
            broadcastListener = (TestChunk.Listener) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] { TestChunk.Listener.class },
                proxyHandler);

            addSubscribedListener(new TestChunk.ListenerAdapter() {
                @Override
                public void incrementFailedCount(ListenerEventMeta meta) {
                    anyFailures.setState(true);
                }
            });
        }

        public TestChunk.Listener getBroadcastListener() {
            return broadcastListener;
        }

        public void addSubscribedListener(TestChunk.Listener listener) {
            proxyHandler.addListener(listener);
        }

        public void removeSubscribedListener(TestChunk.Listener listener) {
            proxyHandler.removeListener(listener);
        }

        public boolean hasAnyTestFailures() {
            return anyFailures.isTrue();
        }

        private static class BroadcastProxyHandler
                implements InvocationHandler {

            private final Class<?> interfaceType;
            private final ListenerManager<TestChunk.Listener> listenerManager;

            public BroadcastProxyHandler() {
                this.interfaceType = TestChunk.Listener.class;
                listenerManager = new ListenerManager<>(TestChunk.Listener.class);
            }

            public boolean addListener(TestChunk.Listener listener) {
                return listenerManager.addListener(listener);
            }

            public boolean removeListener(TestChunk.Listener listener) {
                return listenerManager.removeListener(listener);
            }

            @Override
            public Object invoke(Object proxy,
                                 final Method method,
                                 final Object[] args)
                    throws Throwable {

                if ( interfaceType.equals(method.getDeclaringClass()) ) {
//ThreadTools.outln("inside BroadcastListener, notifying listeners and returning null, method=" + method.getName() + ", args.length=" + (args == null ? 0 : args.length) + ", listenerCount=" + listenerManager.getListenerCount());
                    listenerManager.notifyListeners(
                        new ListenerManager.NotifyAction<TestChunk.Listener>() {

                        @Override
                        public void performAction(TestChunk.Listener listener) {
                            try {
                                method.invoke(listener, args);
                            } catch ( Exception x ) {
                                throw (x instanceof RuntimeException ?
                                    (RuntimeException) x :
                                    new RuntimeException(x));
                            }
                        }
                    });

                    return null;
                } else {
//if ( !"toString".equals(method.getName() ) ) {
//    ThreadTools.outln("inside BroadcastListener, proxy to 'this', method=" + method.getName() + ", args.length=" + (args == null ? 0 : args.length) + ", listenerCount=" + listenerManager.getListenerCount());
//}
                    return method.invoke(this, args);
                }
            }

            @Override
            public String toString() {
                return getClass().getSimpleName()  + "[listenerCount=" + listenerManager.getListenerCount() + "]";
            }
        } // type BroadcastProxyHandler
    } // type BroadcastManager

    private static class BufferedListener {
        private final TestChunk.Listener bufferDrainingListener;
        private final TestChunk.Listener bufferFillingListener;
        private final BufferedProxyHandler proxyHandler;

        private final Waiter waiter;
        private final Object lockObject;
        private final StateMonitor<BufferState> bufferState;
        private final Waiter.Condition runningAndEmptyCondition;
        private final BoundedFIFO<MethodArgEntry> fifo;

        private volatile Thread internalThread;

        public BufferedListener(int capacity,
                                TestChunk.Listener bufferDrainingListener) {

            ObjectTools.paramNullCheck(
                bufferDrainingListener, "bufferDrainingListener");
            this.bufferDrainingListener = bufferDrainingListener;

            waiter = new Waiter(new Object());
            this.lockObject = waiter.getLockObject();
            bufferState = new StateMonitor<>(
                BufferState.NEVER_STARTED, waiter);
            fifo = new BoundedFIFO<>(capacity, waiter);

            runningAndEmptyCondition = waiter.createCondition(
                    new Waiter.Expression() {

                @Override
                public boolean isTrue() {
                    return bufferState.getState() == BufferState.RUNNING &&
                           fifo.isEmpty();
                }
            });

            proxyHandler = new BufferedProxyHandler(Listener.class);
            bufferFillingListener = (Listener) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] { Listener.class },
                proxyHandler);
        }

        public TestChunk.Listener getBufferFillingListener() {
            return bufferFillingListener;
        }

        public void kickoff() {
            synchronized ( lockObject ) {
                if ( bufferState.getState() != BufferState.NEVER_STARTED ) {
                    throw new IllegalStateException("kickoff() can only be " +
                        "called one time; current state=" +
                        bufferState.getState());
                }

                bufferState.transitionTo(BufferState.RUNNING);

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        runWork();
                    }
                };

                internalThread = new Thread(r, "BufferedListener");
                internalThread.start();
            }
        }

        /**
         * Called to request that the buffer shut itself down when empty.
         * This method can safely be called at any time and multiple times.
         */
        public boolean requestShutdownWhenDrained() {
            // only if permitted, otherwise quietly do nothing
            return bufferState.transitionToIfPermitted(
                BufferState.PENDING_SHUTDOWN_WHEN_DRAINED);
        }

        /**
         * Called to request that the buffer shut itself down As Soon As
         * Possible.
         * This method can safely be called at any time and multiple times.
         */
        public boolean requestShutdownAsap() {
            // only if permitted, otherwise quietly do nothing
            return bufferState.transitionToIfPermitted(
                BufferState.PENDING_SHUTDOWN_ASAP);
        }

        /**
         * Waits until shutdown. Returns {@link ThreadTools#TIMED_OUT} if
         * timeout occurred while waiting.
         */
        public boolean waitUntilShutdown(long msTimeout)
                throws InterruptException {

            return bufferState.waitUntilStateIs(BufferState.SHUTDOWN, msTimeout);
        }

        private void runWork() {
            try {
                while ( true ) {
                    MethodArgEntry entry = null;

                    synchronized ( lockObject ) {
                        runningAndEmptyCondition.waitWhileTrue();
                        if ( bufferState.matchesAny(BufferState.RUNNING) ) {
                            // normal situation - still running and not empty
                            entry = fifo.remove();
                        } else if ( bufferState.matchesAny(
                                BufferState.PENDING_SHUTDOWN_WHEN_DRAINED) ) {

                            if ( fifo.isEmpty() ) {
                                return; // all done
                            } else {
                                // not empty
                                entry = fifo.remove();
                            }
                        } else if ( bufferState.matchesAny(BufferState.PENDING_SHUTDOWN_ASAP) ) {
                            return; // all done
                        }
                    } // sync

                    // invoke without holding the lock
                    if ( entry != null ) { // null check just to be safe
//ThreadTools.nap(100);
                        entry.method.invoke(
                            bufferDrainingListener, entry.args);
                    }
                } // while
            } catch ( InterruptException x ) {
                // ignore
            } catch ( Exception x ) {
//ThreadTools.outln(x);
                x.printStackTrace();
            } finally {
                bufferState.transitionToIfPermitted(BufferState.SHUTDOWN);
            }
        }

        private class BufferedProxyHandler implements InvocationHandler {
            private final Class<?> interfaceType;

            public BufferedProxyHandler(Class<?> interfaceType) {
                ObjectTools.paramNullCheck(interfaceType, "interfaceType");
                this.interfaceType = interfaceType;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {

                if ( interfaceType.equals(method.getDeclaringClass()) ) {
//ThreadTools.outln("inside BufferedListener, adding method to FIFO and returning null, method=" + method.getName() + ", args.length=" + (args == null ? 0 : args.length) + ", fifo.size=" + fifo.getSize());
                    fifo.add(new MethodArgEntry(method, args));
                    return null;
                } else {
//if ( !"toString".equals(method.getName() ) ) {
//    ThreadTools.outln("inside BufferedListener, proxy to 'this', method=" + method.getName() + ", args.length=" + (args == null ? 0 : args.length) + ", fifo.size=" + fifo.getSize());
//}
                    return method.invoke(this, args);
                }
            }

            @Override
            public String toString() {
                return getClass().getSimpleName()  + "[fifo.size=" + fifo.getSize() + "]";
            }
        }

        private static class MethodArgEntry {
            public final Method method;
            public final Object[] args;

            public MethodArgEntry(Method method, Object[] args) {
                this.method = method;
                this.args = args;
            }
        } // type MethodArgEntry

        private static enum BufferState
                implements StateMonitor.Monitorable<BufferState> {

            NEVER_STARTED,
            RUNNING,
            PENDING_SHUTDOWN_WHEN_DRAINED,
            PENDING_SHUTDOWN_ASAP,
            SHUTDOWN;

            @Override
            public boolean canTransitionTo(BufferState potentialNewState) {
                switch ( this ) {
                    case NEVER_STARTED:
                        return matchesAny(potentialNewState, RUNNING);

                    case RUNNING:
                        return matchesAny(potentialNewState,
                            PENDING_SHUTDOWN_WHEN_DRAINED,
                            PENDING_SHUTDOWN_ASAP,
                            SHUTDOWN);

                    case PENDING_SHUTDOWN_WHEN_DRAINED:
                        return matchesAny(potentialNewState,
                            PENDING_SHUTDOWN_ASAP, SHUTDOWN);

                    case PENDING_SHUTDOWN_ASAP:
                        return matchesAny(potentialNewState, SHUTDOWN);

                    case SHUTDOWN:
                        return false;

                    default:
                        throw new IllegalArgumentException(
                            "unsupported state=" + potentialNewState +
                            ", needs fixing");
                }
            }

            private static boolean matchesAny(BufferState value,
                                              BufferState... states) {

                for ( BufferState state : states ) {
                    if ( value == state ) {
                        return true;
                    }
                }
                return false;
            }
        } // type BufferState
    } // type BufferedListener

    private static class DisconnectableListener {
        private final TestChunk.Listener rawListener;
        private final TestChunk.Listener disconnectableListener;
        private final DisconnectableProxyHandler proxyHandler;

        public DisconnectableListener(TestChunk.Listener pRawListener) {
            ObjectTools.paramNullCheck(pRawListener, "pRawListener");
            this.rawListener = pRawListener;

            proxyHandler = new DisconnectableProxyHandler(Listener.class);
            disconnectableListener = (Listener) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] { Listener.class },
                proxyHandler);
        }

        public TestChunk.Listener getDisconnectableListener() {
            return disconnectableListener;
        }

        public void disconnect() {
            proxyHandler.disconnect();
        }

        private class DisconnectableProxyHandler implements InvocationHandler {
            private final Class<?> interfaceType;
            private final BooleanState disconnected;

            public DisconnectableProxyHandler(Class<?> interfaceType) {
                ObjectTools.paramNullCheck(interfaceType, "interfaceType");
                this.interfaceType = interfaceType;
                disconnected = new BooleanState(false);
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {

                if ( interfaceType.equals(method.getDeclaringClass()) ) {
                    if ( disconnected.isTrue() ) {
//ThreadTools.outln("inside DisconnectableListener, returning null (disconnected), method=" + method.getName() + ", args.length=" + (args == null ? 0 : args.length) + ", rawListener=" + rawListener);
                        return null;
                    }
//ThreadTools.outln("inside DisconnectableListener, proxy to rawListener, method=" + method.getName() + ", args.length=" + (args == null ? 0 : args.length) + ", rawListener=" + rawListener);

                    return method.invoke(rawListener, args);
                } else {
//if ( !"toString".equals(method.getName() ) ) {
//    ThreadTools.outln("inside DisconnectableListener, proxy to 'this', method=" + method.getName() + ", args.length=" + (args == null ? 0 : args.length) + ", rawListener=" + rawListener);
//}
                    return method.invoke(this, args);
                }
            }

            @Override
            public String toString() {
                return getClass().getSimpleName() + "[disconnected=" + disconnected.isTrue() + "]";
            }

            public void disconnect() {
                disconnected.setState(true);
            }
        } // type DisconnectableProxyHandler
    } // type DisconnectableListener
}