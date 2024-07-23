package com.abc.pp.stringhandoff.tests;

import com.abc.ds.tests.*;
import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;
import com.programix.thread.*;


public class TestPPStringHandoffPassWithPlentyOfTime extends TestPPStringHandoffBase {
    public TestPPStringHandoffPassWithPlentyOfTime(StringHandoffFactory factory,
                                                TestThreadFactory threadFactory) {
        super("pass(item, msTimeout) with plenty of time", new BasicScoringInfo(6, 24), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            testSingleItemHandoffReceiverFirst(false, false);
            outln("====================");
            testSingleItemHandoffReceiverFirst(false, true);
            outln("====================");
            testSingleItemHandoffReceiverFirst(true, false);
            outln("====================");
            testSingleItemHandoffReceiverFirst(true, true);
            outln("====================");

            testSingleItemHandoffReceiverSecond(false, false);
            outln("====================");
            testSingleItemHandoffReceiverSecond(false, true);
            outln("====================");
            testSingleItemHandoffReceiverSecond(true, false);
            outln("====================");
            testSingleItemHandoffReceiverSecond(true, true);
            outln("====================");
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    private void testSingleItemHandoffReceiverFirst(boolean useWackyWaiter,
                                                    boolean useNastyNotifier) throws InterruptedException {

        outln("-- plenty long pass timeout, receiver shows up first --");
        outln("-- using WackyWaiter=" + useWackyWaiter + ", using NastyNotifier=" + useNastyNotifier);
        StringHandoff sh = createDS();
        String singleItem = "apple";

        TestNastyNotifier nn = useNastyNotifier ? new TestNastyNotifier(sh.getLockObject(), threadFactory, testAccess) : null;
        TestWackyWaiter ww = useWackyWaiter ? new TestWackyWaiter(sh.getLockObject(), threadFactory, testAccess) : null;

        Receiver receiver = new Receiver(sh, RECEIVE_INTERVAL, 0L);
        long msTimeout = RECEIVE_INTERVAL * 2;
        try {
            receiver.addExpectedItemToBeRemoved(singleItem);
            Thread.sleep(200);
            outln("Attempting to call pass(\"" + singleItem + "\", " + msTimeout + ")...");
            NanoTimer timer = NanoTimer.createStarted();
            sh.pass(singleItem, msTimeout);
            timer.stop();
            outln("...finished passing", true);
            outln("   seconds to pass", timer.getElapsedSeconds(), 0.0, 0.200, 7);
            Thread.sleep(200);
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

    private void testSingleItemHandoffReceiverSecond(boolean useWackyWaiter,
                                                     boolean useNastyNotifier) throws InterruptedException {

        outln("-- plenty long pass timeout, receiver shows up second --");
        outln("-- using WackyWaiter=" + useWackyWaiter + ", using NastyNotifier=" + useNastyNotifier);
        StringHandoff sh = createDS();
        String singleItem = "apple";

        TestNastyNotifier nn = useNastyNotifier ? new TestNastyNotifier(sh.getLockObject(), threadFactory, testAccess) : null;
        TestWackyWaiter ww = useWackyWaiter ? new TestWackyWaiter(sh.getLockObject(), threadFactory, testAccess) : null;

        long msExpectedToWaitToPass = 1000L;
        Receiver receiver = new Receiver(sh, RECEIVE_INTERVAL, msExpectedToWaitToPass);
        long msTimeout = msExpectedToWaitToPass * 2;
        try {
            receiver.addExpectedItemToBeRemoved(singleItem);
            outln("Attempting to call pass(\"" + singleItem + "\", " + msTimeout + ")...");
            NanoTimer timer = NanoTimer.createStarted();
            sh.pass(singleItem, msTimeout);
            timer.stop();
            outln("...finished passing", true);
            outln("   seconds to pass", timer.getElapsedSeconds(), msExpectedToWaitToPass / 1000.0, 0.200, 7);
            Thread.sleep(200);
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
