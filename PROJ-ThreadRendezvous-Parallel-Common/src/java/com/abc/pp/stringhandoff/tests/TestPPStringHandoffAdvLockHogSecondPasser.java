package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffAdvLockHogSecondPasser extends TestPPStringHandoffBase {
    public TestPPStringHandoffAdvLockHogSecondPasser(StringHandoffFactory factory,
                                         TestThreadFactory threadFactory) {
        super("Advanced: lock hog receive/pass", new BasicScoringInfo(1, 3), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            launch((launcher) -> testLockHog(launcher));
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

    private void testLockHog(Launcher launcher) throws InterruptedException {
        outln("-- passer waiting, lock hog receives and then calls pass, expect IllegalStateException --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "lime";

        launcher.launch("Passer", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem)
                .setExpectedTime(400L, 100L)
                .create());
        });

        launcher.launch("LockHog", () -> {
            sleepAndLog(500L);
            synchronized (sh.getLockObject()) {
                outln("holding lock in synchronized block...");
                receive(new ReceiveConfig.Builder(sh)
                    .setExpectedItem(singleSuccessfulItem)
                    .create());
                outln("STILL holding lock, Passer can't yet see that handoff is complete");
                outln("Now going to try to pass while holding the lock, but expect exception...");
                pass(new PassConfig.Builder(sh, "lemon")
                    .setExpectIllegalStateException(true)
                    .create());
                outln("...finally letting go of the lock");
            }
        });
    }
}
