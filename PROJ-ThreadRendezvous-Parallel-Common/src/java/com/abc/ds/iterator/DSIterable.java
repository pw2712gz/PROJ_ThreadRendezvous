package com.abc.ds.iterator;

/**
 * Implemented by data structures who wish to declare that they can
 * be iterated over.
 */
public interface DSIterable<T> {
    /**
     * Create a new instance of {@link DSIterator} for traversing
     * the items in this sack.
     * Each call returns a new, independent iterator.
     * In general, any alterations to the data structure while iterating
     * render the iterator invalid.
     */
    DSIterator<T> createIterator();
}
