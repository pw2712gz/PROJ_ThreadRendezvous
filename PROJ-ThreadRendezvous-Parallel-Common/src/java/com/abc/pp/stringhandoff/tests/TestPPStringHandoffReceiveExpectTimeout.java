package com.abc.pp.stringhandoff.tests;

import com.abc.ds.tests.*;
import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;
import com.programix.thread.*;


public class TestPPStringHandoffReceiveExpectTimeout extends TestPPStringHandoffBase {
    public TestPPStringHandoffReceiveExpectTimeout(StringHandoffFactory factory,
                                                TestThreadFactory threadFactory) {
        super("receive(msTimeout) expecting TimedOutException", new BasicScoringInfo(6, 8), factory, threadFactory);
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

        outln("-- expect timeout trying to receive --");
        outln("-- using WackyWaiter=" + useWackyWaiter + ", using NastyNotifier=" + useNastyNotifier);
        StringHandoff sh = createDS();
        String singleItem = "apple";

        TestNastyNotifier nn = useNastyNotifier ? new TestNastyNotifier(sh.getLockObject(), threadFactory, testAccess) : null;
        TestWackyWaiter ww = useWackyWaiter ? new TestWackyWaiter(sh.getLockObject(), threadFactory, testAccess) : null;

        long msInitialDelay = 2000;
        long msTimeout = msInitialDelay / 2;
        Passer passer = new Passer(sh, msInitialDelay, msInitialDelay, singleItem);
        NanoTimer timer = NanoTimer.createStopped();
        try {
            Thread.sleep(200);
            outln("Attempting to call receive(" + msTimeout + ")...");
            timer.resetAndStart();
            @SuppressWarnings("unused")
            String item = sh.receive(msTimeout);
            timer.stop();
            outln("whoops, didn't get a TimedOutExcception as expected", false);
            Thread.sleep(200);
        } catch (TimedOutException x) {
            outln("...got TimedOutException (this is good)", true);
            outln("   seconds until timeout", timer.getElapsedSeconds(), msTimeout / 1000.0, 0.200, 7);
        } catch (Exception x) {
            failureExceptionWithStackTrace(x);
        } finally {
            passer.stopRequest();
            passer.waitUntilDone(2000L);
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
