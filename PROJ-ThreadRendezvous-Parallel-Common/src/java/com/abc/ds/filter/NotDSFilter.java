package com.abc.ds.filter;

import com.programix.util.ObjectTools;

/**
 * A {@link DSFilter} which does a logical NOT on the result from
 * the underlying filter.
 */
public class NotDSFilter<T> implements DSFilter<T> {
    private final DSFilter<T> filterToInvert;

    public NotDSFilter(DSFilter<T> filterToInvert) {
        ObjectTools.paramNullCheck(filterToInvert, "filterToInvert");
        this.filterToInvert = filterToInvert;
    }

    @Override
    public boolean matches(T item) {
        return !filterToInvert.matches(item);
    }
}
