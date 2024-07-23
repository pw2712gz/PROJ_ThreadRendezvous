package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;
import com.programix.util.*;


public class TestPPStringHandoffMultiPassReceive extends TestPPStringHandoffBase {
    public TestPPStringHandoffMultiPassReceive(StringHandoffFactory factory,
                                               TestThreadFactory threadFactory) {
        super("multiple items pass and receive", new BasicScoringInfo(15, 12), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            testReceiverFirst();
            outln("====================");
            testPasserFirst();
            outln("====================");
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    private void testReceiverFirst() throws InterruptedException {
        outln("-- receiver shows up first --");
        StringHandoff sh = createDS();
        Receiver receiver = new Receiver(sh, 200L, 0L);
        try {
            receiver.addExpectedItemsToBeRemoved(SAMPLE_DATA);
            Thread.sleep(200);

            boolean even = true;
            for ( String item : SAMPLE_DATA ) {
                if (even) {
                    outln("Calling pass(" + StringTools.quoteWrap(item) + ")... [no timeout: never times out]");
                    sh.pass(item);
                } else {
                    outln("Calling pass(" + StringTools.quoteWrap(item) + ", 0L)... [0ms timeout: never times out]");
                    sh.pass(item, 0L);
                }
                even = !even;
                Thread.sleep(50);
            }

            Thread.sleep(200);
        } catch (InterruptedException x) {
            failureExceptionWithStackTrace(x);
        } finally {
            receiver.stopRequest();
            receiver.waitUntilDone(2000L);
        }
    }

    private void testPasserFirst() throws InterruptedException {
        outln("-- passer shows up first --");
        StringHandoff sh = createDS();
        Passer passer = new Passer(sh, 200L, 0L, SAMPLE_DATA);
        try {
            Thread.sleep(200);
            boolean even = true;
            for ( String expectedItem : SAMPLE_DATA ) {
                String item = null;
                if (even) {
                    outln("Calling receive()... [no timeout: never times out]");
                    item = sh.receive();
                } else {
                    outln("Calling receive(0L)... [0ms timeout: never times out]");
                    item = sh.receive(0L);
                }

                outln("received item", item, expectedItem);
                even = !even;
                Thread.sleep(50);
            }
            Thread.sleep(200);
        } catch (InterruptedException x) {
            failureExceptionWithStackTrace(x);
        } finally {
            passer.stopRequest();
            passer.waitUntilDone(2000L);
        }
    }
}
