package com.abc.ds.tests;

import com.programix.testing.*;
import com.programix.thread.*;

/**
 * Test helper which starts up an internal thread which repeatedly calls
 * notifyAll() [several times a second] on the specified lockObject.
 * This can flush out problems where a thread waiting for notification
 * mistakenly assumes that just receiving notification is enough (perhaps
 * using an "if" instead of a "while").
 */
public class TestNastyNotifier {
    private final TestDSHelper.TestAccess testAccess;
    private final Object lockObject;
    private final TestDSBase.RunState runState;

    public TestNastyNotifier(Object lockObject,
                             TestThreadFactory threadFactory,
                             TestDSHelper.TestAccess testAccess) {

        this.lockObject = lockObject;
        this.testAccess = testAccess;

        runState = new TestDSBase.RunState();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                runWork();
            }
        };

        threadFactory.createThreadFor(r, "NastyNotifier");
    }

    private void runWork() {
        runState.registerCallerAsInternalThread();
        try {
            //testAccess.outln("NastyNotifier - thread starting");
            while (runState.isKeepRunning()) {
                synchronized (lockObject) {
                    lockObject.notifyAll();
                }
                ThreadTools.napRandom(25, 75);
            }
        } catch ( InterruptException x ) {
            // ignore and die
        } finally {
            //testAccess.outln("NastyNotifier - thread finished");
            runState.setNoLongerRunning();
        }
    }

    public void stopRequest() {
        runState.stopRequest();
    }

    public boolean waitUntilDone(long msTimeout) throws InterruptedException {
        return runState.waitWhileStillRunning(msTimeout);
    }
}
