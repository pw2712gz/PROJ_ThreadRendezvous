package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;
import com.programix.thread.*;


public class TestPPStringHandoffShutdownWhilePassing extends TestPPStringHandoffBase {
    public TestPPStringHandoffShutdownWhilePassing(StringHandoffFactory factory,
                                                TestThreadFactory threadFactory) {
        super("shtudown() called while passing", new BasicScoringInfo(2, 14), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            launch((launcher) -> testShutdownCalledWhilePassing(launcher));
            outln("====================");
            launch((launcher) -> testShutdownCalledWhilePassingWithTimeout(launcher));
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

    private void testShutdownCalledWhilePassing(Launcher launcher) throws InterruptedException {
        outln("-- shutdown while passing, no timeout --");
        StringHandoff handoff = createDS();

        launcher.launch("Passer", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(handoff, "strawberry")
                .setExpectShutdownException(true)
                .create());
        });

        RunState runState = launcher.launch("Shutdowner", () -> {
            try {
                sleepAndLog(500L);
                outln("calling shutdown()...");
                NanoTimer timer = NanoTimer.createStarted();
                handoff.shutdown();
                timer.stop();
                outln("... back from shutdown()", true);
                outln("... seconds inside", timer.getElapsedSeconds(), 0.0, 0.100, 5);
            } catch (Exception x) {
                outln(String.format("... got UNEXPECTED Exception while trying to shutdown, x=%s", x), false);
                failureExceptionWithStackTrace(x);
            }
        });

        launcher.fireStartingGun();

        if (runState.waitWhileStillRunning(3_000L)) checkAfterShutdown(handoff, launcher);
    }

    private void testShutdownCalledWhilePassingWithTimeout(Launcher launcher) throws InterruptedException {
        outln("-- shutdown while passing with timeout --");
        StringHandoff handoff = createDS();

        launcher.launch("Passer", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(handoff, "strawberry")
                .setTimeout(5_000)
                .setExpectShutdownException(true)
                .create());
        });

        RunState runState = launcher.launch("Shutdowner", () -> {
            try {
                sleepAndLog(500L);
                outln("calling shutdown()...");
                NanoTimer timer = NanoTimer.createStarted();
                handoff.shutdown();
                timer.stop();
                outln("... back from shutdown()", true);
                outln("... seconds inside", timer.getElapsedSeconds(), 0.0, 0.100, 5);
            } catch (Exception x) {
                outln(String.format("... got UNEXPECTED Exception while trying to shutdown, x=%s", x), false);
                failureExceptionWithStackTrace(x);
            }
        });

        launcher.fireStartingGun();

        if (runState.waitWhileStillRunning(3_000L)) checkAfterShutdown(handoff, launcher);
    }

//    private void testShutdownCalledWhilePassing() throws InterruptedException {
//        StringHandoff handoff = createDS();
//
//        long msDelayBeforeShutdown = 1000L;
//        ShutdownHelper shutdownHelper = new ShutdownHelper(handoff, 1000L);
//        NanoTimer timer = NanoTimer.createStopped();
//        try {
//            outln("Calling pass(\"watermelon\")");
//            timer.resetAndStart();
//            handoff.pass("watermelon");
//            timer.stop();
//            outln("Just passed, but should have thrown ShutdownException", false);
//        } catch ( ShutdownException x ) {
//            outln("Got exception: " + x, true);
//            outln("   seconds until exception", timer.getElapsedSeconds(), msDelayBeforeShutdown / 1000.0, 0.200, 7);
//        }
//
//        checkAfterShutdown(handoff);
//
//        shutdownHelper.stopRequest();
//        shutdownHelper.waitUntilDone(5000);
//    }
}
