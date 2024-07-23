package com.abc.ds.filter.integer;

/**
 * Used to filter items - only items returning true from matches are included.
 */
public interface IntDSFilter {
    /**
     * Returns true if the specified item should be included (matches the
     * filtering condition).
     */
    boolean matches(int item);
}
