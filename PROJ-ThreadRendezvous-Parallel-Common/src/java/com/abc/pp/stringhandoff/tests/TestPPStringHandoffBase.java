package com.abc.pp.stringhandoff.tests;

import java.util.*;

import com.abc.ds.tests.*;
import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;
import com.programix.thread.*;
import com.programix.util.*;

/* deliberate package access */
abstract class TestPPStringHandoffBase extends TestDSBase {

    protected static final long RECEIVE_INTERVAL = 600L;
    protected static final long PASS_INTERVAL = RECEIVE_INTERVAL;
    protected static final long STANDARD_TOLERANCE = 100L;

    protected static final String[] SAMPLE_DATA = {
        "apple", "banana", "cranberry", "date", "eggplant", "fig"
    };

    protected final StringHandoffFactory factory;
    protected final TestDSHelper<String> testHelper;
    protected final TestThreadFactory threadFactory;

    protected TestPPStringHandoffBase(String titleSuffix,
                                      ScoringInfo scoringInfo,
                                      StringHandoffFactory factory,
                                      TestThreadFactory threadFactory) {

        //super("StringHandoff - " + titleSuffix, scoringInfo);
        super(titleSuffix, scoringInfo);
        this.factory = factory;
        this.threadFactory = threadFactory;
        testHelper = new TestDSHelper.Builder<String>()
            .setItemType(String.class)
            .setTestAccess(testAccess)
            .setAllowDuplicates(true)
            .setOrderMatters(true)
            .setWrapItemsInQuotes(true)
            .create();
    }

    protected TestPPStringHandoffBase(String titleSuffix,
                                      ScoringInfo scoringInfo,
                                      StringHandoffFactory factory) {
        this(titleSuffix, scoringInfo, factory, null);
    }

    protected TestPPStringHandoffBase(String titleSuffix,
                                      StringHandoffFactory factory,
                                      TestThreadFactory threadFactory) {
        this(titleSuffix, ScoringInfo.ZERO_POINT_INSTANCE, factory, threadFactory);
    }

    protected TestPPStringHandoffBase(String titleSuffix,
                                      StringHandoffFactory factory) {
        this(titleSuffix, ScoringInfo.ZERO_POINT_INSTANCE, factory, null);
    }


    protected StringHandoff createDS() {
        outln("Creating a new StringHandoff instance ...");
        StringHandoff sh = factory.create();
        outln("   ...created: " + sh.getClass().getCanonicalName());
        return sh;
    }

    protected void outlnPass(String item, long msTimeout) {
        outln(String.format("calling pass(%s, %d)...", StringTools.quoteWrap(item), msTimeout));
    }

    protected void outlnPass(String item) {
        outln(String.format("calling pass(%s)...", StringTools.quoteWrap(item)));
    }

    protected void outlnReceive(long msTimeout) {
        outln(String.format("calling receive(%d)...", msTimeout));
    }

    protected void outlnReceive() {
        outln("calling receive()...");
    }

    protected static class PassConfig {
        public final StringHandoff handoff;
        public final String item;
        public final boolean useTimeout;
        public final long msTimeout;
        public final boolean checkTiming;
        public final long msExpected;
        public final long msTolerance;
        public final boolean expectInterruptException;
        public final boolean expectShutdownException;
        public final boolean expectTimedOutException;
        public final boolean expectIllegalStateException;

        private PassConfig(Builder builder) {
            handoff = builder.handoff;
            item = builder.item;
            useTimeout = builder.useTimeout;
            msTimeout = builder.msTimeout;
            checkTiming = builder.checkTiming;
            msExpected = builder.msExpected;
            msTolerance = builder.msTolerance;
            expectInterruptException = builder.expectInterruptException;
            expectShutdownException = builder.expectShutdownException;
            expectTimedOutException = builder.expectTimedOutException;
            expectIllegalStateException = builder.expectIllegalStateException;
        }

        public static class Builder {
            private StringHandoff handoff;
            private boolean itemWasSet;
            private String item;
            private boolean useTimeout;
            private long msTimeout;
            private boolean checkTiming;
            private long msExpected;
            private long msTolerance;
            private boolean expectInterruptException;
            private boolean expectShutdownException;
            private boolean expectTimedOutException;
            private boolean expectIllegalStateException;

            public Builder(StringHandoff handoff, String item) {
                this.handoff = handoff;
                this.item = item;
                this.itemWasSet = true;
            }

