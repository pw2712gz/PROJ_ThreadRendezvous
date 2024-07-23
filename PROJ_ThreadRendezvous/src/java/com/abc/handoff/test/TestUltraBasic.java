package com.abc.handoff.test;

import com.abc.handoff.*;
import com.abc.pp.stringhandoff.*;
import com.programix.thread.*;
import com.programix.util.*;

public class TestUltraBasic {
    private static void testPasserShowsUpFirst() throws InterruptedException {
        outln("===============================");
        outln("Entering testPasserShowsUpFirst...");

        StringHandoff handoff = new StringHandoffImpl();

        BasicPasser passer = new BasicPasser(handoff, "apple", 200L, "Passer-A");
        BasicReceiver receiver = new BasicReceiver(handoff, "apple", 1200L, "Receiver-A");

        Thread.sleep(2500L); // should be plenty of time for the handoff to have occurred
        passer.stopRequest();
        receiver.stopRequest();
        if (!passer.waitUntilStopped(2000L))
            outln("!!! passer ignored stopRequest and is still stuck running");
        if (!receiver.waitUntilStopped(2000L))
            outln("!!! receiver ignored stopRequest and is still stuck running");
        outln("... leaving testPasserShowsUpFirst");
        outln("===============================");
    }

    private static void testReceiverShowsUpFirst() throws InterruptedException {
        outln("===============================");
        outln("Entering testReceiverShowsUpFirst...");

        StringHandoff handoff = new StringHandoffImpl();

        BasicReceiver receiver = new BasicReceiver(handoff, "banana", 200L, "Receiver-B");
        BasicPasser passer = new BasicPasser(handoff, "banana", 1200L, "Passer-B");

        Thread.sleep(2500L); // should be plenty of time for the handoff to have occurred
        passer.stopRequest();
        receiver.stopRequest();
        if (!passer.waitUntilStopped(2000L))
            outln("!!! passer ignored stopRequest and is still stuck running");
        if (!receiver.waitUntilStopped(2000L))
            outln("!!! receiver ignored stopRequest and is still stuck running");

        outln("... leaving testReceiverShowsUpFirst");
        outln("===============================");
    }

    private static void outln(String fmt, Object... args) {
        ThreadTools.outln(String.format(fmt, args));
    }

    private static void out(Throwable t) {
        String[] line = StringTools.stackTraceToStringArray(t);
        for (int i = 0; i < line.length; i++) {
            outln(line[i]);
        }
    }

    public static void main(String[] args) {
        try {
            testPasserShowsUpFirst();
            Thread.sleep(500);
            testReceiverShowsUpFirst();
        } catch (Exception x) {
            out(x);
        }
    }

    private static class BasicPasser {
        private final StringHandoff handoff;
        private final String stringToPass;
        private final long msDelayBeforePassing;
        private final Thread internalThread;
        private volatile boolean keepGoing;

        public BasicPasser(StringHandoff handoff,
                String stringToPass,
                long msDelayBeforePassing,
                String name) {

            ObjectTools.paramNullCheck(handoff, "handoff");

            this.handoff = handoff;
            this.stringToPass = stringToPass;
            this.msDelayBeforePassing = Math.max(1, msDelayBeforePassing);

            keepGoing = true;
            internalThread = new Thread(() -> runWork(), name != null ? name : "Passer");
            internalThread.start();
        }

        private void runWork() {
            boolean wasInterrupted = false;
            try {
                outln("%s starting", Thread.currentThread().getName());
                outln("Taking a %d ms nap before calling pass(%s)",
                        msDelayBeforePassing, StringTools.quoteWrap(stringToPass));
                Thread.sleep(msDelayBeforePassing);
                if (!keepGoing)
                    return;

                outln("Attempting to call pass(%s)", StringTools.quoteWrap(stringToPass));
                handoff.pass(stringToPass);
                outln("... finished passing");
            } catch (InterruptedException x) {
                // ignore and let thread die
                wasInterrupted = true;
            } catch (Throwable x) {
                out(x);
            } finally {
                outln("%s finished%s", Thread.currentThread().getName(), wasInterrupted ? " (was interrupted)" : "");
            }
        }

        public void stopRequest() {
            keepGoing = false;
            internalThread.interrupt();
        }

        public boolean waitUntilStopped(long msTimeout) throws InterruptedException {
            internalThread.join(msTimeout);
            return !internalThread.isAlive();
        }

        private static void outln(String fmt, Object... args) {
            ThreadTools.outln(String.format(fmt, args));
        }

        private static void out(Throwable t) {
            String[] line = StringTools.stackTraceToStringArray(t);
            for (int i = 0; i < line.length; i++) {
                outln(line[i]);
            }
        }
    } // type BasicPasser

    private static class BasicReceiver {
        private final StringHandoff handoff;
        private final String expectedMessage;
        private final long msDelayBeforeAttemptingToReceive;
        private final Thread internalThread;
        private volatile boolean keepGoing;

        public BasicReceiver(StringHandoff handoff,
                String expectedMessage,
                long msDelayBeforeAttemptingToReceive,
                String name) {

            ObjectTools.paramNullCheck(handoff, "handoff");

            this.handoff = handoff;
            this.expectedMessage = expectedMessage;
            this.msDelayBeforeAttemptingToReceive = Math.max(1, msDelayBeforeAttemptingToReceive);

            keepGoing = true;
            internalThread = new Thread(() -> runWork(), name != null ? name : "Passer");
            internalThread.start();
        }

        private void runWork() {
            boolean wasInterrupted = false;
            try {
                outln("%s starting", Thread.currentThread().getName());
                outln("Taking a %d ms nap before calling receive()", msDelayBeforeAttemptingToReceive);
                Thread.sleep(msDelayBeforeAttemptingToReceive);
                if (!keepGoing)
                    return;

                outln("Attempting to call receive()");
                String result = handoff.receive();
                outln("... back from receive() with message: %s, %s", StringTools.quoteWrap(result),
                        StringTools.isSame(result, expectedMessage) ? "as expected (PASSED)"
                                : String.format("but expected: %s, (FAILED)", StringTools.quoteWrap(expectedMessage)));
            } catch (InterruptedException x) {
                // ignore and let thread die
                wasInterrupted = true;
            } catch (Throwable x) {
                out(x);
            } finally {
                outln("%s finished%s", Thread.currentThread().getName(), wasInterrupted ? " (was interrupted)" : "");
            }
        }

        public void stopRequest() {
            keepGoing = false;
            internalThread.interrupt();
        }

        public boolean waitUntilStopped(long msTimeout) throws InterruptedException {
            internalThread.join(msTimeout);
            return !internalThread.isAlive();
        }

        private static void outln(String fmt, Object... args) {
            ThreadTools.outln(String.format(fmt, args));
        }

        private static void out(Throwable t) {
            String[] line = StringTools.stackTraceToStringArray(t);
            for (int i = 0; i < line.length; i++) {
                outln(line[i]);
            }
        }
    } // type BasicReceiver
}
