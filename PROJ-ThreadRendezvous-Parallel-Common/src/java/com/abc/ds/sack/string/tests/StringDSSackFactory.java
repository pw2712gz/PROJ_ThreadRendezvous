package com.abc.ds.sack.string.tests;

import com.abc.ds.sack.string.*;

public interface StringDSSackFactory {
    /**
     * Returns true if the underlying data structure allows duplicate items.
     */
    boolean allowDuplicates();

    /**
     * Returns true if the underlying data structure keeps the items in order.
     */
    boolean orderMatters();

    StringDSSack create();

    StringDSSack create(int initialCapacity,
                        int percentToGrowCapacity);
}
