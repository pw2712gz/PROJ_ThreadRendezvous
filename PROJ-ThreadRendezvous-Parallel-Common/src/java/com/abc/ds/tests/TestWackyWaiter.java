package com.abc.ds.tests;

import java.util.*;

import com.programix.testing.*;
import com.programix.thread.ix.*;

/**
 * Test helper which starts up several threads which all repeatedly wait for
 * notification on the specified lockObject.
 * This can flush out any problems with the use of notify() where notifyAll()
 * should have been used.
 */
public class TestWackyWaiter {
    private final TestDSHelper.TestAccess testAccess;
    private final Object lockObject;
    private final ThreadStore threadStore;
    private volatile boolean noStopRequested;

    public TestWackyWaiter(Object lockObject,
                           TestThreadFactory threadFactory,
                           TestDSHelper.TestAccess testAccess) {

        this.lockObject = lockObject;
        this.testAccess = testAccess;

        threadStore = new ThreadStore();

        noStopRequested = true;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                runWork();
            }
        };

        threadFactory.createThreadFor(r, "WackyWaiterA");
        threadFactory.createThreadFor(r, "WackyWaiterB");
        threadFactory.createThreadFor(r, "WackyWaiterC");
        threadFactory.createThreadFor(r, "WackyWaiterD");
        threadFactory.createThreadFor(r, "WackyWaiterE");
    }

    private void runWork() {
        try {
            threadStore.add(Thread.currentThread());
            //testAccess.outln("WackyWaiter - thread starting");
            while (noStopRequested) {
                synchronized (lockObject) {
                    lockObject.wait();
                }
            }
        } catch ( InterruptedException x ) {
            // ignore and die
        } finally {
            //testAccess.outln("WackyWaiter - thread finished");
            threadStore.remove(Thread.currentThread());
        }
    }

    public void stopRequest() {
        noStopRequested = false;
        int count = threadStore.interruptAllThreads();
        //testAccess.outln("WackyWaiter - interrupted " + count + " threads.");
    }

    public boolean waitUntilDone(long msTimeout) throws InterruptedException {
        return threadStore.waitUntilEmpty(msTimeout);
    }

    private static class ThreadStore {
        private final Set<Thread> threadSet;
        private final WaiterIx waiter;
        private final WaiterIx.Condition emptyCondition;

        public ThreadStore() {
            threadSet = new HashSet<>();
            waiter = new WaiterIx(this);
            emptyCondition = waiter.createCondition(new WaiterIx.Expression() {
                @Override
                public boolean isTrue() {
                    return threadSet.isEmpty();
                }
            });
        }

        public synchronized boolean waitUntilEmpty(long msTimeout) throws InterruptedException {
            return emptyCondition.waitUntilTrue(msTimeout);
        }

        public synchronized void add(Thread thread) {
            threadSet.add(thread);
            notifyAll();
        }

        public synchronized void remove(Thread thread) {
            threadSet.remove(thread);
            notifyAll();
        }

        public synchronized int interruptAllThreads() {
            Thread[] threads = threadSet.toArray(new Thread[threadSet.size()]);
            for (Thread thread : threads) {
                thread.interrupt();
            }
            return threads.length;
        }
    } // type ThreadStore
}