            public Builder() {
            }

            public PassConfig create() throws IllegalArgumentException {
                ObjectTools.paramNullCheck(handoff, "handoff");
                if (!itemWasSet) throw new IllegalArgumentException("item must be set (can be null, but must be specified to be null");

                return new PassConfig(this);
            }

            public Builder setHandoff(StringHandoff handoff) {
                this.handoff = handoff;
                return this;
            }

            public Builder setItem(String item) {
                this.item = item;
                itemWasSet = true;
                return this;
            }

            public Builder setTimeout(long msTimeout) {
                this.msTimeout = msTimeout;
                useTimeout = true;
                return this;
            }

            public Builder setExpectedTime(long msExpected, long msTolerance) {
                this.msExpected = msExpected;
                this.msTolerance = msTolerance;
                checkTiming = true;
                return this;
            }

            public Builder setExpectInterruptException(boolean expectInterruptException) {
                this.expectInterruptException = expectInterruptException;
                return this;
            }

            public Builder setExpectShutdownException(boolean expectShutdownException) {
                this.expectShutdownException = expectShutdownException;
                return this;
            }

            public Builder setExpectTimedOutException(boolean expectTimedOutException) {
                this.expectTimedOutException = expectTimedOutException;
                return this;
            }

            public Builder setExpectIllegalStateException(boolean expectIllegalStateException) {
                this.expectIllegalStateException = expectIllegalStateException;
                return this;
            }
        }  // type Builder
    }  // type PassConfig

    protected void pass(PassConfig conf) throws InterruptedException {
        if (conf.useTimeout) outlnPass(conf.item, conf.msTimeout); else outlnPass(conf.item);
        String itemQuoteWrapped = StringTools.quoteWrap(conf.item);
        NanoTimer timer = NanoTimer.createStarted();
        long msElapsed = -1;
        try {
            try {
                if (conf.useTimeout) conf.handoff.pass(conf.item, conf.msTimeout); else conf.handoff.pass(conf.item);
            } finally {
                timer.stop();
                msElapsed = (long)timer.getElapsedMilliseconds();
            }

            if (conf.expectInterruptException ||
                conf.expectShutdownException ||
                conf.expectTimedOutException ||
                conf.expectIllegalStateException) {

                String exceptionName = conf.expectInterruptException ? "InterruptedException" :
                    (conf.expectShutdownException ? "ShutdownException" :
                        (conf.expectTimedOutException ? "TimedOutException" : "IllegalStateException"));
                outln(String.format("... expected %s, but did NOT get it while trying to pass %s, after waiting %d ms",
                    exceptionName, itemQuoteWrapped, msElapsed), false);
            } else {
                String msgPrefix = String.format("... passed: %s", itemQuoteWrapped);
                if (conf.checkTiming) {
                    outln(String.format("%s, ms inside pass:", msgPrefix), msElapsed, conf.msExpected, conf.msTolerance);
                } else {
                    outln(String.format("%s, %d ms inside pass", msgPrefix, msElapsed));
                }
            }
        } catch (InterruptedException x) {
            if (conf.expectInterruptException) {
                outln(String.format("... as expected, got InterruptedException while trying to pass %s, after waiting %d ms, x=%s",
                    itemQuoteWrapped, msElapsed, x), true);
            }
            throw x; // re-throw it to unwind the thread
        } catch (ShutdownException x) {
            if (conf.expectShutdownException) {
                outln(String.format("... as expected, got ShutdownException while trying to pass %s, after waiting %d ms, x=%s",
                    itemQuoteWrapped, msElapsed, x), true);
            } else {
                outln(String.format("... got UNEXPECTED ShutdownException while trying to pass %s, x=%s",
                    itemQuoteWrapped, x), false);
                failureExceptionWithStackTrace(x);
            }
        } catch (TimedOutException x) {
            if (conf.expectTimedOutException) {
                outln(String.format("... as expected, got TimedOutException while trying to pass %s, after waiting %d ms, x=%s",
                    itemQuoteWrapped, msElapsed, x), true);
                if (conf.checkTiming) {
                    outln(String.format("... ms inside pass(%s, %d)", itemQuoteWrapped,
                        conf.msTimeout), msElapsed, conf.msExpected, conf.msTolerance);
                }
            } else {
                outln(String.format("... got UNEXPECTED TimedOutException while trying to pass %s, x=%s",
                    itemQuoteWrapped, x), false);
                failureExceptionWithStackTrace(x);
            }
        } catch (IllegalStateException x) {
            if (conf.expectIllegalStateException) {
                outln(String.format("... as expected, got IllegalStateException while trying to pass %s, after waiting %d ms, x=%s",
                    itemQuoteWrapped, msElapsed, x), true);
            } else {
                outln(String.format("... got UNEXPECTED IllegalStateException while trying to pass %s, x=%s",
                    itemQuoteWrapped, x), false);
                failureExceptionWithStackTrace(x);
            }
        } catch (Exception x) {
            outln(String.format("... got UNEXPECTED Exception while trying to pass %s, x=%s",
                itemQuoteWrapped, x), false);
            failureExceptionWithStackTrace(x);
        }
    }

