package com.abc.pp.stringhandoff.tests;

import com.abc.ds.tests.*;
import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;
import com.programix.thread.*;


public class TestPPStringHandoffSinglePassReceivePlentyOfTime extends TestPPStringHandoffBase {
    public TestPPStringHandoffSinglePassReceivePlentyOfTime(StringHandoffFactory factory,
                                                TestThreadFactory threadFactory) {
        super("plenty long timeout, single item pass and receive", factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            testSingleItemHandoffReceiverFirst(false, false);
            testSingleItemHandoffReceiverFirst(false, true);
            testSingleItemHandoffReceiverFirst(true, false);
            testSingleItemHandoffReceiverFirst(true, true);

            testSingleItemHandoffPasserFirst(false, false);
            testSingleItemHandoffPasserFirst(false, true);
            testSingleItemHandoffPasserFirst(true, false);
            testSingleItemHandoffPasserFirst(true, true);
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    private void testSingleItemHandoffReceiverFirst(boolean useWackyWaiter,
                                                    boolean useNastyNotifier) throws InterruptedException {

        outln("-- plenty long timeout, receiver shows up first --");
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
            sh.pass(singleItem, msTimeout);
            outln("...finished passing", true);
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

    private void testSingleItemHandoffPasserFirst(boolean useWackyWaiter,
                                                  boolean useNastyNotifier) throws InterruptedException {

        outln("-- plenty long timeout, passer shows up first --");
        outln("-- using WackyWaiter=" + useWackyWaiter + ", using NastyNotifier=" + useNastyNotifier);
        StringHandoff sh = createDS();
        String singleItem = "apple";

        TestNastyNotifier nn = useNastyNotifier ? new TestNastyNotifier(sh.getLockObject(), threadFactory, testAccess) : null;
        TestWackyWaiter ww = useWackyWaiter ? new TestWackyWaiter(sh.getLockObject(), threadFactory, testAccess) : null;

        Passer passer = new Passer(sh, RECEIVE_INTERVAL, 0L, singleItem);
        long msTimeout = PASS_INTERVAL * 2;
        NanoTimer timer = NanoTimer.createStopped();
        try {
            Thread.sleep(200);
            outln("Attempting to call receive(" + msTimeout + ")...");
            timer.resetAndStart();
            String item = sh.receive(msTimeout);
            timer.stop();
            outln(String.format("received item in %.5f seconds", timer.getElapsedSeconds()), item, singleItem);
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
