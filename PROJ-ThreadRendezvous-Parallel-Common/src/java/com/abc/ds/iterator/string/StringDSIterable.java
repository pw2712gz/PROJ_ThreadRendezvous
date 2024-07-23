package com.abc.ds.iterator.string;

/**
 * Implemented by String data structures who wish to declare that they can
 * be iterated over.
 */
public interface StringDSIterable {
    /**
     * Create a new instance of {@link StringDSIterator} for traversing
     * the items in this sack.
     * Each call returns a new, independent iterator.
     * In general, any alterations to the data structure while iterating
     * render the iterator invalid.
     */
    StringDSIterator createIterator();
}
