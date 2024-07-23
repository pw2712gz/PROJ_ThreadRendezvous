package com.abc.ds.fifo;

import java.util.*;

/**
 * A First-In-First-Out (FIFO) queue which has unlimited capacity.
 * See <a href="http://en.wikipedia.org/wiki/FIFO">FIFO</a> at Wikipedia.
 */
public interface DSUnboundedFifo<T> {
    Class<T> getItemType();

    int count();
    boolean isEmpty();
    void clear();

    /**
     * Appends an item to the end of the FIFO.
     */
    void add(T item);

    /**
     * Appends each of the specified items to the end of the FIFO in the
     * order they are specified.
     */
    @SuppressWarnings("unchecked")
    void addAll(T... items);

    /**
     * Removes and returns the item at the front of the FIFO
     * or throws {@link NoSuchElementException} if currently empty.
     * Use {@link #isEmpty()} to avoid this exception.
     */
    T remove() throws NoSuchElementException;

    /**
     * Removes and returns all the items currently in the FIFO.
     * If empty, a zero-length array is returned, never null.
     */
    T[] removeAll();

    /**
     * Returns the item at the front of the FIFO
     * or throws {@link NoSuchElementException} if currently empty.
     * Use {@link #isEmpty()} to avoid this exception.
     */
    T peek() throws NoSuchElementException;

    /**
     * Returns all the items currently in the FIFO.
     * If empty, a zero-length array is returned, never null.
     */
    T[] peekAll();
}
