package com.abc.ds.filter.integer;

/**
 * A {@link IntDSFilter} which matches items which are equal.
 */
public class EqualToIntDSFilter implements IntDSFilter {
    private final int valueToCompareAgainst;

    public EqualToIntDSFilter(int valueToCompareAgainst) {
        this.valueToCompareAgainst = valueToCompareAgainst;
    }

    @Override
    public boolean matches(int item) {
        return valueToCompareAgainst == item;
    }
}
