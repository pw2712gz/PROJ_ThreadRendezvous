package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffAdvPassSuccessWithNegativeTimeout extends TestPPStringHandoffBase {
    public TestPPStringHandoffAdvPassSuccessWithNegativeTimeout(StringHandoffFactory factory,
                                         TestThreadFactory threadFactory) {
        super("Advanced: pass success with negative timeout", new BasicScoringInfo(1, 2), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            launch((launcher) -> testPassNegativeTimeout(launcher));
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

    private void testPassNegativeTimeout(Launcher launcher) throws InterruptedException {
        outln("-- receiver waiting, pass with negative timeout --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "pear";

        launcher.launch("Receiver", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectedItem(singleSuccessfulItem)
                .setExpectedTime(400L, 200L)
                .create());
        });

        launcher.launch("Passer", () -> {
            sleepAndLog(500L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem)
                .setTimeout(-111L)
                .create());
        });
    }
}
