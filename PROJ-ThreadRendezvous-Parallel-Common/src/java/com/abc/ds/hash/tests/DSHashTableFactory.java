package com.abc.ds.hash.tests;

import com.abc.ds.hash.*;

public interface DSHashTableFactory {
    <K, V> DSHashTable<K, V> create(Class<K> keyType,
                                    Class<V> valueType,
                                    int capacity,
                                    double loadFactor);
}
