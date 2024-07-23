package com.abc.ds.hash;

import com.abc.ds.filter.*;
import com.abc.ds.iterator.*;
import com.abc.ds.keyvalue.*;

public interface DSHashTable<K, V> extends DSIterable<DSKeyValuePair<K, V>> {
    /** Returns the type of the keys in this hash table. */
    Class<K> getKeyType();

    /** Returns the type of the values in this hash table. */
    Class<V> getValueType();

    /** Returns the number of key-value pairs currently in the hash table. */
    int count();

    /** Returns true if there are no key-value pairs currently in the hash table. */
    boolean isEmpty();

    /** Removes all the key-value pairs from the hash table. */
    void clear();

    /**
     * Inserts or replaces the key-value pair for the specified key.
     * The value returned is the value previously stored under the key, or
     * null if nothing was previously stored for this key.
     */
    V insert(K key, V value);

    /** Returns true if the specified key is present in the hash table */
    boolean containsKey(K key);

    DSKeyValuePair<K, V>[] peekAll();

    /**
     * Finds and returns the key-value pairs with keys which match the
     * specified filter.
     * If nothing matches, a zero-length array is returned.
     */
    DSKeyValuePair<K, V>[] peekKeyMatches(DSFilter<K> keyFilter);

    /**
     * Returns the value stored by the specified key, or null if nothing is
     * stored under the key.
     */
    V peekValue(K key);

    /**
     * Removes all the key-value pairs with keys which match the specified filter.
     * @return the number of items removed
     */
    int deleteKeyMatches(DSFilter<K> keyFilter);

    /**
     * Removes the key-value pair and returns the value which was previously
     * stored under the specified key.
     * If not found, then null is quietly returned.
     */
    V delete(K key);

    /**
     * Iterates though all the key-value pairs in the hash table,
     * but the order of iteration may vary from call to call.
     */
    @Override
    DSIterator<DSKeyValuePair<K, V>> createIterator();

    /**
     * Returns an array of the keys found in the specified pairs, never null.
     * If pairs is null or a zero-length array, then a zero-length array of
     * keys is returned. If there's a null slot in the source array, the
     * result will have a null in the corresponding slot.
     */
    K[] extractKeys(DSKeyValuePair<K, V>[] pairs);

    /**
     * Returns an array of the values found in the specified pairs, never null.
     * If pairs is null or a zero-length array, then a zero-length array of
     * values is returned. If there's a null slot in the source array, the
     * result will have a null in the corresponding slot.
     */
    V[] extractValues(DSKeyValuePair<K, V>[] pairs);

}
