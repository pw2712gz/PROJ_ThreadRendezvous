package com.abc.ds.keyvalue;

import com.abc.ds.*;
import com.programix.util.*;

public class DSKeyValueTools {
    // no instances
    private DSKeyValueTools() {
    }

    @SuppressWarnings("unchecked")
    public static <K, V> DSKeyValuePair<K, V>[] createArray(
            int length,
            Class<K> keyType,
            Class<V> valueType) {

        return DSTools.createArrayFromType(DSKeyValuePair.class, length);
    }

    public static boolean isSame(DSKeyValuePair<?, ?> a,
                                 DSKeyValuePair<?, ?> b) {

        if ( a == b ) {
            // both pointing to same object, OR both are null
            return true;
        } else if ( a == null || b == null ) {
            // one is null and the other is NOT null
            return false;
        } else {
            return
                ObjectTools.isSame(a.getKey(), b.getKey()) &&
                ObjectTools.isSame(a.getValue(), b.getValue());
        }
    }

    /**
     * Compares two pairs without the caller having to cast to an annoying
     * generic type. If a is not null and b is not null and both aren't
     * an implementation of {@link DSKeyValuePair}, then false is returned.
     */
    public static boolean isSamePair(Object a,
                                     Object b) {

        if ( a == b ) {
            // both pointing to same object, OR both are null
            return true;
        } else if ( a == null || b == null ) {
            // one is null and the other is NOT null
            return false;
        } else if ( a instanceof DSKeyValuePair<?, ?> &&
                    b instanceof DSKeyValuePair<?, ?> ) {

            return isSame((DSKeyValuePair<?, ?>) a, (DSKeyValuePair<?, ?>) b);
        } else {
            return false;
        }
    }

    /**
     * Gets the hashCode for the key in the pair without regard for the
     * actual generic type of the pair. This way, two different
     * implementations of {@link DSKeyValuePair} get the same hashCode for
     * the same key.
     */
    public static int hashCodeForKey(DSKeyValuePair<?, ?> pair) {
        if ( pair == null ) {
            return 0;
        } else if ( pair.getKey() == null ) {
            return -1; // this shouldn't really happen, but quietly deal with it
        } else {
            return pair.getKey().hashCode();
        }
    }

    public static boolean isSameKey(DSKeyValuePair<?, ?> a,
                                    DSKeyValuePair<?, ?> b) {

        if ( a == b ) {
            // both pointing to same object, OR both are null
            return true;
        } else if ( a == null || b == null ) {
            // one is null and the other is NOT null
            return false;
        } else {
            return ObjectTools.isSame(a.getKey(), b.getKey());
        }
    }

    public static <K> K[] extractKeys(Class<K> keyType,
                                      DSKeyValuePair<K, ?>[] pairs) {

        int count = pairs == null ? 0 : pairs.length;
        K[] keys = DSTools.createArrayFromType(keyType, count);
        for ( int i = 0; i < keys.length; i++ ) {
            keys[i] = pairs[i] == null ? null : pairs[i].getKey();
        }
        return keys;
    }

    public static <V> V[] extractValues(Class<V> valueType,
                                        DSKeyValuePair<?, V>[] pairs) {

        int count = pairs == null ? 0 : pairs.length;
        V[] values = DSTools.createArrayFromType(valueType, count);
        for ( int i = 0; i < values.length; i++ ) {
            values[i] = pairs[i] == null ? null : pairs[i].getValue();
        }
        return values;
    }
}
