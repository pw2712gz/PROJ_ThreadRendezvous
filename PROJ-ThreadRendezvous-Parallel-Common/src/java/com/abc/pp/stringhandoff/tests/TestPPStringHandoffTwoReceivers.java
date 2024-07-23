package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffTwoReceivers extends TestPPStringHandoffBase {
    public TestPPStringHandoffTwoReceivers(StringHandoffFactory factory,
                                           TestThreadFactory threadFactory) {
        super("2+ receivers, expecting IllegalStateException", new BasicScoringInfo(3, 22), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            launch((launcher) -> testTwoReceiversNoTimeout(launcher));
            outln("====================");
            launch((launcher) -> testTwoReceiversBothCanTimeout(launcher));
            outln("====================");
            launch((launcher) -> testTwoReceiversFirstCanTimeout(launcher));
            outln("====================");
            launch((launcher) -> testFirstWorksAfterSecondThrows(launcher));
            outln("====================");
            launch((launcher) -> testThreeReceivers(launcher));
            outln("====================");
            launch((launcher) -> testThreeReceiversFirstSucceeds(launcher));
            outln("====================");
            launch((launcher) -> testTimeoutClearsReceiveIndicator(launcher));
            outln("====================");
            launch((launcher) -> testInterruptClearsItem(launcher));
            outln("====================");
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    protected void launch(Launchable launchable) throws InterruptedException {
        launchWithDeadline(10_000L, threadFactory, (launcher) -> {
            launchable.launch(launcher);
            launcher.fireStartingGun();
        });
    }

    private void testTwoReceiversNoTimeout(Launcher launcher) throws InterruptedException {
        outln("-- two receivers - no timeout --");
        StringHandoff sh = createDS();

        RunState runStateFirst = launcher.launch("ReceiverA", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("ReceiverB", () -> {
            try {
                sleepAndLog(500L);
                receive(new ReceiveConfig.Builder(sh)
                    .setExpectIllegalStateException(true)
                    .create());
            } finally {
                outln("sending a stopRequest to " + runStateFirst.getInternalThreadNameWhenRegistered());
                runStateFirst.stopRequest();
            }
        });
    }

    private void testTwoReceiversBothCanTimeout(Launcher launcher) throws InterruptedException {
        outln("-- two receivers - both can timeout --");
        StringHandoff sh = createDS();

        RunState runStateFirst = launcher.launch("ReceiverA", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setTimeout(4_000L)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("ReceiverB", () -> {
            try {
                sleepAndLog(500L);
                receive(new ReceiveConfig.Builder(sh)
                    .setTimeout(3_000L)
                    .setExpectIllegalStateException(true)
                    .create());
            } finally {
                outln("sending a stopRequest to " + runStateFirst.getInternalThreadNameWhenRegistered());
                runStateFirst.stopRequest();
            }
        });
    }

    private void testTwoReceiversFirstCanTimeout(Launcher launcher) throws InterruptedException {
        outln("-- two receivers - first can timeout --");
        StringHandoff sh = createDS();

        RunState runStateFirst = launcher.launch("ReceiverA", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setTimeout(4_000L)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("ReceiverB", () -> {
            try {
                sleepAndLog(500L);
                receive(new ReceiveConfig.Builder(sh)
                    .setExpectIllegalStateException(true)
                    .create());
            } finally {
                outln("sending a stopRequest to " + runStateFirst.getInternalThreadNameWhenRegistered());
                runStateFirst.stopRequest();
            }
        });
    }

    private void testFirstWorksAfterSecondThrows(Launcher launcher) throws InterruptedException {
        outln("-- two receivers - confirm first still works even after second throws IllegalStateEx --");
        StringHandoff sh = createDS();

        String[] items = { "apple", "banana", "cherry" };
        // time    Pa      Ra      Rb
        //    0
        //  100  apple
        //  200
        //  300
        //  400
        //  500          apple
        //       banana
        //  600
        //  700                   Rb attempts to receive, gets exception, shouldn't break anything about Pa and Ra
        //  800
        //  900          banana
        //       cherry
        // 1000
        // 1100
        // 1200
        // 1300          cherry
        //

        long msDelayBeforeFirstPass = 500L;
        long msDelayBeforeFirstReceive = 100L;
        long msDelayBetweenHandoffs = 400L;
        long msDelayBeforeSecondReceiverAttempt = 700L;

        launcher.launch("PasserA", () -> {
            sleepAndLog(msDelayBeforeFirstPass);
            for (String item : items) {
                pass(new PassConfig.Builder(sh, item).create());
                sleepAndLog(msDelayBetweenHandoffs);
            }
        });

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(msDelayBeforeFirstReceive);
            for (String expectedItem : items) {
                receive(new ReceiveConfig.Builder(sh)
                    .setExpectedItem(expectedItem)
                    .setExpectedTime(msDelayBetweenHandoffs, 200)
                    .create());
            }
        });

        launcher.launch("ReceiverB", () -> {
            sleepAndLog(msDelayBeforeSecondReceiverAttempt);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectIllegalStateException(true)
                .create());
        });
    }

    private void testThreeReceivers(Launcher launcher) throws InterruptedException {
        outln("-- three receivers --");
        StringHandoff sh = createDS();

        RunState runStateFirst = launcher.launch("ReceiverA", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setTimeout(4_000L)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("ReceiverB", () -> {
            sleepAndLog(500L);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectIllegalStateException(true)
                .create());
        });

        launcher.launch("ReceiverC", () -> {
            try {
                sleepAndLog(1000L);
                receive(new ReceiveConfig.Builder(sh)
                    .setExpectIllegalStateException(true)
                    .create());
            } finally {
                outln("sending a stopRequest to " + runStateFirst.getInternalThreadNameWhenRegistered());
                runStateFirst.stopRequest();
            }
        });
    }

    private void testThreeReceiversFirstSucceeds(Launcher launcher) throws InterruptedException {
        outln("-- three receivers, one passer, 1st receiver succeeds --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "watermelon";

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setTimeout(4_000L)
                .setExpectedItem(singleSuccessfulItem)
                .create());
        });

        launcher.launch("ReceiverB", () -> {
            sleepAndLog(500L);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectIllegalStateException(true)
                .create());
        });

        launcher.launch("ReceiverC", () -> {
            sleepAndLog(1000L);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectIllegalStateException(true)
                .create());
        });

        launcher.launch("PasserA", () -> {
            sleepAndLog(1500L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem).create());
        });
    }

    private void testTimeoutClearsReceiveIndicator(Launcher launcher) throws InterruptedException {
        outln("-- two receivers, 1st times out, 2nd receiver succeeds --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "watermelon";

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setTimeout(500L)
                .setExpectTimedOutException(true)
                .create());
        });

        launcher.launch("ReceiverB", () -> {
            sleepAndLog(800L);
            receive(new ReceiveConfig.Builder(sh)
                .setTimeout(500L)
                .setExpectedItem(singleSuccessfulItem)
                .create());
        });

        launcher.launch("PasserA", () -> {
            sleepAndLog(1_000L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem).create());
        });
    }

    private void testInterruptClearsItem(Launcher launcher) throws InterruptedException {
        outln("-- two passers, 1st gets interrupted, 2nd passer succeeds --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "watermelon";

        RunState firstRunState = launcher.launch("ReceiverA", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setTimeout(900L)
                .setExpectTimedOutException(true)
                .create());
        });

        launcher.launch("Interruptor", () -> {
            sleepAndLog(600L);
            Thread thread = firstRunState.getInternalThreadWaitingIfNecessary();
            outln(String.format("calling interrupt() on receiver thread (%s) ...", thread.getName()));
            thread.interrupt();
        });

        launcher.launch("ReceiverB", () -> {
            sleepAndLog(800L);
            receive(new ReceiveConfig.Builder(sh)
                .setTimeout(500L)
                .setExpectedItem(singleSuccessfulItem)
                .create());
        });

        launcher.launch("PasserA", () -> {
            sleepAndLog(1_000L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem).create());
        });
    }