    protected static class ReceiveConfig {
        public final StringHandoff handoff;
        public final boolean useTimeout;
        public final long msTimeout;
        public final boolean checkItem;
        public final String expectedItem;
        public final boolean checkTiming;
        public final long msExpected;
        public final long msTolerance;
        public final boolean expectInterruptException;
        public final boolean expectShutdownException;
        public final boolean expectTimedOutException;
        public final boolean expectIllegalStateException;

        private ReceiveConfig(Builder builder) {
            handoff = builder.handoff;
            useTimeout = builder.useTimeout;
            msTimeout = builder.msTimeout;
            checkItem = builder.checkItem;
            expectedItem = builder.expectedItem;
            checkTiming = builder.checkTiming;
            msExpected = builder.msExpected;
            msTolerance = builder.msTolerance;
            expectInterruptException = builder.expectInterruptException;
            expectShutdownException = builder.expectShutdownException;
            expectTimedOutException = builder.expectTimedOutException;
            expectIllegalStateException = builder.expectIllegalStateException;
        }

        public static class Builder {
            private StringHandoff handoff;
            private boolean useTimeout;
            private long msTimeout;
            private boolean checkItem;
            private String expectedItem;
            private boolean checkTiming;
            private long msExpected;
            private long msTolerance;
            private boolean expectInterruptException;
            private boolean expectShutdownException;
            private boolean expectTimedOutException;
            private boolean expectIllegalStateException;

            public Builder(StringHandoff handoff) {
                this.handoff = handoff;
            }

            public Builder() {
            }

            public ReceiveConfig create() throws IllegalArgumentException {
                ObjectTools.paramNullCheck(handoff, "handoff");

                return new ReceiveConfig(this);
            }

            public Builder setHandoff(StringHandoff handoff) {
                this.handoff = handoff;
                return this;
            }

            public Builder setTimeout(long msTimeout) {
                this.msTimeout = msTimeout;
                useTimeout = true;
                return this;
            }

            public Builder setExpectedItem(String expectedItem) {
                this.expectedItem = expectedItem;
                checkItem = true;
                return this;
            }

            public Builder setExpectedTime(long msExpected, long msTolerance) {
                this.msExpected = msExpected;
                this.msTolerance = msTolerance;
                checkTiming = true;
                return this;
            }

            public Builder setExpectInterruptException(boolean expectInterruptException) {
                this.expectInterruptException = expectInterruptException;
                return this;
            }

            public Builder setExpectShutdownException(boolean expectShutdownException) {
                this.expectShutdownException = expectShutdownException;
                return this;
            }

            public Builder setExpectTimedOutException(boolean expectTimedOutException) {
                this.expectTimedOutException = expectTimedOutException;
                return this;
            }

            public Builder setExpectIllegalStateException(boolean expectIllegalStateException) {
                this.expectIllegalStateException = expectIllegalStateException;
                return this;
            }
        }  // type Builder
    }  // type ReceiveConfig

