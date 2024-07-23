package com.abc.ds.keyvalue;

import com.abc.ds.*;

/**
 * Provides a solid implementation of {@link #equals(Object)}, {@link #hashCode()},
 * and {@link #toString()}. This should only be used as the super-class of
 * {@link DSKeyValuePair}'s with immutable keys (the hash code for they key
 * is cached internally!).
 */
public abstract class AbstractDSKeyValuePair<K, V>
        implements DSKeyValuePair<K, V>, DSCachedHashCode {

    private Integer cachedHashCode = null;

    protected AbstractDSKeyValuePair() {
    }

    /**
     * Returns true if equivalent to the specified obj (same key, same value).
     * See {@link DSKeyValueTools#isSamePair(Object, Object)}
.     */
    @Override
    public boolean equals(Object obj) {
        return DSKeyValueTools.isSamePair(this, obj);
    }

    /**
     * Efficiently returns the hashCode the the key of this pair.
     * If the key is null, then 0 is used as the hashCode.
     * The first time this method is called, the hash code is calculated
     * and cached.
     */
    @Override
    public int hashCode() {
        synchronized ( this ) {
            if ( cachedHashCode == null ) {
                cachedHashCode = new Integer(
                    getKey() == null ? 0 : getKey().hashCode());
            }
            return cachedHashCode.intValue();
        }
    }

    /**
     * Returns formatted as: "key=value".
     */
    @Override
    public String toString() {
        return getKey() + "=" + getValue();
    }
}
