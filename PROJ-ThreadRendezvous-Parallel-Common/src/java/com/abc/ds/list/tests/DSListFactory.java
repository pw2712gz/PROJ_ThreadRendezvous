package com.abc.ds.list.tests;

import com.abc.ds.list.*;

public interface DSListFactory {
    <T> DSList<T> create(Class<T> itemType);

    <T> DSList<T> create(Class<T> itemType,
                         int initialCapacity,
                         int percentToGrowCapacity);
}