    protected void receive(ReceiveConfig conf) throws InterruptedException {
        if (conf.useTimeout) outlnReceive(conf.msTimeout); else outlnReceive();
        NanoTimer timer = NanoTimer.createStarted();
        long msElapsed = -1;
        String item = null;
        String itemQuoteWrapped = null;
        try {
            try {
                item = conf.useTimeout ? conf.handoff.receive(conf.msTimeout) : conf.handoff.receive();
                itemQuoteWrapped = StringTools.quoteWrap(item);
            } finally {
                timer.stop();
                msElapsed = (long)timer.getElapsedMilliseconds();
            }

            if (conf.expectInterruptException ||
                conf.expectShutdownException ||
                conf.expectTimedOutException ||
                conf.expectIllegalStateException) {

                String exceptionName = conf.expectInterruptException ? "InterruptedException" :
                    (conf.expectShutdownException ? "ShutdownException" :
                        (conf.expectTimedOutException ? "TimedOutException" : "IllegalStateException"));
                outln(String.format("... expected %s, but did NOT get it while trying to receive%s, after waiting %d ms",
                    exceptionName,
                    itemQuoteWrapped != null ? String.format(" (got %s back instead)", itemQuoteWrapped) : "",
                        msElapsed), false);
            } else {
                if (conf.checkItem) {
                    if (conf.checkTiming) {
                        outln("... received:", item, conf.expectedItem);
                        outln("... ms inside receive:", msElapsed, conf.msExpected, conf.msTolerance);
                    } else {
                        outln(String.format("... %d ms to receive:", msElapsed), item, conf.expectedItem);
                    }
                } else {
                    if (conf.checkTiming) {
                        outln(String.format("... received %s, ms inside receive:", item), msElapsed, conf.msExpected, conf.msTolerance);
                    } else {
                        outln(String.format("... received %s, %d ms inside receive:", item, msElapsed));
                    }
                }
            }
        } catch (InterruptedException x) {
            if (conf.expectInterruptException) {
                outln(String.format("... as expected, got InterruptedException while trying to receive, after waiting %d ms, x=%s",
                    msElapsed, x), true);
            }
            throw x; // re-throw it to unwind the thread
        } catch (ShutdownException x) {
            if (conf.expectShutdownException) {
                outln(String.format("... as expected, got ShutdownException while trying to receive, after waiting %d ms, x=%s",
                    msElapsed, x), true);
            } else {
                outln(String.format("... got UNEXPECTED ShutdownException while trying to receive, x=%s", x), false);
                failureExceptionWithStackTrace(x);
            }
        } catch (TimedOutException x) {
            if (conf.expectTimedOutException) {
                outln(String.format("... as expected, got TimedOutException while trying to receive, after waiting %d ms, x=%s",
                    msElapsed, x), true);
                if (conf.checkTiming) {
                    outln(String.format("... ms inside receive(%d)", conf.msTimeout), msElapsed, conf.msExpected, conf.msTolerance);
                }
            } else {
                outln(String.format("... got UNEXPECTED TimedOutException while trying to receive, x=%s", x), false);
                failureExceptionWithStackTrace(x);
            }
        } catch (IllegalStateException x) {
            if (conf.expectIllegalStateException) {
                outln(String.format("... as expected, got IllegalStateException while trying to receive, after waiting %d ms, x=%s",
                    msElapsed, x), true);
            } else {
                outln(String.format("... got UNEXPECTED IllegalStateException while trying to receive, x=%s", x), false);
                failureExceptionWithStackTrace(x);
            }
        } catch (Exception x) {
            outln(String.format("... got UNEXPECTED Exception while trying to receive, x=%s", x), false);
            failureExceptionWithStackTrace(x);
        }
    }


