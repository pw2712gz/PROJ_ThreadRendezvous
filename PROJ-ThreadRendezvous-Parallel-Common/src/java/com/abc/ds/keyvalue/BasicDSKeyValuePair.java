package com.abc.ds.keyvalue;

import com.programix.util.*;

/**
 * Hold an key-value pair.
 * Instances are immutable is K and V are immutable. K must be immutable
 * because the hashCode is cached.
 */
public final class BasicDSKeyValuePair<K, V> extends AbstractDSKeyValuePair<K, V> {
    private final K key;
    private final V value;

    public BasicDSKeyValuePair(K key, V value)
            throws IllegalArgumentException {

        ObjectTools.paramNullCheck(key, "key");
        this.key = key;
        this.value = value;
    }

    public static <K, V> BasicDSKeyValuePair<K, V> createFrom(
           DSKeyValuePair<K, V> origPair) {

        return new BasicDSKeyValuePair<K, V>(
            origPair.getKey(), origPair.getValue());
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }
}
