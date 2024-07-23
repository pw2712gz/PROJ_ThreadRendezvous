package com.abc.ds.filter;

/**
 * A {@link DSFilter} which matches <em>everything</em> (always returns true).
 */
public class MatchEverythingDSFilter<T> implements DSFilter<T> {
    public MatchEverythingDSFilter() {
    }

    @Override
    public boolean matches(T item) {
        return true;
    }
}