    /*
    private void passAndLogInternal(StringHandoff sh, String item,
                                    boolean useTimeout, long msTimeout,
                                    boolean checkTiming, long msExpected, long msTolerance)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        if (useTimeout) outlnPass(item, msTimeout); else outlnPass(item);
        NanoTimer timer = NanoTimer.createStarted();
        if (useTimeout) sh.pass(item, msTimeout); else sh.pass(item);
        timer.stop();
        long msElapsed = (long)timer.getElapsedMilliseconds();
        String msgPrefix = String.format("...passed: %s", StringTools.quoteWrap(item));
        if (checkTiming) {
            outln(String.format("%s, ms inside pass:", msgPrefix), msElapsed, msExpected, msTolerance);
        } else {
            outln(String.format("%s, %d ms inside pass", msgPrefix, msElapsed));
        }
    }

    protected void passAndLog(StringHandoff sh, String item, long msTimeout,
                              long msExpectedToPass, long msTolerance)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        passAndLogInternal(sh, item, true, msTimeout, true, msExpectedToPass, msTolerance);
    }

    protected void passAndLog(StringHandoff sh, String item, long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        passAndLogInternal(sh, item, true, msTimeout, false, 0, 0);
    }

    protected void passAndLog(StringHandoff sh, String item, long msExpectedToPass, long msTolerance)
        throws InterruptedException, ShutdownException, IllegalStateException {

        passAndLogInternal(sh, item, false, 0, true, msExpectedToPass, msTolerance);
    }

    protected void passAndLog(StringHandoff sh, String item)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        passAndLogInternal(sh, item, false, 0, false, 0, 0);
    }

    protected void outlnReceive(long msTimeout) {
        outln(String.format("calling receive(%d)...", msTimeout));
    }

    protected void outlnReceive() {
        outln(String.format("calling receive()..."));
    }

    private void receiveAndLogInternal(StringHandoff sh,
                                       boolean useTimeout, long msTimeout,
                                       boolean checkItem, String expectedItem,
                                       boolean checkTiming, long msExpected, long msTolerance)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        if (useTimeout) outlnReceive(msTimeout); else outlnReceive();
        NanoTimer timer = NanoTimer.createStarted();
        String item = useTimeout ? sh.receive(msTimeout) : sh.receive();
        timer.stop();
        long msElapsed = (long)timer.getElapsedMilliseconds();
        if (checkItem) {
            if (checkTiming) {
                outln("... received:", item, expectedItem);
                outln("... ms inside receive:", msElapsed, msExpected, msTolerance);
            } else {
                outln(String.format("... %d ms to receive:", msElapsed), item, expectedItem);
            }
        } else {
            if (checkTiming) {
                outln(String.format("... received %s, ms inside receive:", item), msElapsed, msExpected, msTolerance);
            } else {
                outln(String.format("... received %s, %d ms inside receive:", item, msElapsed));
            }
        }
    }

    protected void receiveAndLog(StringHandoff sh, long msTimeout, String expectedItem, long msExpected, long msTolerance)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        receiveAndLogInternal(sh, true, msTimeout, true, expectedItem, true, msExpected, msTolerance);
    }

    protected void receiveAndLog(StringHandoff sh, long msTimeout, String expectedItem)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        receiveAndLogInternal(sh, true, msTimeout, true, expectedItem, false, 0, 0);
    }

    protected void receiveAndLog(StringHandoff sh, long msTimeout, long msExpected, long msTolerance)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        receiveAndLogInternal(sh, true, msTimeout, false, null, true, msExpected, msTolerance);
    }

    protected void receiveAndLog(StringHandoff sh, long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        receiveAndLogInternal(sh, true, msTimeout, false, null, false, 0, 0);
    }

    protected void receiveAndLog(StringHandoff sh, String expectedItem, long msExpected, long msTolerance)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        receiveAndLogInternal(sh, false, 0, true, expectedItem, true, msExpected, msTolerance);
    }

    protected void receiveAndLog(StringHandoff sh, String expectedItem)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        receiveAndLogInternal(sh, false, 0, true, expectedItem, false, 0, 0);
    }

    protected void receiveAndLog(StringHandoff sh, long msExpected, long msTolerance)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        receiveAndLogInternal(sh, false, 0, false, null, true, msExpected, msTolerance);
    }

    protected void receiveAndLog(StringHandoff sh)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        receiveAndLogInternal(sh, false, 0, false, null, false, 0, 0);
    }
     */

    protected TestWackyWaiter kickoffWackyWaiter(Object lockObject) throws IllegalStateException {
        if (threadFactory == null) {
            throw new IllegalStateException("can't use WackyWaiter, no TestThreadFactory supplied at construction");
        }
        return new TestWackyWaiter(lockObject, threadFactory, testAccess);
    }

    protected TestNastyNotifier kickoffNastyNotifier(Object lockObject) throws IllegalStateException {
        if (threadFactory == null) {
            throw new IllegalStateException("can't use NastyNotifier, no TestThreadFactory supplied at construction");
        }
        return new TestNastyNotifier(lockObject, threadFactory, testAccess);
    }

    protected void checkAfterShutdown(StringHandoff handoff, Launcher launcher) throws InterruptedException {
        outln("Checking that after shutdown, all methods throw ShutdownException immediately");

        launcher.launch("PasserW", () -> {
            sleepAndLog(200);
            pass(new PassConfig.Builder(handoff, "apple")
                .setExpectShutdownException(true)
                .setExpectedTime(0, 75)
                .create());
        });

        launcher.launch("PasserX", () -> {
            sleepAndLog(300);
            pass(new PassConfig.Builder(handoff, "banana")
                .setTimeout(500L)
                .setExpectShutdownException(true)
                .setExpectedTime(0, 75)
                .create());
        });

        launcher.launch("ReceiverY", () -> {
            sleepAndLog(400);
            receive(new ReceiveConfig.Builder(handoff)
                .setExpectShutdownException(true)
                .setExpectedTime(0, 75)
                .create());
        });

        launcher.launch("ReceiverZ", () -> {
            sleepAndLog(500);
            receive(new ReceiveConfig.Builder(handoff)
                .setTimeout(500L)
                .setExpectShutdownException(true)
                .setExpectedTime(0, 75)
                .create());
        });
    }

