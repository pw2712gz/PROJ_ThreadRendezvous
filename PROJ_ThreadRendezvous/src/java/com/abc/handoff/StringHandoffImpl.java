package com.abc.handoff;

import com.abc.pp.stringhandoff.*;
import com.programix.thread.*;

public class StringHandoffImpl implements StringHandoff {
    private String message; // null if empty
    private boolean isShutdown = false;
    private boolean isPassing = false;
    private boolean isReceiving = false;

    public StringHandoffImpl() {
    }

    @Override
    public synchronized void pass(String msg, long msTimeout)
            throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {
        if (isShutdown) {
            throw new ShutdownException("The system is shutting down.");
        }
        if (msg == null) {
            throw new IllegalStateException("Cannot pass null message.");
        }
        if (isPassing) {
            throw new IllegalStateException("Another pass operation is in progress.");
        }
        isPassing = true;
        try {
            long endTime;
            if (msTimeout > 0) {
                endTime = System.currentTimeMillis() + msTimeout;
            } else {
                endTime = Long.MAX_VALUE;
            }

            while (message != null) {
                if (isShutdown) {
                    throw new ShutdownException("The system is shutting down.");
                }
                long waitTime = endTime - System.currentTimeMillis();
                if (waitTime <= 0) {
                    throw new TimedOutException("Pass operation timed out");
                }
                wait(waitTime);
            }

            if (isShutdown) {
                throw new ShutdownException("The system is shutting down.");
            }

            message = msg;
            notifyAll();

            while (message != null) {
                if (isShutdown) {
                    throw new ShutdownException("The system is shutting down.");
                }
                long waitTime = endTime - System.currentTimeMillis();
                if (waitTime <= 0) {
                    message = null;
                    notifyAll();
                    throw new TimedOutException("Pass operation timed out");
                }
                wait(waitTime);
            }
        } catch (InterruptedException e) {
            if (!isShutdown) {
                message = null;
                notifyAll();
            }
            throw e;
        } finally {
            isPassing = false;
            notifyAll();
        }
    }

    @Override
    public synchronized String receive(long msTimeout)
            throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {
        if (isShutdown) {
            throw new ShutdownException("The system is shutting down.");
        }
        if (isReceiving) {
            throw new IllegalStateException("Another receive operation is in progress.");
        }
        isReceiving = true;
        try {
            long endTime;
            if (msTimeout > 0) {
                endTime = System.currentTimeMillis() + msTimeout;
            } else {
                endTime = Long.MAX_VALUE;
            }

            while (message == null) {
                if (isShutdown) {
                    throw new ShutdownException("The system is shutting down.");
                }
                long waitTime = endTime - System.currentTimeMillis();
                if (waitTime <= 0) {
                    throw new TimedOutException("Receive operation timed out");
                }
                wait(waitTime);
            }

            if (isShutdown) {
                throw new ShutdownException("The system is shutting down.");
            }

            String msg = message;
            message = null;
            notifyAll();
            return msg;
        } finally {
            isReceiving = false;
            notifyAll();
        }
    }

    @Override
    public synchronized void pass(String msg)
            throws InterruptedException, ShutdownException, IllegalStateException {
        pass(msg, 0L);
    }

    @Override
    public synchronized String receive()
            throws InterruptedException, ShutdownException, IllegalStateException {
        return receive(0L);
    }

    @Override
    public synchronized void shutdown() {
        isShutdown = true;
        notifyAll();
    }

    @Override
    public Object getLockObject() {
        return this;
    }
}