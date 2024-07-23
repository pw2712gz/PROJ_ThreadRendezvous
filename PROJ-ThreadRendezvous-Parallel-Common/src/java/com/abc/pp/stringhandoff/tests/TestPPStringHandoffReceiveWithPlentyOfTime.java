package com.abc.pp.stringhandoff.tests;

import com.abc.ds.tests.*;
import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;
import com.programix.thread.*;


public class TestPPStringHandoffReceiveWithPlentyOfTime extends TestPPStringHandoffBase {
    public TestPPStringHandoffReceiveWithPlentyOfTime(StringHandoffFactory factory,
                                                TestThreadFactory threadFactory) {
        super("receive(msTimeout) with plenty of time", new BasicScoringInfo(6, 16), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            testSingleItemHandoffPasserFirst(false, false);
            outln("====================");
            testSingleItemHandoffPasserFirst(false, true);
            outln("====================");
            testSingleItemHandoffPasserFirst(true, false);
            outln("====================");
            testSingleItemHandoffPasserFirst(true, true);
            outln("====================");

            testSingleItemHandoffPasserSecond(false, false);
            outln("====================");
            testSingleItemHandoffPasserSecond(false, true);
            outln("====================");
            testSingleItemHandoffPasserSecond(true, false);
            outln("====================");
            testSingleItemHandoffPasserSecond(true, true);
            outln("====================");
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    private void testSingleItemHandoffPasserFirst(boolean useWackyWaiter,
                                                  boolean useNastyNotifier) throws InterruptedException {

        outln("-- plenty long receive timeout, passer shows up first --");
        outln("-- using WackyWaiter=" + useWackyWaiter + ", using NastyNotifier=" + useNastyNotifier);
        StringHandoff sh = createDS();
        String singleItem = "apple";

        TestNastyNotifier nn = useNastyNotifier ? new TestNastyNotifier(sh.getLockObject(), threadFactory, testAccess) : null;
        TestWackyWaiter ww = useWackyWaiter ? new TestWackyWaiter(sh.getLockObject(), threadFactory, testAccess) : null;

        Passer passer = new Passer(sh, PASS_INTERVAL, 0L, singleItem);
        long msTimeout = PASS_INTERVAL * 2;
        NanoTimer timer = NanoTimer.createStopped();
        try {
            Thread.sleep(200);
            outln("Attempting to call receive(" + msTimeout + ")...");
            timer.resetAndStart();
            String item = sh.receive(msTimeout);
            timer.stop();
            outln("...received item", item, singleItem);
            outln("   seconds to receive", timer.getElapsedSeconds(), 0.0, 0.200, 7);
            Thread.sleep(200);
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

    private void testSingleItemHandoffPasserSecond(boolean useWackyWaiter,
                                                   boolean useNastyNotifier) throws InterruptedException {

        outln("-- plenty long receive timeout, passer shows up second --");
        outln("-- using WackyWaiter=" + useWackyWaiter + ", using NastyNotifier=" + useNastyNotifier);
        StringHandoff sh = createDS();
        String singleItem = "apple";

        TestNastyNotifier nn = useNastyNotifier ? new TestNastyNotifier(sh.getLockObject(), threadFactory, testAccess) : null;
        TestWackyWaiter ww = useWackyWaiter ? new TestWackyWaiter(sh.getLockObject(), threadFactory, testAccess) : null;

        NanoTimer timer = NanoTimer.createStopped();
        long msExpectedToWaitToReceive = 1000L;
        long msTimeout = msExpectedToWaitToReceive * 2;
        Passer passer = new Passer(sh, RECEIVE_INTERVAL, msExpectedToWaitToReceive);
        try {
            outln("Attempting to call receive(" + msTimeout + ")...");
            timer.resetAndStart();
            String item = sh.receive(msTimeout);
            timer.stop();
            outln("...received item", singleItem, item);
            outln("   seconds to receive", timer.getElapsedSeconds(), msExpectedToWaitToReceive / 1000.0, 0.200, 7);
            Thread.sleep(200);
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
