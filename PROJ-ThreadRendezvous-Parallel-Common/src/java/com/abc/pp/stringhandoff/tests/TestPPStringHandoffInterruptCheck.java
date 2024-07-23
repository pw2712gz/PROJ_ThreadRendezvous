package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffInterruptCheck extends TestPPStringHandoffBase {
    public TestPPStringHandoffInterruptCheck(StringHandoffFactory factory,
                                             TestThreadFactory threadFactory) {
        super("interrupt check", new BasicScoringInfo(10, 8), factory, threadFactory);
    }

    @Override
    protected void performTests() {
        try {
            outln("====================");
            launch((launcher) -> testInterruptWhilePassing(launcher));
            outln("====================");
            launch((launcher) -> testInterruptWhilePassingWithTimeout(launcher));
            outln("====================");
            launch((launcher) -> testInterruptWhileReceiving(launcher));
            outln("====================");
            launch((launcher) -> testInterruptWhileReceivingWithTimeout(launcher));
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

    private void testInterruptWhilePassing(Launcher launcher) throws InterruptedException {
        outln("-- interrupt while passing --");
        StringHandoff sh = createDS();

        RunState runStatePasser = launcher.launch("Passer", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, "raspberry")
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("Interruptor", () -> {
            sleepAndLog(500L);
            Thread passerThread = runStatePasser.getInternalThreadWaitingIfNecessary();
            outln("calling interrupt() on passer thread...");
            passerThread.interrupt();
        });
    }

    private void testInterruptWhilePassingWithTimeout(Launcher launcher) throws InterruptedException {
        outln("-- interrupt while passing with timeout --");
        StringHandoff sh = createDS();

        RunState runStatePasser = launcher.launch("Passer", () -> {
            sleepAndLog(100L);
            pass(new PassConfig.Builder(sh, "blackberry")
                .setTimeout(5_000L)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("Interruptor", () -> {
            sleepAndLog(1_000L);
            Thread passerThread = runStatePasser.getInternalThreadWaitingIfNecessary();
            outln("calling interrupt() on passer thread...");
            passerThread.interrupt();
        });
    }

    private void testInterruptWhileReceiving(Launcher launcher) throws InterruptedException {
        outln("-- interrupt while receiving --");
        StringHandoff sh = createDS();

        RunState runStateReceiver = launcher.launch("Receiver", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("Interruptor", () -> {
            sleepAndLog(500L);
            Thread receiverThread = runStateReceiver.getInternalThreadWaitingIfNecessary();
            outln("calling interrupt() on receiver thread...");
            receiverThread.interrupt();
        });
    }

    private void testInterruptWhileReceivingWithTimeout(Launcher launcher) throws InterruptedException {
        outln("-- interrupt while receiving with timeout --");
        StringHandoff sh = createDS();

        RunState runStateReceiver = launcher.launch("Receiver", () -> {
            sleepAndLog(100L);
            receive(new ReceiveConfig.Builder(sh)
                .setTimeout(2_000L)
                .setExpectInterruptException(true)
                .create());
        });

        launcher.launch("Interruptor", () -> {
            sleepAndLog(500L);
            Thread receiverThread = runStateReceiver.getInternalThreadWaitingIfNecessary();
            outln("calling interrupt() on receiver thread...");
            receiverThread.interrupt();
        });
    }
}
