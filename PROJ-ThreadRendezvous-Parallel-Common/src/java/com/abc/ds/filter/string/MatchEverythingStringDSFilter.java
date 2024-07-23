package com.abc.ds.filter.string;

/**
 * A {@link StringDSFilter} which matches <em>everything</em> (always returns true).
 */
public class MatchEverythingStringDSFilter implements StringDSFilter {
    public MatchEverythingStringDSFilter() {
    }

    @Override
    public boolean matches(String item) {
        return true;
    }
}
