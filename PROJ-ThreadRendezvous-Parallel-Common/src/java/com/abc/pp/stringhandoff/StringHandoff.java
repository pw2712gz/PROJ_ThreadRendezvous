package com.abc.pp.stringhandoff;

import com.programix.thread.*;

/**
 * <h3>Rendezvous</h3>
 * <tt>StringHandoff</tt> is used to pass a <tt>String</tt> from one thread to another.
 * The passer and the receiver <u>meet</u> inside an instance for the hand-off.
 * For example, if <tt>pass()</tt> is called and there is no receiver waiting, the thread calling <tt>pass()</tt> will
 * block until a receiver arrives.
 * Similarly, if <tt>receive()</tt> is called and there is no passer waiting, the thread calling <tt>receive()</tt> will
 * block until a passer arrives.
 *
 * <h3>Timeouts Cause a <tt>TimedOutException</tt> to be Thrown</h3>
 * For this class, methods that can timeout do <u>not</u> return <tt>true</tt> for success and <tt>false</tt> for timeout.
 * Methods that take a timeout parameter (<tt>pass(String, long)</tt> or <tt>receive(long)</tt>) will throw a
 *  <b><tt>TimedOutException</tt></b> if the specified number of milliseconds passes without the hand-off occurring.
 * <p>
 * If the hand-off has completed&mdash;regardless of how long it took&mdash;no <tt>TimedOutException</tt> should be
 * thrown.
 * <p>
 * A timeout value of <tt>0L</tt> (zero) is used to indicate that the calling thread should wait indefinitely for the
 * condition to be met (never timeout).
 * <p>
 * Negative timeouts should be tolerated and be treated as a very short timeout.
 * <p>
 * <tt>TimedOutException</tt> is a <tt>RuntimeException</tt>.
 *
 * <h3>One Passer at a Time, One Receiver at a Time</h3>
 * There can only be one thread waiting to pass at any given time.
 * If threadA is waiting inside <tt>pass()</tt> and threadB calls <tt>pass()</tt>, threadB must throw an
 * <b><tt>IllegalStateException</tt></b>.
 * threadB throwing an exception should not affect threadA at all (threadA should continue waiting).
 * <p>
 * Similarly, there can only be one thread waiting to receive at any given time.
 * If threadA is waiting inside <tt>receive()</tt> and threadB calls <tt>receive()</tt>, threadB must throw an
 * <tt>IllegalStateException</tt>.
 * threadB throwing an exception should not affect threadA at all (threadA should continue waiting).
 * <p>
 * <tt>IllegalStateException</tt> is a <tt>RuntimeException</tt>.
 *
 * <h3>Shutdown</h3>
 * The methods that declare that they may throw a <b><tt>ShutdownException</tt></b> will do so after <tt>shutdown()</tt>
 * has been called.
 * The <tt>shutdown()</tt> method itself does <u>not</u> ever throw <tt>ShutdownException</tt> (in fact, it doesn't
 * ever throw any kind of exception).
 * The <tt>shutdown()</tt> method <i>may</i> be called multiple times and calling <tt>shutdown()</tt> multiple
 * times must be harmless.
 * <p>
 * After threadA has already called <tt>shutdown()</tt>, if threadB calls
 * <tt>pass(String)</tt>, <tt>pass(String, long)</tt>, <tt>receive()</tt>, or <tt>receive(long)</tt>
 * threadB must immediately throw <tt>ShutdownException</tt>.
 * <p>
 * If threadA is waiting inside
 * <tt>pass(String)</tt>, <tt>pass(String, long)</tt>, <tt>receive()</tt>, or <tt>receive(long)</tt>
 * and threadB calls <tt>shutdown()</tt>, threadA should stop waiting and throw <tt>ShutdownException</tt>.
 * <p>
 * ShutdownException is a RuntimeException.
 *
 * <h3>Exception Precedence</h3>
 * Exception precedence from highest to lowest is listed below:
 * <ol>
 * <li><tt>InterruptedException</tt></li>
 * <li><tt>ShutdownException</tt></li>
 * <li><tt>IllegalStateException</tt></li>
 * <li><tt>TimedOutException</tt></li>
 * </ol>
 * <p>
 * Example 1: while threadA is waiting,
 * if both of these happen before threadA can re-acquire the lock to get back from <tt>wait</tt>:
 * i) threadB calls <tt>shutdown()</tt> and then
 * ii) threadC interrupts threadA,
 * the result should be that threadA throws <tt>InterruptedException</tt>.
 * <p>
 * Example 2: while threadA is waiting with a timeout,
 * if both of these happen before threadA can re-acquire the lock to get back from <tt>wait</tt>:
 * i) threadB calls <tt>shutdown()</tt> and then
 * ii) threadA times out,
 * the result should be that threadA throws <tt>ShutdownException</tt>.
 * <p>
 * Example 3: while threadA is waiting inside <tt>pass()</tt>,
 * if both of these happen before threadA can re-acquire the lock to get back from <tt>wait</tt>:
 * i) threadB calls <tt>shutdown()</tt> and then
 * ii) threadC calls <tt>pass()</tt>,
 * the result should be that threadC should throw <tt>ShutdownException<</tt> (<i>not</i>
 * <tt>IllegalStateException</tt>!) AND threadA should throw <tt>ShutdownException</tt>.
 */
