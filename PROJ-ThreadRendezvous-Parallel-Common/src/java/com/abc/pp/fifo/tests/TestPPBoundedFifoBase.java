package com.abc.pp.fifo.tests;

import java.util.*;

import com.abc.ds.tests.*;
import com.abc.pp.fifo.*;
import com.programix.testing.*;
import com.programix.thread.*;
import com.programix.util.*;

/* deliberate package access */
abstract class TestPPBoundedFifoBase extends TestDSBase {
    protected final PPBoundedFifoFactory factory;
    protected final TestDSHelper<String> testHelper;
    protected final TestThreadFactory threadFactory;

    protected TestPPBoundedFifoBase(String titleSuffix,
                                    PPBoundedFifoFactory factory,
                                    TestThreadFactory threadFactory) {

        super("PPBoundedFifo - " + titleSuffix);
        this.factory = factory;
        this.threadFactory = threadFactory;
        testHelper = new TestDSHelper.Builder<String>()
            .setItemType(String.class)
            .setTestAccess(testAccess)
            .setAllowDuplicates(true)
            .setOrderMatters(true)
            .setWrapItemsInQuotes(true)
            .create();
    }

    protected TestPPBoundedFifoBase(String titleSuffix,
                                    PPBoundedFifoFactory factory) {
        this(titleSuffix, factory, null);
    }


    protected PPBoundedFifo<String> createDS() {
        int capacity = 100; // pretty big to for basic tests
        return createDS(capacity);
    }

    protected PPBoundedFifo<String> createDS(int capacity) {
        outln("Creating a new PPBoundedFifo<String> instance with capacity=" +
            capacity + " ...");
        PPBoundedFifo<String> fifo = factory.create(String.class, capacity);
        outln("   ...created: " + fifo.getClass().getCanonicalName());
        return fifo;
    }

    protected void add(PPBoundedFifo<String> fifo, String... items) {
        try {
            if (items == null) items = StringTools.ZERO_LEN_ARRAY;

            for ( String item : items ) {
                fifo.add(item);
                outln("add(" + StringTools.quoteWrap(item) + ")");
            }
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    protected void addBulk(PPBoundedFifo<String> fifo, String... items) {
        try {
            if (items == null) items = StringTools.ZERO_LEN_ARRAY;

            outln("Adding " + items.length + " items: " + formatCommaDelimited(items));
            for ( String item : items ) {
                fifo.add(item);
            }
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    protected void checkRemoveAll(PPBoundedFifo<String> ds,
                                  String... expectedItems) {

        testHelper.check("removeAll()", ds.removeAll(), expectedItems);
    }

    protected void checkRemoveAtLeastOne(PPBoundedFifo<String> ds,
                                         String... expectedItems) throws InterruptedException {

        testHelper.check("removeAtLeastOne()", ds.removeAtLeastOne(), expectedItems);
    }

    protected TestWackyWaiter kickoffWackyWaiter(Object lockObject) throws IllegalStateException {
        if (threadFactory == null) {
            throw new IllegalStateException("can't use WackyWaiter, no TestThreadFactory supplied at construction");
        }
        return new TestWackyWaiter(lockObject, threadFactory, testAccess);
    }

    protected TestNastyNotifier kickoffNastyNotifier(Object lockObject) throws IllegalStateException {
        if (threadFactory == null) {
            throw new IllegalStateException("can't use NastyNotifier, no TestThreadFactory supplied at construction");
        }
        return new TestNastyNotifier(lockObject, threadFactory, testAccess);
    }

    protected class Remover<T> {
        private final PPBoundedFifo<T> ds;
        private final long msBetweenRemoveAttempts;
        private final ExpectedRemoveList expectedRemoveList;
        private final TestDSBase.RunState runState;

        public Remover(PPBoundedFifo<T> ds,
                       long msBetweenRemoveAttempts) {

            this.ds = ds;
            this.msBetweenRemoveAttempts = msBetweenRemoveAttempts;

            expectedRemoveList = new ExpectedRemoveList();

            runState = new TestDSBase.RunState();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    runWork();
                }
            };

            threadFactory.createThreadFor(r, "Remover");
        }

        private void runWork() {
            runState.registerCallerAsInternalThread();
            try {
                testAccess.outln("Remover starting");
                NanoTimer timer = NanoTimer.createStopped();
                while (runState.isKeepRunning()) {
                    timer.resetAndStart();
                    T item = ds.remove();
                    timer.stop();

                    T expectedItem = expectedRemoveList.removeOrNull();
                    if (expectedItem == null) {
                        testAccess.outln(String.format(
                            "removed '%s' after blocking for %.5f seconds",
                            item, timer.getElapsedSeconds()));
                    } else {

                    }

                    Thread.sleep(msBetweenRemoveAttempts);
                }
            } catch ( InterruptedException x ) {
                // ignore and die
            } finally {
                testAccess.outln("Remover finished");
                runState.setNoLongerRunning();
            }
        }

        public void stopRequest() {
            runState.stopRequest();
        }

        public boolean waitUntilDone(long msTimeout) throws InterruptedException {
            return runState.waitWhileStillRunning(msTimeout);
        }

        public void addExpectedItemToBeRemoved(T item) {
            expectedRemoveList.add(item);
        }

        private class ExpectedRemoveList {
            private List<T> list;

            public ExpectedRemoveList() {
                list = new LinkedList<>();
            }

            public synchronized void add(T item) {
                list.add(item);
            }

            public synchronized T removeOrNull() {
                return list.isEmpty() ? null : list.remove(0);
            }
        } // type ExpectedRemoveList
    } // type Remover
}
