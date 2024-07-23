package com.abc.ds.filter;

import com.programix.util.ObjectTools;

/**
 * A {@link DSFilter} which does a logical OR on all the filters passed in.
 * A item is matches if it matches any of the filters.
 * For efficiency, specify the filters most likely to match first
 * (short-circuiting OR logic).
 */
public class OrDSFilter<T> implements DSFilter<T> {
    private final DSFilter<T>[] filters;

    @SafeVarargs
    public OrDSFilter(DSFilter<T>... filters) {
        if ( ObjectTools.isAnySlotEmpty(filters) ) {
            throw new IllegalArgumentException("at least one filter must be " +
                "specified and none of them can be a null reference");
        }
        this.filters = filters;
    }

    @Override
    public boolean matches(T item) {
        for ( DSFilter<T> filter : filters ) {
            if ( filter.matches(item) ) {
                return true;
            }
        }
        return false;
    }
}
