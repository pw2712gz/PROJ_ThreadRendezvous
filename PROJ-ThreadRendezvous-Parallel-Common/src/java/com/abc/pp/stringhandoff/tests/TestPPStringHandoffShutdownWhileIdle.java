package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;
import com.programix.thread.*;


public class TestPPStringHandoffShutdownWhileIdle extends TestPPStringHandoffBase {
    public TestPPStringHandoffShutdownWhileIdle(StringHandoffFactory factory,
                                                TestThreadFactory threadFactory) {
        super("shtudown() called while idle", new BasicScoringInfo(2, 6), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            launch((launcher) -> testShutdownCalledWhileIdle(launcher));
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

    private void testShutdownCalledWhileIdle(Launcher launcher) throws InterruptedException {
        StringHandoff handoff = createDS();
        outln("-- shutdown while no threads are inside --");


        RunState runState = launcher.launch("Shutdowner", () -> {
            try {
                sleepAndLog(50L);
                outln("calling shutdown()...");
                NanoTimer timer = NanoTimer.createStarted();
                handoff.shutdown();
                timer.stop();
                outln("... back from shutdown()", true);
                outln("... seconds inside", timer.getElapsedSeconds(), 0.0, 0.050, 5);
            } catch (Exception x) {
                outln(String.format("... got UNEXPECTED Exception while trying to shutdown, x=%s", x), false);
                failureExceptionWithStackTrace(x);
            }
        });

        launcher.fireStartingGun();

        if (runState.waitWhileStillRunning(3_000L)) checkAfterShutdown(handoff, launcher);
    }
}
