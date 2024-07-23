package com.abc.ds.filter;


/**
 * A {@link DSFilter} which only matches items which are null.
 */
public class NullDSFilter<T> implements DSFilter<T> {
    public NullDSFilter() {
    }

    @Override
    public boolean matches(T item) {
        return item == null;
    }
}
