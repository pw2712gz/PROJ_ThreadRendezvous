package com.abc.ds.tests;

import java.math.*;
import java.util.*;

import com.abc.ds.filter.*;
import com.abc.ds.tests.TestDSHelper.*;
import com.programix.testing.*;
import com.programix.thread.*;
import com.programix.thread.ix.*;
import com.programix.util.*;

public abstract class TestDSBase extends BaseTest {
    protected final TestAccess testAccess;

    protected TestDSBase(String title, ScoringInfo scoringInfo) {
        super(title, scoringInfo);

        testAccess = new TestDSHelper.TestAccess() {
            @Override
            public final void outln(String msg) {
                TestDSBase.this.outln(msg);
            }

            @Override
            public final void outlnErrorText(String msg) {
                TestDSBase.this.outlnErrorText(msg);
            }

            @Override
            public final void outln(String msg, boolean success) {
                TestDSBase.this.outln(msg, success);
            }

            @Override
            public final void outln(String msg,
                                    boolean result,
                                    boolean expectedResult) {
                TestDSBase.this.outln(msg, result, expectedResult);
            }

            @Override
            public final void outln(String msg,
                                    String result,
                                    String expectedResult) {
                TestDSBase.this.outln(msg, result, expectedResult);
            }

            @Override
            public final void outln(String msg,
                                    Object result,
                                    Object expectedResult) {
                TestDSBase.this.outln(msg, result, expectedResult);
            }

            @Override
            public final void outln(String msg,
                                    BigDecimal result,
                                    BigDecimal expectedResult) {
                TestDSBase.this.outln(msg, result, expectedResult);
            }

            @Override
            public final void outln(String msg, int result, int expectedResult) {
                TestDSBase.this.outln(msg, result, expectedResult);
            }

            @Override
            public final void outln(String msg,
                                    int result,
                                    int expectedResult,
                                    int tolerance) {
                TestDSBase.this.outln(msg, result, expectedResult, tolerance);
            }

            @Override
            public final void outln(String msg, long result, long expectedResult) {
                TestDSBase.this.outln(msg, result, expectedResult);
            }

            @Override
            public final void outln(String msg,
                                    long result,
                                    long expectedResult,
                                    long tolerance) {
                TestDSBase.this.outln(msg, result, expectedResult, tolerance);
            }

            @Override
            public void outln(String msg,
                              double result,
                              double expectedResult,
                              double tolerance,
                              int decimalPlaces) {

                TestDSBase.this.outln(msg, result, expectedResult, tolerance, decimalPlaces);
            }

            @Override
            public final void outln(String msg,
                                    Object[] result,
                                    Object[] expectedResult) {
                TestDSBase.this.outln(msg, result, expectedResult);
            }

            @Override
            public final void outln(String msg,
                                    byte[] result,
                                    byte[] expectedResult) {
                TestDSBase.this.outln(msg, result, expectedResult);
            }

            @Override
            public final void outln(String msg,
                                    int[] result,
                                    int[] expectedResult) {
                TestDSBase.this.outln(msg, result, expectedResult);
            }
        };
    }

    protected TestDSBase(String title) {
        this(title, ScoringInfo.ZERO_POINT_INSTANCE);
    }

    /*
    protected void checkZeroLenArray(String methodName, String[] results) {
        if ( results == null ) {
            outln(methodName + " returned null, expected a zero-length array", false);
        } else if ( results.length > 0 ) {
            outln(methodName + " returned an array with length=" + results.length +
                ", expected a zero-length array", false);
        } else {
            outln(methodName + " returned a zero-length array", true);
        }
    }

    protected void checkArrayMatch(String[] results,
                                   String[] expecteds) {

        if ( ObjectTools.isSameArray(results, expecteds) ) {
            String suffix = results.length > 100
                ? "[too many to list here]"
                : "[" + StringTools.formatCommaDelimited(results) + "]";
            outln("all " + results.length +
                " items match what is expected " + suffix, true);
        } else if ( results == null && expecteds != null ) {
            outln("result String[] is null but " +
                "expected String[] is not null and has length of " +
                expecteds.length, false);
        } else if ( results != null && expecteds == null ) {
            outln("result String[] is not null and has a length of " +
                results.length + ", but expected String[] is null", false);
        } else {
            outln("mismatch on Result items and Expected items in array:", false);
            outln("    number of items", results.length, expecteds.length);

            int lenOfLonger = Math.max(results.length, expecteds.length);
            boolean printMatchesToo = expecteds.length <= 100;
            outln(String.format(
                "    %5.5s %-20.20s %-20.20s", "Index", "Result", "Expected"));
            outln(String.format(
                "    %5.5s %-20.20s %-20.20s", "-----", "-----", "---------"));
            for ( int i = 0; i < lenOfLonger; i++ ) {
                boolean hasResult = i < results.length;
                boolean hasExpected = i < expecteds.length;
                boolean matches =
                    hasResult &&
                    hasExpected &&
                    ObjectTools.isSame(results[i], expecteds[i]);

                boolean printLine = printMatchesToo || !matches;
                if ( printLine ) {
                    String text = String.format("    %5d %-20.20s %-20.20s",
                        i,
                        hasResult ? StringTools.quoteWrap(results[i]) : "",
                        hasExpected ? StringTools.quoteWrap(expecteds[i]) : ""
                        );

                    if ( matches ) {
                        outln(text);
                    } else {
                        outlnErrorText(text);
                    }
                }
            }
        }
    }
     */