//    private void testTwoReceivers() throws InterruptedException {
//        StringHandoff handoff = createDS();
//
//        Receiver receiver = new Receiver(handoff, 200L, 0L);
//        Thread.sleep(500);
//
//        try {
//            outln("Attempting to call receive()");
//            handoff.receive();
//            outln("Just received something--trouble", false);
//        } catch ( IllegalStateException x ) {
//            outln("Got exception: " + x, true);
//        }
//
//        NanoTimer timer = NanoTimer.createStopped();
//        for ( int i = 0; i < 3; i++ ) {
//            timer.resetAndStart();
//            String item = SAMPLE_DATA[i];
//            handoff.pass(item, 500L);
//            timer.stop();
//            outln("...passed '" + item + "'", true);
//            outln("   seconds until passed", timer.getElapsedSeconds(), (i == 0 ? 0L : 200L) / 1000.0, 0.080, 7);
//        }
//
//        Thread.sleep(500); // give receiver a chance to try to receive again
//
//        // Shutting down the Receiver should cause StringHandoff to clear
//        // out the status that someone is trying to receive.
//        receiver.stopRequest();
//        receiver.waitUntilDone(5000);
//
//        // Create a Passer passing different data to be sure that the old
//        // data is irrelevant.
//        String[] otherData = new String[] { "orange", "pear", "tomato" };
//        Passer passer = new Passer(handoff, 0L, 0L, otherData);
//        Thread.sleep(500); // give passer a change to load up a string
//
//        outln("handoff.receive()", handoff.receive(), otherData[0]);
//        outln("handoff.receive()", handoff.receive(), otherData[1]);
//
//        passer.stopRequest();
//        passer.waitUntilDone(5000);
//    }
}
