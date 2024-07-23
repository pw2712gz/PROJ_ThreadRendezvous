package com.abc.ds.filter;

import com.programix.util.*;

/**
 * A {@link DSFilter} which matches items which are considered to be
 * {@link ObjectTools#isSame(Object, Object) the same}.
 * The valueToCompareAgainst is permitted to be null and matches any null items.
 */
public class EqualToDSFilter<T> implements DSFilter<T> {
    private final T valueToCompareAgainst;

    public EqualToDSFilter(T valueToCompareAgainst) {
        this.valueToCompareAgainst = valueToCompareAgainst;
    }

    @Override
    public boolean matches(T item) {
        return ObjectTools.isSame(item, valueToCompareAgainst);
    }
}
