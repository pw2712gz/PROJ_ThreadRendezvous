package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffAdvInterruptedBeforeCallingPass extends TestPPStringHandoffBase {
    public TestPPStringHandoffAdvInterruptedBeforeCallingPass(StringHandoffFactory factory,
                                         TestThreadFactory threadFactory) {
        super("Advanced: interrupted before calling pass/receive", new BasicScoringInfo(1, 10), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            launch((launcher) -> testInterruptBeforePass(launcher));
            outln("====================");
            launch((launcher) -> testShutdownAndInterruptBeforePass(launcher));
            outln("====================");
            launch((launcher) -> testInterruptBeforeReceive(launcher));
            outln("====================");
            launch((launcher) -> testShutdownAndInterruptBeforeReceive(launcher));
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

    private void testInterruptBeforePass(Launcher launcher) throws InterruptedException {
        outln("-- passer waiting, second passer interrupted before calling pass --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "clementine";

        launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem)
                .setExpectedTime(700L, 200L)
                .create());
        });

        launcher.launch("PasserB", () -> {
            sleepAndLog(400L);
            outln("getting ready to call pass, but interrupting myself first...");
            Thread.currentThread().interrupt();
            outln("... back from Thread.currentThread().interrupt(), Thread.currentThread().isInterrupted() is " +
                Thread.currentThread().isInterrupted());
            pass(new PassConfig.Builder(sh, "tangerine")
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(800L);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectedItem(singleSuccessfulItem)
                .create());
        });
    }

    private void testShutdownAndInterruptBeforePass(Launcher launcher) throws InterruptedException {
        outln("-- shutdown, second passer interrupted before calling pass --");
        StringHandoff sh = createDS();

        launcher.launch("PasserA", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, "orange")
                .setExpectShutdownException(true)
                .create());
        });

        launcher.launch("Shutdowner", () -> {
            sleepAndLog(400L);
            outln("calling shutdown()...");
            sh.shutdown();
        });

        launcher.launch("PasserB", () -> {
            sleepAndLog(800L);
            outln("getting ready to call pass, but interrupting myself first...");
            Thread.currentThread().interrupt();
            outln("... back from Thread.currentThread().interrupt(), Thread.currentThread().isInterrupted() is " +
                Thread.currentThread().isInterrupted());
            pass(new PassConfig.Builder(sh, "mango")
                .setExpectInterruptException(true)
                .create());
        });
    }

    private void testInterruptBeforeReceive(Launcher launcher) throws InterruptedException {
        outln("-- receiver waiting, second receiver interrupted before calling receive --");
        StringHandoff sh = createDS();

        String singleSuccessfulItem = "clementine";

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectedItem(singleSuccessfulItem)
                .setExpectedTime(700L, 200L)
                .create());
        });

        launcher.launch("ReceiverB", () -> {
            sleepAndLog(400L);
            outln("getting ready to call receive, but interrupting myself first...");
            Thread.currentThread().interrupt();
            outln("... back from Thread.currentThread().interrupt(), Thread.currentThread().isInterrupted() is " +
                Thread.currentThread().isInterrupted());
            receive(new ReceiveConfig.Builder(sh)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("PasserA", () -> {
            sleepAndLog(800L);
            pass(new PassConfig.Builder(sh, singleSuccessfulItem)
                .create());
        });
    }

    private void testShutdownAndInterruptBeforeReceive(Launcher launcher) throws InterruptedException {
        outln("-- shutdown, second receiver interrupted before calling receive --");
        StringHandoff sh = createDS();

        launcher.launch("ReceiverA", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectShutdownException(true)
                .create());
        });

        launcher.launch("Shutdowner", () -> {
            sleepAndLog(400L);
            outln("calling shutdown()...");
            sh.shutdown();
        });

        launcher.launch("ReceiverB", () -> {
            sleepAndLog(800L);
            outln("getting ready to call receive, but interrupting myself first...");
            Thread.currentThread().interrupt();
            outln("... back from Thread.currentThread().interrupt(), Thread.currentThread().isInterrupted() is " +
                Thread.currentThread().isInterrupted());
            receive(new ReceiveConfig.Builder(sh)
                .setExpectInterruptException(true)
                .create());
        });
    }
}