    protected String[] getFruits(int count) {
        return TestFruitGenerator.getFruits(count);
    }

    protected String[] getFruits(int offset, int count) {
        return TestFruitGenerator.getFruits(offset, count);
    }

    protected String formatCommaDelimited(String... items) {
        return StringTools.formatCommaDelimited(items);
    }

    protected String formatCommaDelimited(int... items) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for ( int item : items ) {
            if ( first ) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(item);
        }
        return sb.toString();
    }

    protected DSFilter<String> createStringLengthFilter(final int lengthToMatch) {
        return new DSFilter<>() {
            @Override
            public boolean matches(String item) {
                return item != null && item.length() == lengthToMatch;
            }

            @Override
            public String toString() {
                return "DSFilter[string length = " + lengthToMatch + "]";
            }
        };
    }

    public static class RunState {
        private boolean keepRunning;
        private String internalThreadNameWhenRegistered;
        private Thread internalThread;
        private boolean stillRunning;
        private final WaiterIx waiter;
        private final WaiterIx.Condition stillRunningCondition;
        private final WaiterIx.Condition internalThreadAvailableCondition;

        public RunState() {
            keepRunning = true;
            stillRunning = true;

            waiter = new WaiterIx(this);
            stillRunningCondition = waiter.createCondition(new WaiterIx.Expression() {
                @Override
                public boolean isTrue() {
                    return stillRunning;
                }
            });
            internalThreadAvailableCondition = waiter.createCondition(new WaiterIx.Expression() {
                @Override
                public boolean isTrue() {
                    return internalThread != null;
                }
            });
        }

        public synchronized boolean isKeepRunning() {
            return keepRunning;
        }

        public synchronized void registerCallerAsInternalThread() {
            internalThread = Thread.currentThread();
            internalThreadNameWhenRegistered = Thread.currentThread().getName();
            waiter.signalChange();
        }

        public synchronized String getInternalThreadNameWhenRegistered() {
            return internalThreadNameWhenRegistered;
        }

        public synchronized Thread getInternalThreadWaitingIfNecessary() throws InterruptedException {
            internalThreadAvailableCondition.waitUntilTrue();
            return internalThread;
        }

        public synchronized void stopRequest() {
            keepRunning = false;
            if (internalThread != null) {
                internalThread.interrupt();
            }
            waiter.signalChange();
        }

        public synchronized boolean isStillRunning() {
            return stillRunning;
        }

        public synchronized void setNoLongerRunning() {
            stillRunning = false;
            waiter.signalChange();
        }

        public synchronized boolean waitWhileStillRunning(long msTimeout)
                throws InterruptedException {

            return stillRunningCondition.waitWhileTrue(msTimeout);
        }

        public static String[] extractThreadNamesFor(RunState[] runStates) {
            if (runStates == null || runStates.length == 0) return StringTools.ZERO_LEN_ARRAY;

            List<String> names = new ArrayList<>();
            for (RunState runState : runStates) {
                names.add(runState.internalThreadNameWhenRegistered);
            }
            return names.toArray(StringTools.ZERO_LEN_ARRAY);
        }
    } // type RunState

    public static interface LauncherTask {
        void runTask() throws InterruptedException;
    } // LauncherTask

    protected class Launcher {
        private final TestThreadFactory threadFactory;
        private final Map<String, RunState> threadNameToRunStateLookup;
        private final BooleanState startingGun;
        private final Object lockObject;

        public Launcher(TestThreadFactory threadFactory) {
            lockObject = new Object();
            this.threadFactory = threadFactory;
            threadNameToRunStateLookup = new HashMap<>();
            startingGun = new BooleanState(false, lockObject);
        }

        public void addRunStateForThreadName(String threadName, RunState runState) {
            synchronized (lockObject) {
                threadNameToRunStateLookup.put(threadName, runState);
            }
        }

        public RunState runStateForThreadName(String threadName) {
            synchronized (lockObject) {
                return threadNameToRunStateLookup.get(threadName);
            }
        }

        public RunState[] getRunStateSnapshot() {
            synchronized (lockObject) {
                return threadNameToRunStateLookup.values().toArray(new RunState[0]);
            }
        }

        public String getStillRunningThreadNamesCommaDelimited() {
            synchronized (lockObject) {
                List<String> names = new ArrayList<>();
                for (RunState runState : getRunStateSnapshot()) {
                    if (runState.isStillRunning()) names.add(runState.internalThreadNameWhenRegistered);
                }
                return StringTools.formatCommaDelimited(StringTools.toArray(names));
            }
        }

        public void fireStartingGun() {
            startingGun.setState(true);
        }

        public RunState launch(String baseName, LauncherTask task) throws InterruptedException {
            BooleanState internalThreadPickedUp = new BooleanState(false);
            RunState runState = new RunState();
            threadFactory.createThreadFor(baseName, () -> {
                try {
                    addRunStateForThreadName(Thread.currentThread().getName(), runState);
                    runState.registerCallerAsInternalThread();
                    internalThreadPickedUp.waitUntilTrue();
                    startingGun.waitUntilTrue();
                    task.runTask();
                } catch (InterruptException x) {
                    // ignore and let thread die
                } catch (InterruptedException x) {
                    // ignore and let thread die
                } catch (Exception x) {
                    failureExceptionWithStackTrace(x);
                } finally {
                    outln(runState.getInternalThreadNameWhenRegistered() + " finished");
                    synchronized (lockObject) {
                        runState.setNoLongerRunning();
                        threadNameToRunStateLookup.remove(runState.getInternalThreadNameWhenRegistered());
                    }
                }
            });

            // wait for it to spin up and register itself
            runState.getInternalThreadWaitingIfNecessary();
            internalThreadPickedUp.setState(true);
            return runState;
        }

        public boolean waitForLaunchedTasksToComplete(long msTimeout) throws InterruptedException {
            long msEndTime = System.currentTimeMillis() + msTimeout;
            long msRemaining = msTimeout;
            RunState[] runStates = getRunStateSnapshot();
//            outln("runStates.length=" + runStates.length);
            boolean allHaveStopped = true;
            for (int i = 0; i < runStates.length && msRemaining > 0L; i++, msRemaining = msEndTime - System.currentTimeMillis()) {
                allHaveStopped = false;
                runStates[i].waitWhileStillRunning(msRemaining);
                if (i == runStates.length - 1) allHaveStopped = true;
            }
            return allHaveStopped;
        }

        public void shutdown() throws InterruptedException {
            RunState[] runStates = getRunStateSnapshot();
            for (RunState runState : runStates) {
                outln("sending stopRequest to " + runState.getInternalThreadNameWhenRegistered());
                runState.stopRequest();
            }
            long msTimeout = 3000L;
            for (RunState runState : runStates) {
                if (!runState.waitWhileStillRunning(msTimeout)) {
                    outln(runState.getInternalThreadNameWhenRegistered() + " failed to terminate within " + msTimeout + " ms of being interrupted");
                }
            }
        }
    }  // type Launcher

    protected void launchWithDeadline(long msUntilDeadline,
                                      TestThreadFactory threadFactory,
                                      Launchable launchable) throws InterruptedException {
        Launcher launcher = new Launcher(threadFactory);
        try {
            launchable.launch(launcher);

            if (!launcher.waitForLaunchedTasksToComplete(msUntilDeadline)) {
                outln("waited " + msUntilDeadline + " ms for all tasks to complete, and at least one did not [" +
                    launcher.getStillRunningThreadNamesCommaDelimited() + "]", false);
            }
        } finally {
            launcher.shutdown();
        }
    }

    protected static interface Launchable {
        void launch(Launcher launcher) throws InterruptedException;
    }  // type Launchable


}
