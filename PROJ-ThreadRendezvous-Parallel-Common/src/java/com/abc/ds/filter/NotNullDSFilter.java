package com.abc.ds.filter;


/**
 * A {@link DSFilter} which only matches items which are not null.
 */
public class NotNullDSFilter<T> implements DSFilter<T> {
    public NotNullDSFilter() {
    }

    @Override
    public boolean matches(T item) {
        return item != null;
    }
}
