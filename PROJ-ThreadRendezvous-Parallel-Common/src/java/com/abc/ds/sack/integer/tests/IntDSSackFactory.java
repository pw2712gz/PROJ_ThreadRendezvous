package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;

public interface IntDSSackFactory {
    /**
     * Returns true if the underlying data structure allows duplicate items.
     */
    boolean allowDuplicates();

    /**
     * Returns true if the underlying data structure keeps the items in order.
     */
    boolean orderMatters();

    IntDSSack create();

    IntDSSack create(int initialCapacity, int percentToGrowCapacity);
}
