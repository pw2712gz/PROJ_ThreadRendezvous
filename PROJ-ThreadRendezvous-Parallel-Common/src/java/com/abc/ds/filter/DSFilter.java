package com.abc.ds.filter;

/**
 * Used to filter items - only items returning true from matches are included.
 */
public interface DSFilter<T> {
    /**
     * Returns true if the specified item should be included (matches the
     * filtering condition).
     */
    boolean matches(T item);
}
