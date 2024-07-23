package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffTwoPassers extends TestPPStringHandoffBase {
    public TestPPStringHandoffTwoPassers(StringHandoffFactory factory,
                                         TestThreadFactory threadFactory) {
        super("2+ passers, expecting IllegalStateException", new BasicScoringInfo(3, 23), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            launch((launcher) -> testTwoPassersNoTimeout(launcher));
            outln("====================");
            launch((launcher) -> testTwoPassersBothCanTimeout(launcher));
            outln("====================");
            launch((launcher) -> testTwoPassersFirstCanTimeout(launcher));
            outln("====================");
            launch((launcher) -> testFirstWorksAfterSecondThrows(launcher));
            outln("====================");
            launch((launcher) -> testThreePassers(launcher));
            outln("====================");
            launch((launcher) -> testThreePassersFirstSucceeds(launcher));
            outln("====================");
            launch((launcher) -> testTimeoutClearsItem(launcher));
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

    private void testTwoPassersNoTimeout(Launcher launcher) throws InterruptedException {
        outln("-- two passers - no timeout --");
        StringHandoff sh = createDS();

        RunState runStateFirst = launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, "watermelon")
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("PasserB", () -> {
            try {
                sleepAndLog(500L);
                pass(new PassConfig.Builder(sh, "cherry")
                    .setExpectIllegalStateException(true)
                    .create());
            } finally {
                outln("sending a stopRequest to " + runStateFirst.getInternalThreadNameWhenRegistered());
                runStateFirst.stopRequest();
            }
        });
    }

    private void testTwoPassersBothCanTimeout(Launcher launcher) throws InterruptedException {
        outln("-- two passers - both can timeout --");
        StringHandoff sh = createDS();

        RunState runStateFirst = launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, "watermelon")
                .setTimeout(4_000L)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("PasserB", () -> {
            try {
                sleepAndLog(500L);
                pass(new PassConfig.Builder(sh, "cherry")
                    .setTimeout(3_000L)
                    .setExpectIllegalStateException(true)
                    .create());
            } finally {
                outln("sending a stopRequest to " + runStateFirst.getInternalThreadNameWhenRegistered());
                runStateFirst.stopRequest();
            }
        });
    }

    private void testTwoPassersFirstCanTimeout(Launcher launcher) throws InterruptedException {
        outln("-- two passers - first can timeout --");
        StringHandoff sh = createDS();

        RunState runStateFirst = launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, "watermelon")
                .setTimeout(4_000L)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("PasserB", () -> {
            try {
                sleepAndLog(500L);
                pass(new PassConfig.Builder(sh, "cherry")
                    .setExpectIllegalStateException(true)
                    .create());
            } finally {
                outln("sending a stopRequest to " + runStateFirst.getInternalThreadNameWhenRegistered());
                runStateFirst.stopRequest();
            }
        });
    }

    private void testFirstWorksAfterSecondThrows(Launcher launcher) throws InterruptedException {
        outln("-- two passers - confirm first still works even after second throws IllegalStateEx --");
        StringHandoff sh = createDS();

        String[] items = { "apple", "banana", "cherry" };
        // time    P1      R      P2
        //    0
        //  100  apple
        //  200
        //  300
        //  400
        //  500          apple
        //       banana
        //  600
        //  700                   P2 attempts to pass, gets exception, shouldn't break anything about P1 and R
        //  800
        //  900          banana
        //       cherry
        // 1000
        // 1100
        // 1200
        // 1300          cherry
        //

        long msDelayBeforeFirstPass = 100L;
        long msDelayBeforeFirstReceive = 500L;
        long msDelayBetweenHandoffs = 400L;
        long msDelayBeforeSecondPasserAttempt = 700L;

        launcher.launch("PasserA", () -> {
            sleepAndLog(msDelayBeforeFirstPass);

            PassConfig.Builder confBuilder = new PassConfig.Builder()
                .setHandoff(sh)
                .setExpectedTime(msDelayBetweenHandoffs, 200);

            for (String item : items) {
                pass(confBuilder.setItem(item).create());
            }
        });

        launcher.launch("PasserB", () -> {
            sleepAndLog(msDelayBeforeSecondPasserAttempt);
            pass(new PassConfig.Builder(sh, "nectarine")
                .setExpectIllegalStateException(true)
                .create());
        });

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(msDelayBeforeFirstReceive);
            for (String expectedItem : items) {
                receive(new ReceiveConfig.Builder(sh)
                    .setExpectedItem(expectedItem)
                    .create());
                sleepAndLog(msDelayBetweenHandoffs);
            }
        });
    }

    private void testThreePassers(Launcher launcher) throws InterruptedException {
        outln("-- three passers --");
        StringHandoff sh = createDS();

        RunState runStateFirst = launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, "watermelon")
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("PasserB", () -> {
            sleepAndLog(500L);
            pass(new PassConfig.Builder(sh, "cherry")
                .setExpectIllegalStateException(true)
                .create());
        });

        launcher.launch("PasserC", () -> {
            try {
                sleepAndLog(1000L);
                pass(new PassConfig.Builder(sh, "peach")
                    .setExpectIllegalStateException(true)
                    .create());
            } finally {
                outln("sending a stopRequest to " + runStateFirst.getInternalThreadNameWhenRegistered());
                runStateFirst.stopRequest();
            }
        });
    }

    private void testThreePassersFirstSucceeds(Launcher launcher) throws InterruptedException {
        outln("-- three passers, one receiver, 1st passer succeeds --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "watermelon";

        launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem).create());
        });

        launcher.launch("PasserB", () -> {
            sleepAndLog(500L);
            pass(new PassConfig.Builder(sh, "cherry")
                .setExpectIllegalStateException(true)
                .create());
        });

        launcher.launch("PasserC", () -> {
            sleepAndLog(1_000L);
            pass(new PassConfig.Builder(sh, "peach")
                .setExpectIllegalStateException(true)
                .create());
        });

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(1500L);
            receive(new ReceiveConfig.Builder(sh).setExpectedItem(singleSuccessfulItem).create());
        });
    }

    private void testTimeoutClearsItem(Launcher launcher) throws InterruptedException {
        outln("-- two passers, 1st times out, 2nd passer succeeds --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "watermelon";

        launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, "uglifruit")
                .setTimeout(500L)
                .setExpectTimedOutException(true)
                .create());
        });

        launcher.launch("PasserB", () -> {
            sleepAndLog(1_000L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem)
                .setTimeout(500L)
                .create());
        });

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(1_200L);
            receive(new ReceiveConfig.Builder(sh).setExpectedItem(singleSuccessfulItem).create());
        });
    }

    private void testInterruptClearsItem(Launcher launcher) throws InterruptedException {
        outln("-- two passers, 1st gets interrupted, 2nd passer succeeds --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "watermelon";

        RunState firstPasserRunState = launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, "uglifruit")
                .setTimeout(900L)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("Interruptor", () -> {
            sleepAndLog(600L);
            Thread passerThread = firstPasserRunState.getInternalThreadWaitingIfNecessary();
            outln(String.format("calling interrupt() on passer thread (%s) ...", passerThread.getName()));
            passerThread.interrupt();
        });


        launcher.launch("PasserB", () -> {
            sleepAndLog(800L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem)
                .setTimeout(500L)
                .create());
        });

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(1_000L);
            receive(new ReceiveConfig.Builder(sh).setExpectedItem(singleSuccessfulItem).create());
        });
    }