public interface StringHandoff {
    /**
     * Attempts to hand off the specified String to a receiver and waits inside until the hand-off can be completed
     * (or time runs out).
     *
     * @param msg the String to be handed off to a receiver
     * @param msTimeout number of milliseconds to wait for the hand-off to occur or zero to wait without ever timing
     * out. If msTimeout is negative, it should be treated as a very short timeout.
     * For a full explanation of the exceptions, see the interface Javadoc.
     * @throws InterruptedException if the calling thread is interrupted
     * @throws TimedOutException if time runs out AND the hand-off has not yet occurred
     * @throws ShutdownException if <tt>StringHandoff</tt> has been shutdown
     * @throws IllegalStateException if there is already a thread waiting to pass
     */
    void pass(String msg, long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException;

    /**
     * Attempts to hand off the specified String to a receiver and waits inside until the hand-off can be completed.
     *
     * @param msg the String to be handed off to a receiver
     * For a full explanation of the exceptions, see the interface Javadoc.
     * @throws InterruptedException if the calling thread is interrupted
     * @throws ShutdownException if <tt>StringHandoff</tt> has been shutdown
     * @throws IllegalStateException if there is already a thread waiting to pass
     */
    void pass(String msg) throws InterruptedException, ShutdownException, IllegalStateException;

    /**
     * Attempts to receive a String handed off by a passer and waits inside until the hand-off can be completed
     * (or time runs out).
     *
     * @param msTimeout number of milliseconds to wait for the hand-off to occur or zero to wait without ever timing
     * out. If msTimeout is negative, it should be treated as a very short timeout.
     * For a full explanation of the exceptions, see the interface Javadoc.
     * @throws InterruptedException if the calling thread is interrupted
     * @throws TimedOutException if time runs out AND the hand-off has not yet occurred
     * @throws ShutdownException if <tt>StringHandoff</tt> has been shutdown
     * @throws IllegalStateException if there is already a thread waiting to receive
     */
    String receive(long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException;

    /**
     * Attempts to receive a String handed off by a passer and waits inside until the hand-off can be completed.
     *
     * For a full explanation of the exceptions, see the interface Javadoc.
     * @throws InterruptedException if the calling thread is interrupted
     * @throws ShutdownException if <tt>StringHandoff</tt> has been shutdown
     * @throws IllegalStateException if there is already a thread waiting to receive
     */
    String receive() throws InterruptedException, ShutdownException, IllegalStateException;

    /**
     * Called to shutdown this instance of <tt>StringHandoff</tt>.
     * This call is asynchronous (does not wait, but simply signal that a shutdown has been requested).
     * This method may be safely called any number of times.
     * This method never throws any exceptions.
     */
    void shutdown();

    /**
     * Return a reference to the object being used for synchronization.
     * This can be used to allow the caller to bridge holding the lock between method calls.
     */
    Object getLockObject();
}
