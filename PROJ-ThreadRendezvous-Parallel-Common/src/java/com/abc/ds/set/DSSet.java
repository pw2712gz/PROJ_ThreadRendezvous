package com.abc.ds.set;

import com.abc.ds.sack.*;

/**
 * A set of references to objects of type T.
 * May or may not be ordered and does <em>NOT</em> allow duplicates.
 */
public interface DSSet<T> extends DSSack<T> {
    /**
     * Potentially adds the specified item to the set returning true if
     * actually added.
     * If the item is a duplicate the request to add will be silently
     * ignored and false is returned.
     */
    @Override
    boolean add(T item);

    /**
     * Potentially removes the specified item from
     * the set returning true if actually found and removed.
     * Returns false if not found.
     */
    @Override
    boolean remove(T item);

    /**
     * Performs the set UNION of this set and the specified otherSet.
     * Any duplicates are silently ignored.
     * @return the number of items actually added.
     */
    int union(DSSet<T> otherSet);

    /**
     * Performs the set INTERSECTION of this set and the specified otherSet.
     * Only items which are in both this set and otherSet are retained.
     */
    void intersection(DSSet<T> otherSet);

    /**
     * Performs the set DIFFERENCE of this set and the specified otherSet.
     * Only items which are in this set and NOT in otherSet retained.
     */
    void subtract(DSSet<T> otherSet);
}
