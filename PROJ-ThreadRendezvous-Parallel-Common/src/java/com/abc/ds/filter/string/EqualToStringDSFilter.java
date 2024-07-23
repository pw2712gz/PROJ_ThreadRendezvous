package com.abc.ds.filter.string;

import com.programix.util.*;

/**
 * A {@link StringDSFilter} which matches items which are equal.
 */
public class EqualToStringDSFilter implements StringDSFilter {
    private final String valueToCompareAgainst;

    public EqualToStringDSFilter(String valueToCompareAgainst) {
        this.valueToCompareAgainst = valueToCompareAgainst;
    }

    @Override
    public boolean matches(String item) {
        return ObjectTools.isSame(valueToCompareAgainst, item);
    }
}