    protected void checkAfterShutdown(StringHandoff handoff) throws InterruptedException {

        outln("Checking that after shutdown, all methods throw ShutdownException immediately");


        NanoTimer timer = NanoTimer.createStopped();
        try {
            outln("Trying handoff.receive() ...");
            timer.resetAndStart();
            handoff.receive();
            outln("No ShutdownException", false);
        } catch ( ShutdownException x ) {
            outln("ShutdownException thrown", true);
            outln("   seconds until exception", timer.getElapsedSeconds(), 0.0, 0.080, 7);
        }

        try {
            outln("Trying handoff.receive(500) ...");
            timer.resetAndStart();
            handoff.receive(500L);
            outln("No ShutdownException", false);
        } catch ( ShutdownException x ) {
            outln("ShutdownException thrown", true);
            outln("   seconds until exception", timer.getElapsedSeconds(), 0.0, 0.080, 7);
        }

        try {
            outln("Trying handoff.pass(\"strawberry\") ...");
            timer.resetAndStart();
            handoff.pass("strawberry");
            outln("No ShutdownException", false);
        } catch ( ShutdownException x ) {
            outln("ShutdownException thrown", true);
            outln("   seconds until exception", timer.getElapsedSeconds(), 0.0, 0.080, 7);
        }

        try {
            outln("Trying handoff.pass(\"strawberry\", 500) ...");
            timer.resetAndStart();
            handoff.pass("strawberry", 500L);
            outln("No ShutdownException", false);
        } catch ( ShutdownException x ) {
            outln("ShutdownException thrown", true);
            outln("   seconds until exception", timer.getElapsedSeconds(), 0.0, 0.080, 7);
        }
    }

    protected class ShutdownHelper {
        private final StringHandoff ds;
        private final long msDelayBeforeShutdown;
        private final TestDSBase.RunState runState;

        public ShutdownHelper(StringHandoff ds,
                              long msDelayBeforeShutdown) {

            this.ds = ds;
            this.msDelayBeforeShutdown = msDelayBeforeShutdown;

            runState = new TestDSBase.RunState();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    runWork();
                }
            };

