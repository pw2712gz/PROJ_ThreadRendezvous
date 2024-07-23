package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;

public interface DSSackFactory {
    /**
     * Returns true if the underlying data structure allows duplicate items.
     */
    boolean allowDuplicates();

    /**
     * Returns true if the underlying data structure keeps the items in order.
     */
    boolean orderMatters();

    <T> DSSack<T> create(Class<T> itemType);

    <T> DSSack<T> create(Class<T> itemType,
                         int initialCapacity,
                         int percentToGrowCapacity);
}
