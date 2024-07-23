package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffAdvNullHandoff extends TestPPStringHandoffBase {
    public TestPPStringHandoffAdvNullHandoff(StringHandoffFactory factory,
                                         TestThreadFactory threadFactory) {
        super("Advanced: pass null, receive null", new BasicScoringInfo(1, 4), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            launch((launcher) -> testPassNullNoTimeout(launcher));
            outln("====================");
            launch((launcher) -> testTimeoutClearsItem(launcher));
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

    private void testPassNullNoTimeout(Launcher launcher) throws InterruptedException {
        outln("-- pass null, receive null, no timeout --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = null;

        launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem)
                .setExpectedTime(1400L, 200L)
                .create());
        });

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(1500L);
            receive(new ReceiveConfig.Builder(sh).setExpectedItem(singleSuccessfulItem).create());
        });
    }

    private void testTimeoutClearsItem(Launcher launcher) throws InterruptedException {
        outln("-- two passers, 1st times out, 2nd passer succeeds in passing null --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = null;

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
}