            threadFactory.createThreadFor(r, "ShutdownHelper");
        }

        private void runWork() {
            runState.registerCallerAsInternalThread();
            try {
                testAccess.outln(
                    String.format(
                        "waiting %.5f seconds before calling shutdown()", msDelayBeforeShutdown / 1000.0));

                if (msDelayBeforeShutdown > 0L) {
                    Thread.sleep(msDelayBeforeShutdown);
                }

                if (runState.isKeepRunning()) {
                    testAccess.outln("Attempting to call shutdown()...");
                    ds.shutdown();
                }
            } catch ( InterruptedException x ) {
                // ignore and die
            } finally {
                testAccess.outln("ShutdownHelper finished");
                runState.setNoLongerRunning();
            }
        }

        public void stopRequest() {
            runState.stopRequest();
        }

        public boolean waitUntilDone(long msTimeout) throws InterruptedException {
            return runState.waitWhileStillRunning(msTimeout);
        }
    } // type ShutdownHelper

    protected class Passer {
        private final StringHandoff ds;
        private final long msBetweenPassAttempts;
        private final long delayBeforeFirstPass;
        private final TestDSBase.RunState runState;
        private final String[] itemsToPass;

        public Passer(StringHandoff ds,
                      long msBetweenPassAttempts,
                      long delayBeforeFirstPass,
                      String... itemsToPass) {

            this.ds = ds;
            this.delayBeforeFirstPass = delayBeforeFirstPass;
            this.msBetweenPassAttempts = msBetweenPassAttempts;
            this.itemsToPass = itemsToPass;

            runState = new TestDSBase.RunState();
            threadFactory.createThreadFor(() -> runWork(), "Passer");
        }

        public Passer(StringHandoff ds,
                      long msBetweenPassAttempts,
                      long delayBeforeFirstPass) {

            this(ds, msBetweenPassAttempts, delayBeforeFirstPass, SAMPLE_DATA);
        }

        public Passer(StringHandoff ds,
                      long msBetweenPassAttempts) {

            this(ds, msBetweenPassAttempts, 0L);
        }

        private void runWork() {
            runState.registerCallerAsInternalThread();
            try {
                testAccess.outln("Passer starting");
                if (delayBeforeFirstPass > 0) {
                    testAccess.outln("waiting " + delayBeforeFirstPass +
                        " ms before first attempt...");
                    Thread.sleep(delayBeforeFirstPass);
                }
                NanoTimer timer = NanoTimer.createStopped();
                for ( int count = 0; runState.isKeepRunning(); count++ ) {
                    String item = itemsToPass[count % itemsToPass.length];
                    outln("Passer attempting to pass '" + item + "' ...");
                    timer.resetAndStart();
                    ds.pass(item);
                    timer.stop();
                    testAccess.outln(String.format(
                        "passed '%s' after blocking for %.5f seconds",
                        item, timer.getElapsedSeconds()));
                    Thread.sleep(msBetweenPassAttempts);
                }
            } catch ( InterruptedException x ) {
                // ignore and die
            } finally {
                testAccess.outln("Passer finished");
                runState.setNoLongerRunning();
            }
        }

        public void stopRequest() {
            runState.stopRequest();
        }

        public boolean waitUntilDone(long msTimeout) throws InterruptedException {
            return runState.waitWhileStillRunning(msTimeout);
        }
    } // type Passer


    protected class Receiver {
        private final StringHandoff ds;
        private final long delayBeforeFirstRemove;
        private final long msBetweenRemoveAttempts;
        private final ExpectedRemoveList expectedRemoveList;
        private final TestDSBase.RunState runState;

        public Receiver(StringHandoff ds,
                        long msBetweenRemoveAttempts,
                        long delayBeforeFirstRemove) {

            this.ds = ds;
            this.msBetweenRemoveAttempts = msBetweenRemoveAttempts;
            this.delayBeforeFirstRemove = delayBeforeFirstRemove;

            expectedRemoveList = new ExpectedRemoveList();

            runState = new TestDSBase.RunState();
            threadFactory.createThreadFor(() -> runWork(), "Receiver");
        }

        private void runWork() {
            runState.registerCallerAsInternalThread();
            try {
                testAccess.outln("Receiver starting");
                if (delayBeforeFirstRemove > 0) {
                    testAccess.outln("waiting " + delayBeforeFirstRemove +
                        " ms before first attempt...");
                    Thread.sleep(delayBeforeFirstRemove);
                }
                NanoTimer timer = NanoTimer.createStopped();
                while (runState.isKeepRunning()) {
                    timer.resetAndStart();
                    String item = ds.receive();
                    timer.stop();

                    String expectedItem = expectedRemoveList.removeOrNull();
                    if (expectedItem == null) {
                        testAccess.outln(String.format(
                            "received '%s' after blocking for %.5f seconds",
                            item, timer.getElapsedSeconds()));
                    } else {
                        testAccess.outln(String.format(
                            "received '%s' [expected '%s'] after blocking for %.5f seconds",
                            item, expectedItem, timer.getElapsedSeconds()),
                            StringTools.isSame(item, expectedItem));
                    }

                    Thread.sleep(msBetweenRemoveAttempts);
                }
            } catch ( InterruptedException x ) {
                // ignore and die
            } finally {
                testAccess.outln("Receiver finished");
                runState.setNoLongerRunning();
            }
        }

        public void stopRequest() {
            runState.stopRequest();
        }

        public boolean waitUntilDone(long msTimeout) throws InterruptedException {
            return runState.waitWhileStillRunning(msTimeout);
        }

        public void addExpectedItemToBeRemoved(String item) {
            expectedRemoveList.add(item);
        }

        public void addExpectedItemsToBeRemoved(String... items) {
            if (items == null || items.length == 0) return;
            for ( String item : items ) {
                addExpectedItemToBeRemoved(item);
            }
        }

        private class ExpectedRemoveList {
            private List<String> list;

            public ExpectedRemoveList() {
                list = new LinkedList<>();
            }

            public synchronized void add(String item) {
                list.add(item);
            }

            public synchronized String removeOrNull() {
                return list.isEmpty() ? null : list.remove(0);
            }
        } // type ExpectedRemoveList
    } // type Receiver
}
