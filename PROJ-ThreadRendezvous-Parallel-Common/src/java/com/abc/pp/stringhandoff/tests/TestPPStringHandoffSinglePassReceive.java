package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffSinglePassReceive extends TestPPStringHandoffBase {
    public TestPPStringHandoffSinglePassReceive(StringHandoffFactory factory,
                                                TestThreadFactory threadFactory) {
        super("single item pass and receive", new BasicScoringInfo(20, 3), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            testSingleItemHandoffReceiverFirst();
            outln("====================");
            testSingleItemHandoffPasserFirst();
            outln("====================");
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    private void testSingleItemHandoffReceiverFirst() throws InterruptedException {
        outln("-- receiver shows up first --");
        StringHandoff sh = createDS();
        String singleItem = "apple";
        Receiver receiver = new Receiver(sh, RECEIVE_INTERVAL, 0L);
        try {
            receiver.addExpectedItemToBeRemoved(singleItem);
            Thread.sleep(200);
            outln("Attempting to call pass(\"" + singleItem + "\")...");
            sh.pass(singleItem);
            outln("...finished passing", true);
            Thread.sleep(200);
        } catch (InterruptedException x) {
            failureExceptionWithStackTrace(x);
        } finally {
            receiver.stopRequest();
            receiver.waitUntilDone(2000L);
        }
    }

    private void testSingleItemHandoffPasserFirst() throws InterruptedException {
        outln("-- passer shows up first --");
        StringHandoff sh = createDS();
        String singleItem = "apple";
        Passer passer = new Passer(sh, PASS_INTERVAL, 0L, singleItem);
        try {
            Thread.sleep(200);
            outln("Attempting to call receive()...");
            String item = sh.receive();
            outln("received item", item, singleItem);
            Thread.sleep(200);
        } catch (InterruptedException x) {
            failureExceptionWithStackTrace(x);
        } finally {
            passer.stopRequest();
            passer.waitUntilDone(2000L);
        }
    }
}
