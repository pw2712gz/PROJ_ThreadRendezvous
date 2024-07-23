package com.abc.pp.fifo;

public interface PPBoundedFifo<T> {
    /** The type of items stored. */
    Class<T> getItemType();

    /** Returns the number if items currently in the FIFO. */
    int getCount();

    /** Returns true if {@link #getCount()} == 0. */
    boolean isEmpty();

    /** Returns true if {@link #getCount()} == {@link #getCapacity()}. */
    boolean isFull();

    /** Removes any and all items in the FIFO leaving it in an empty state. */
    void clear();

    /**
     * Returns the maximum number of items which can be stored in this FIFO.
     * This value never changes.
     */
    int getCapacity();

    /**
     * Add the specified item to the fifo.
     * If currently full, the calling thread waits until there is space and
     * then adds the item.
     * If this method doesn't throw InterruptedException, then the item was
     * successfully added.
     */
    void add(T item) throws InterruptedException;

    /**
     * Adds all of the specified items. This call may wait multiple times
     * until all have been added. The number of items to add can exceed the
     * capacity... it just waits until some other thread removes items to make
     * more space.
     * If this method doesn't throw InterruptedException, then all of the items
     * were successfully added.
     */
    void addAll(T[] items) throws InterruptedException;

    /**
     * Removes and returns the next item.
     * If currently empty, the calling thread waits until another thread adds
     * an item.
     * If this method doesn't throw InterruptedException, then the item was
     * successfully removed.
     */
    T remove() throws InterruptedException;

    /**
     * Removes all the items, waiting until there is at least one to remove.
     * If currently empty, the calling thread waits until another thread adds
     * an item.
     */
    T[] removeAtLeastOne() throws InterruptedException;

    /**
     * Immediately removes any and all items without waiting.
     * If currently empty, a zero-length array is returned (non-blocking).
     */
    T[] removeAll();

    /**
     * Returns a reference to use for synchronized blocks which need to
     * call multiple methods without other threads being able to get in.
     * Never returns null.
     */
    Object getLockObject();

    /**
     * Waits until the fifo is empty with a timeout.
     * If currently empty, the calling thread returns right away.
     * @param msTimeout approximate maximum number of ms to wait for the fifo
     * to become empty. If this much time elapses and it's still not empty,
     * false is returned. A value of 0 means the waiting should never time out.
     * @return true if empty, false if timed out
     * @throws InterruptedException if interrupted while waiting
     */
    boolean waitUntilEmpty(long msTimeout) throws InterruptedException;

    /**
     * Waits until the fifo is empty (no timeout).
     * If currently empty, the calling thread returns right away.
     * @throws InterruptedException if interrupted while waiting
     */
    void waitUntilEmpty() throws InterruptedException;

    /**
     * Waits while the fifo is empty with a timeout.
     * If not currently empty, the calling thread returns right away.
     * @param msTimeout approximate maximum number of ms to wait while the fifo
     * is empty. If this much time elapses and it's still empty,
     * false is returned. A value of 0 means the waiting should never time out.
     * @return true if not empty, false if timed out
     * @throws InterruptedException if interrupted while waiting
     */
    boolean waitWhileEmpty(long msTimeout) throws InterruptedException;

    /**
     * Waits while the fifo is empty (no timeout).
     * If not currently empty, the calling thread returns right away.
     * @throws InterruptedException if interrupted while waiting
     */
    void waitWhileEmpty() throws InterruptedException;

    /**
     * Waits until the fifo is full with a timeout.
     * If currently full, the calling thread returns right away.
     * @param msTimeout approximate maximum number of ms to wait for the fifo
     * to become full. If this much time elapses and it's still not full,
     * false is returned. A value of 0 means the waiting should never time out.
     * @return true if full, false if timed out
     * @throws InterruptedException if interrupted while waiting
     */
    boolean waitUntilFull(long msTimeout) throws InterruptedException;

    /**
     * Waits until the fifo is full (on timeout).
     * If currently full, the calling thread returns right away.
     * @throws InterruptedException if interrupted while waiting
     */
    void waitUntilFull() throws InterruptedException;

    /**
     * Waits while the fifo is full with a timeout.
     * If not currently full, the calling thread returns right away.
     * @param msTimeout approximate maximum number of ms to wait while the fifo
     * is full. If this much time elapses and it's still full,
     * false is returned. A value of 0 means the waiting should never time out.
     * @return true if not full, false if timed out
     * @throws InterruptedException if interrupted while waiting
     */
    boolean waitWhileFull(long msTimeout) throws InterruptedException;

    /**
     * Waits while the fifo is full (no timeout).
     * If not currently full, the calling thread returns right away.
     * @throws InterruptedException if interrupted while waiting
     */
    void waitWhileFull() throws InterruptedException;
}
