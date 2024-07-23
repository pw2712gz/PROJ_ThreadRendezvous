package com.programix.time;

import com.programix.thread.*;
import com.programix.util.*;

public class ElapsedTimeFormatter implements ThreadSafe {
    private final DateTime startTime;
    private final char[] elapsedDigit = new char[12];
    private int timeFormatLength = elapsedDigit.length;

    public ElapsedTimeFormatter(DateTime startTime) {
        ObjectTools.paramNullCheck(startTime, "startTime");
        this.startTime = startTime;
    }

    public synchronized int getTimeFormatLength() {
        return timeFormatLength;
    }

    public synchronized void setTimeFormatLength(int timeFormatLength)
            throws IllegalArgumentException {

        if ( timeFormatLength > elapsedDigit.length ) {
            throw new IllegalArgumentException(
                "Formatted time length can not be more than "
                + elapsedDigit.length);
        }

        if ( timeFormatLength < 5 ) {
            throw new IllegalArgumentException(
                "Formatted time length can not be less than 5 ");
        }

        this.timeFormatLength = timeFormatLength;
    }

    /**
     * Formats the elapsed time potentially up to:
     * <pre>
     *    hh:mm:ss.ppp (up to 99:59:59.999, then rolls over)
     * </pre>
     * If {@link #setTimeFormatLength(int)} has been set shorter, then
     * the elapsed time is clipped and just shows the right hand side.
     */
    public synchronized String formatElapsedTime(DateTime eventTime) {
        updateElapsedTime(eventTime.getTime() - startTime.getTime());
        int offset = elapsedDigit.length - timeFormatLength;
        return new String(elapsedDigit, offset, timeFormatLength);
    }

    private synchronized void updateElapsedTime(long msTotal) {
        int msPart = (int) (msTotal % 1000L);
        int secTotal = (int) (msTotal / 1000L);
        int secPart = secTotal % 60;
        int minTotal = secTotal / 60;
        int minPart = minTotal % 60;
        int hrTotal = minTotal / 60;
        int hrPart = hrTotal % 100;

        int ptr = elapsedDigit.length - 1;

        calcDigits(msPart, 3, elapsedDigit, ptr);
        ptr -= 3;

        elapsedDigit[ptr] = '.';
        ptr--;

        calcDigits(secPart, 2, elapsedDigit, ptr);
        ptr -= 2;

        elapsedDigit[ptr] = ':';
        ptr--;

        calcDigits(minPart, 2, elapsedDigit, ptr);
        ptr -= 2;

        elapsedDigit[ptr] = ':';
        ptr--;

        calcDigits(hrPart, 2, elapsedDigit, ptr);
    }

    private static void calcDigits(int val,
                                   int placeCount,
                                   char[] dst,
                                   int ptr) {

        for ( int i = 0; i < placeCount; i++ ){
            int placeVal = val % 10;
            val = val / 10;
            dst[ptr] = (char) ('0' + placeVal);
            ptr--;
        }
    }
}
