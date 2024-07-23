package com.abc.ds.filter.string;

/**
 * Used to filter items - only items returning true from matches are included.
 */
public interface StringDSFilter {
    /**
     * Returns true if the specified item should be included (matches the
     * filtering condition).
     */
    boolean matches(String item);
}