//    private void testTwoPassers() throws InterruptedException {
//        StringHandoff handoff = createDS();
//
//        Passer passer = new Passer(handoff, 0L, 0L);
//        sleepWithLog(500); // give passer a change to load up a string
//
//        try {
//            outln("Attempting to pass 'grape'");
//            handoff.pass("grape");
//            outln("Just passed 'grape'", false);
//        } catch ( IllegalStateException x ) {
//            // This is correct, Passer is already trying to pass right now
//            outln("Got exception: " + x, true);
//        }
//
//        outln("handoff.receive()", handoff.receive(), SAMPLE_DATA[0]);
//        outln("handoff.receive()", handoff.receive(), SAMPLE_DATA[1]);
//        outln("handoff.receive()", handoff.receive(), SAMPLE_DATA[2]);
//        sleepWithLog(500); // give passer a chance to put in a fourth string
//
//        // Shutting down the Passer should cause StringHandoff to clear
//        // out the value that Passer was trying to pass and should also
//        // clear out the status that someone is trying to pass.
//        passer.stopRequest();
//        passer.waitUntilDone(5000);
//
//        // Create a new Passer passing different data to be sure that the old
//        // data got removed upon the 'stopRequest'.
//        String[] otherData = new String[] { "orange", "pear", "tomato" };
//        passer = new Passer(handoff, 0L, 0L, otherData);
//        sleepWithLog(500); // give passer a change to load up a string
//
//        outln("handoff.receive()", handoff.receive(), otherData[0]);
//        outln("handoff.receive()", handoff.receive(), otherData[1]);
//        sleepWithLog(500); // give passer a chance to put in a thrid string
//
//        passer.stopRequest();
//        passer.waitUntilDone(5000);
//
//        Receiver receiver = new Receiver(handoff, 0L, 200L);
//
//        // This should succeed since the old Passer is done.
//        NanoTimer timer = NanoTimer.createStarted();
//        handoff.pass("grape", 500L);
//        outln("...passed grape", true);
//        outln("   seconds until passed", timer.getElapsedSeconds(), 200.0 / 1000.0, 0.080, 7);
//
//        receiver.stopRequest();
//        receiver.waitUntilDone(5000);
//    }
}
