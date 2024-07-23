package com.programix.testing;

import java.io.*;

import com.programix.thread.*;
import com.programix.time.*;
import com.programix.util.*;

public interface TestChunk {
    String getDisplayName();
    ScoringInfo getScoringInfo();
    int getActiveThreadCount();
    TestState getTestState();

    boolean kickoffTestsAsync();
    void cancelAllTests();

    void addListener(Listener listener);
    void removeListener(Listener listener);

    public static interface Listener {
        public static final Listener[] ZERO_LEN_ARRAY = new Listener[0];

        void outln(ListenerEventMeta meta, String line);
        void outStackTrace(ListenerEventMeta meta, Throwable t);

        void outlnErrorText(ListenerEventMeta meta, String line);
        void outErrorStackTrace(ListenerEventMeta meta, Throwable t);

        void incrementPassedCount(ListenerEventMeta meta);
        void incrementFailedCount(ListenerEventMeta meta);

        void testStateChanged(ListenerEventMeta meta,
                              TestState oldState,
                              TestState newState);
    } // type Listener

    /**
     * Meta-information about the {@link Listener} event.
     */
    public static class ListenerEventMeta {
        private final DateTime whenItOccurred;
        private final String threadName;

        public ListenerEventMeta(DateTime whenItOccurred,
                                 String threadName) {

            this.whenItOccurred =
                whenItOccurred == null ? DateTools.getNow() : whenItOccurred;
            this.threadName =
                threadName == null ? "<unknown>" : threadName;
        }

        public ListenerEventMeta() {
            this(DateTools.getNow(), Thread.currentThread().getName());
        }

        public DateTime getWhenItOccurred() {
            return whenItOccurred;
        }

        public String getThreadName() {
            return threadName;
        }
    } // type ListenerEventMeta

    /**
     * Implementation of {@link Listener} which does nothing when each
     * of the methods is called - this is meant for subclassing.
     */
    public static abstract class ListenerAdapter implements Listener {
        protected ListenerAdapter() {
        }

        @Override
        public void outln(ListenerEventMeta meta, String line) {
        }

        @Override
        public void outStackTrace(ListenerEventMeta meta, Throwable t) {
        }

        @Override
        public void outlnErrorText(ListenerEventMeta meta, String line) {
        }

        @Override
        public void outErrorStackTrace(ListenerEventMeta meta, Throwable t) {
        }

        @Override
        public void incrementPassedCount(ListenerEventMeta meta) {
        }

        @Override
        public void incrementFailedCount(ListenerEventMeta meta) {
        }

        @Override
        public void testStateChanged(ListenerEventMeta meta,
                                     TestState oldState,
                                     TestState newState) {
        }
    } // type ListenerAdapter

    public static abstract class StandardBaseListener implements Listener {
        private TestState currentTestState;
        private final Counter passCounter;
        private final Counter failCounter;
        private ElapsedTimeFormatter elapsedTimeFormatter;

        protected StandardBaseListener() {
            currentTestState = TestState.NEVER_STARTED;
            passCounter = new Counter(0, this);
            failCounter = new Counter(0, this);
//            elapsedTimeFormatter = new ElapsedTimeFormatter(DateTools.getNow());
        }

        protected abstract void println(boolean isError, String line);

        protected synchronized void outPrintln(String format, Object... args) {
            println(false, String.format(format, args));
        }

        protected synchronized void errPrintln(String format, Object... args) {
            println(true, String.format(format, args));
        }

        private synchronized ElapsedTimeFormatter getElapsedTimeFormatter() {
            if ( elapsedTimeFormatter == null ) {
                elapsedTimeFormatter = new ElapsedTimeFormatter(DateTools.getNow());
            }
            return elapsedTimeFormatter;
        }

        protected synchronized String formatLinePrefix(ListenerEventMeta meta) {
            return String.format("%12.12s|%-20.20s|",
                getElapsedTimeFormatter().formatElapsedTime(
                    meta.getWhenItOccurred()),
                    meta.getThreadName());
        }

        protected synchronized String formatLine(ListenerEventMeta meta,
                                                 String format,
                                                 Object... args) {
            return formatLinePrefix(meta) + String.format(format, args);
        }

        protected synchronized String[] formatStackTrace(ListenerEventMeta meta,
                                                         Throwable t) {
            String linePrefix = formatLinePrefix(meta);
            String[] traceLines = StringTools.stackTraceToStringArray(t);
            String[] lines = new String[traceLines.length];
            for ( int i = 0; i < lines.length; i++ ) {

                lines[i] = String.format("%s%s%s",
                    linePrefix, (i == 0 ? ">FAIL: " : ""), traceLines[i].replace("\t", "    "));
            }
            return lines;
        }

        public synchronized TestState getCurrentTestState() {
            return currentTestState;
        }

        @Override
        public synchronized void outln(ListenerEventMeta meta, String line) {
            outPrintln(formatLine(meta, line));
        }

        @Override
        public synchronized void outlnErrorText(ListenerEventMeta meta,
                                                String line) {
            errPrintln(formatLine(meta, line));
        }

        @Override
        public synchronized void outStackTrace(ListenerEventMeta meta,
                                               Throwable t) {
            for ( String line : formatStackTrace(meta, t) ) {
                outPrintln(line);
            }
        }

        @Override
        public synchronized void outErrorStackTrace(ListenerEventMeta meta,
                                                    Throwable t) {
            for ( String line : formatStackTrace(meta, t) ) {
                errPrintln(line);
            }
        }

        @Override
        public synchronized void incrementPassedCount(ListenerEventMeta meta) {
            passCounter.increment();
        }

        @Override
        public synchronized void incrementFailedCount(ListenerEventMeta meta) {
            failCounter.increment();
        }

        protected synchronized String passFailCountsMessage() {
            return String.format("[%,d passed, %,d failed, %,d total]",
                passCounter.getCount(),
                failCounter.getCount(),
                passCounter.getCount() + failCounter.getCount());
        }

        @Override
        public synchronized void testStateChanged(ListenerEventMeta meta,
                                                  TestState oldState,
                                                  TestState newState) {
            currentTestState = newState;
            outPrintln(formatLine(meta, "state changed from '%s' to '%s' %s",
                oldState.getDisplayName(),
                newState.getDisplayName(),
                passFailCountsMessage()));
        }
    } // type StandardBaseListener

    public static class ConsoleListener
            extends StandardBaseListener implements Listener {

        public ConsoleListener() {
        }

        @Override
        protected synchronized void println(boolean isError, String line) {
            synchronized ( System.out ) {
                @SuppressWarnings("resource")
                PrintStream ps = isError ? System.err : System.out;
                ps.println(line);
            }
        }
    } // type ConsoleListener
}
