package com.abc.ds.iterator.integer;

/**
 * Implemented by int data structures who wish to declare that they can
 * be iterated over.
 */
public interface IntDSIterable {
    /**
     * Create a new instance of {@link IntDSIterator} for traversing
     * the items.
     * Each call returns a new, independent iterator.
     * In general, any alterations to the data structure while iterating
     * render the iterator invalid.
     */
    IntDSIterator createIterator();
}
