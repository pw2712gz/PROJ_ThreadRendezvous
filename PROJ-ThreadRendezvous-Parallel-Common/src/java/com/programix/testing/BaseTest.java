package com.programix.testing;

import java.math.*;

import com.programix.math.*;
import com.programix.util.*;

public abstract class BaseTest {
    protected final String displayName;
    protected final ScoringInfo scoringInfo;
    protected final Object listenerLock = new Object();
    private TestChunk.Listener listener;

    protected BaseTest(String displayName, ScoringInfo pScoringInfo) {
        ObjectTools.paramNullCheck(displayName, "displayName");
        this.displayName = displayName;
        this.scoringInfo = pScoringInfo != null ? pScoringInfo : ScoringInfo.ZERO_POINT_INSTANCE;
    }

    protected BaseTest(String displayName) {
        this(displayName, null);
    }

    public final String getDisplayName() {
        return displayName;
    }

    public final ScoringInfo getScoringInfo() {
        return scoringInfo;
    }

    public final void runTests(TestChunk.Listener pListener) {
        ObjectTools.paramNullCheck(pListener, "pListener");
        listener = pListener;
        try {
            performTests();
        } catch ( Throwable t ) {
            failureExceptionWithStackTrace(t);
            if ( t instanceof ThreadDeath ) throw t;
        }
    }

    /**
     * Subclasses fill in the detailed tests and call one of the outln
     * methods to log and indicate successes and failures.
     */
    protected abstract void performTests();

    /** Sleeps for the specified number of ms logging a message before going to sleep */
    protected final void sleepAndLog(long msSleepTime) throws InterruptedException {
        outln("sleeping for " + msSleepTime + " ms...");
        Thread.sleep(msSleepTime);
    }

    protected final void failureExceptionWithStackTrace(Throwable x) {
        synchronized ( listenerLock ) {
            TestChunk.ListenerEventMeta meta = new TestChunk.ListenerEventMeta();
            listener.incrementFailedCount(meta);
            listener.outErrorStackTrace(meta, x);
        }
    }

    protected final void outln(String msg) {
        listener.outln(new TestChunk.ListenerEventMeta(), msg);
    }

    protected final void outlnErrorText(String msg) {
        listener.outlnErrorText(new TestChunk.ListenerEventMeta(), msg);
    }

    protected final void outln(String msg, boolean success) {
        synchronized ( listenerLock ) {
            TestChunk.ListenerEventMeta meta = new TestChunk.ListenerEventMeta();
            if ( success ) {
                listener.incrementPassedCount(meta);
                listener.outln(meta, " PASS: " + msg);
            } else {
                listener.incrementFailedCount(meta);
                listener.outlnErrorText(meta, ">FAIL: " + msg);
            }
        }
    }

    protected final void outln(String msg,
                               boolean result,
                               boolean expectedResult) {

        outln(msg + " [result=" + result + ", expected=" +
            expectedResult + "]", result == expectedResult);
    }

    protected final void outln(String msg,
                               String result,
                               String expectedResult) {

        outln(msg + " [result=" + StringTools.quoteWrap(result) +
            ", expected=" + StringTools.quoteWrap(expectedResult) +
            "]", ObjectTools.isSame(result, expectedResult));
    }

    protected final void outln(String msg,
                               Object result,
                               Object expectedResult) {

        if ( (result instanceof String) &&
             (expectedResult instanceof String) ) {

            outln(msg, (String) result, (String) expectedResult);
        } else {
            outln(msg + " [result=" + result + ", expected=" + expectedResult +
                "]", ObjectTools.isSame(result, expectedResult));
        }
    }

    /**
     * Compares the BigDecimals without regard to scale differences.
     */
    protected final void outln(String msg,
                               BigDecimal result,
                               BigDecimal expectedResult) {

        boolean success = ObjectTools.isSame(result, expectedResult)
                ? true
                : DecimalTools.equalTo(result, expectedResult);

        outln(msg + " [result=" + result + ", expected=" + expectedResult +
            "]", success);
    }

