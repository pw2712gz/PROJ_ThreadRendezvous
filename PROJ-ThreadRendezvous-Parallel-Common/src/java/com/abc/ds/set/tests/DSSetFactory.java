package com.abc.ds.set.tests;

import com.abc.ds.set.*;

public interface DSSetFactory {
    /**
     * Returns true if the underlying data structure keeps the items in order
     * and that order is important to considering the results to be correct
     * or not.
     */
    boolean orderMatters();

    <T> DSSet<T> create(Class<T> itemType);

    <T> DSSet<T> create(Class<T> itemType,
                         int initialCapacity,
                         int percentToGrowCapacity);
}
