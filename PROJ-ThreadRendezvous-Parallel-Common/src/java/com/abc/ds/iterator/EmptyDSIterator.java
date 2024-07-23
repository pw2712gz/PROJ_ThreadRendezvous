package com.abc.ds.iterator;

import java.util.*;

/**
 * An empty iterator which meets the contract, but iterates over zero items.
 * Typically, internal implementations will use this when they are known
 * to be empty.
 * Keep in mind that since instances are immutable, there's no need for more
 * than one of each generic type.
 * Instances are immutable.
 */
public final class EmptyDSIterator<T> implements DSIterator<T> {
    public EmptyDSIterator() {
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() throws NoSuchElementException {
        throw new NoSuchElementException("empty, cannot call next()");
    }
}
