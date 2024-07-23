package com.abc.ds.iterator;

import java.util.*;

/**
 * Used to step through all the items in a data structure. In general, if
 * the underlying data structure is modified in any way, any active
 * iterators become invalid and will have undefined behavior.
 */
public interface DSIterator<T> {
    /**
     * Returns true if there are any more items to iterate through.
     */
    boolean hasNext();

    /**
     * Advanced to the next item and returns it. The first time this method
     * is called, it 'moves' to the first item.
     * If {@link #hasNext()} returns false, then calling this method results
     * in undefined behavior (but typically {@link NoSuchElementException} is
     * thrown).
     *
     * @throws NoSuchElementException if there are no more items, use
     * {@link #hasNext()} to check first to avoid this exception.
     */
    T next() throws NoSuchElementException;
}
