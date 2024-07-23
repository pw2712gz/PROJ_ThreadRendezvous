package com.abc.pp.stringhandoff.tests;

import com.abc.ds.tests.*;
import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;
import com.programix.thread.*;


public class TestPPStringHandoffPassExpectTimeout extends TestPPStringHandoffBase {
    public TestPPStringHandoffPassExpectTimeout(StringHandoffFactory factory,
                                                TestThreadFactory threadFactory) {
        super("pass(item, msTimeout) expecting TimedOutException", new BasicScoringInfo(6, 8), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            testHandoffExpectTimeout(false, false);
            outln("====================");
            testHandoffExpectTimeout(false, true);
            outln("====================");
            testHandoffExpectTimeout(true, false);
            outln("====================");
            testHandoffExpectTimeout(true, true);
            outln("====================");
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    private void testHandoffExpectTimeout(boolean useWackyWaiter,
                                          boolean useNastyNotifier) throws InterruptedException {

        outln("-- expect timeout trying to pass --");
        outln("-- using WackyWaiter=" + useWackyWaiter + ", using NastyNotifier=" + useNastyNotifier);
        StringHandoff sh = createDS();
        String singleItem = "apple";

        TestNastyNotifier nn = useNastyNotifier ? new TestNastyNotifier(sh.getLockObject(), threadFactory, testAccess) : null;
        TestWackyWaiter ww = useWackyWaiter ? new TestWackyWaiter(sh.getLockObject(), threadFactory, testAccess) : null;

        long msInitialDelay = 2000;
        long msTimeout = msInitialDelay / 2;
        Receiver receiver = new Receiver(sh, RECEIVE_INTERVAL, msInitialDelay);
        NanoTimer timer = NanoTimer.createStopped();
        try {
            receiver.addExpectedItemToBeRemoved(singleItem);
            Thread.sleep(200);
            outln("Attempting to call pass(\"" + singleItem + "\", " + msTimeout + ")...");
            timer.resetAndStart();
            sh.pass(singleItem, msTimeout);
            timer.stop();
            outln("whoops, didn't get a TimedOutExcception as expected", false);
            Thread.sleep(200);
        } catch (TimedOutException x) {
            outln("...got TimedOutException (this is good)", true);
            outln("   seconds until timeout", timer.getElapsedSeconds(), msTimeout / 1000.0, 0.200, 7);
        } catch (Exception x) {
            failureExceptionWithStackTrace(x);
        } finally {
            receiver.stopRequest();
            receiver.waitUntilDone(2000L);
            if (nn != null) {
                nn.stopRequest();
                nn.waitUntilDone(1000);
            }
            if (ww != null) {
                ww.stopRequest();
                ww.waitUntilDone(1000);
            }
        }
    }
}