    protected final void outln(String msg, int result, int expectedResult) {
        outln(msg + " [result=" + result + ", expected=" +
            expectedResult + "]", result == expectedResult);
    }

    protected final void outln(String msg,
                               int result,
                               int expectedResult,
                               int tolerance) {

        outln(msg + " [result=" + result + ", expected=" +
            expectedResult + ", tolerance=" + tolerance + "]",
            Math.abs(result - expectedResult) <= tolerance);
    }

    protected final void outln(String msg,
                               long result,
                               long expectedResult) {

        outln(msg + " [result=" + result + ", expected=" +
            expectedResult + "]", result == expectedResult);
    }

    protected final void outln(String msg,
                               long result,
                               long expectedResult,
                               long tolerance) {

        outln(msg + " [result=" + result + ", expected=" +
            expectedResult + ", tolerance=" + tolerance + "]",
            Math.abs(result - expectedResult) <= tolerance);
    }

    protected final void outln(String msg,
                               double result,
                               double expectedResult,
                               double tolerance,
                               int decimalPlaces) {

        String format = String.format(
            "%%s [result=%%.%df, expected=%%.%df, tolerance=%%.%df]",
            decimalPlaces, decimalPlaces, decimalPlaces);
        outln(String.format(format, msg, result, expectedResult , tolerance),
            Math.abs(result - expectedResult) <= tolerance);
    }

    protected final void outln(String msg,
                               Object[] result,
                               Object[] expectedResult) {

        if ( result == null || expectedResult == null ) {
            outln(msg, (Object) result, (Object) expectedResult);
            return;
        }

        if ( result.length != expectedResult.length ) {
            outln(msg + " [result.length=" + result.length +
                ", expected.length=" + expectedResult.length + "]", false);
            return;
        }

        for ( int i = 0; i < result.length; i++ ) {
            if ( ObjectTools.isDifferent(result[i], expectedResult[i]) ) {
                outln(msg + " [mismatch: result[" + i + "]=" + result[i] +
                    ", expected[" + i + "]=" + expectedResult[i], false);
                return;
            }
        }

        outln(msg + " [all objects match; result.length=" + result.length +
            ", expected.length=" + expectedResult.length + "]", true);
    }

    protected final void outln(String msg,
                               byte[] result,
                               byte[] expectedResult) {

        if ( result == null || expectedResult == null ) {
            outln(msg, (Object) result, (Object) expectedResult);
            return;
        }

        if ( result.length != expectedResult.length ) {
            outln(msg + " [result.length=" + result.length +
                ", expected.length=" + expectedResult.length + "]", false);
            return;
        }

        for ( int i = 0; i < result.length; i++ ) {
            if ( result[i] != expectedResult[i] ) {
                outln(msg + " [byte mismatch: result[" + i + "]=" + result[i] +
                    ", expected[" + i + "]=" + expectedResult[i], false);
                return;
            }
        }

        outln(msg + " [all bytes match; result.length=" + result.length +
            ", expected.length=" + expectedResult.length + "]", true);
    }

    protected final void outln(String msg, int[] result, int[] expectedResult) {
        if ( result == null || expectedResult == null ) {
            outln(msg, (Object) result, (Object) expectedResult);
            return;
        }

        if ( result.length != expectedResult.length ) {
            outln(msg + " [result.length=" + result.length +
                ", expected.length=" + expectedResult.length + "]", false);
            return;
        }

        for ( int i = 0; i < result.length; i++ ) {
            if ( result[i] != expectedResult[i] ) {
                outln(msg + " [int mismatch: result[" + i + "]=" + result[i] +
                    ", expected[" + i + "]=" + expectedResult[i], false);
                return;
            }
        }

        outln(msg + " [all ints match; result.length=" + result.length +
            ", expected.length=" + expectedResult.length + "]", true);
    }
}
