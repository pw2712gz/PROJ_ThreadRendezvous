package com.abc.ds.list.integer.tests;

import com.abc.ds.list.integer.*;

public interface IntDSListFactory {
    IntDSList create();

    IntDSList create(int initialCapacity, int percentToGrowCapacity);
}
