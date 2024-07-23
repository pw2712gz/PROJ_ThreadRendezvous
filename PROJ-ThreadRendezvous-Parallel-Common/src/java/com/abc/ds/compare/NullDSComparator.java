package com.abc.ds.compare;

import com.programix.util.*;

public class NullDSComparator<T> implements DSComparator<T> {
	private final DSNullOrdering nullOrdering;
	private final DSComparator<T> rawComparator;

	public NullDSComparator(DSNullOrdering nullOrdering,
							DSComparator<T> rawComparator) {

		ObjectTools.paramNullCheck(nullOrdering, "nullOrdering");
		ObjectTools.paramNullCheck(rawComparator, "rawComparator");
		this.nullOrdering = nullOrdering;
		this.rawComparator = rawComparator;
	}

	@Override
	public DSCompareResult compare(T a, T b) {
        if ( a == b ) {
            // both pointing to same object, OR both are null
            return DSCompareResult.EQUAL_TO;
        } else if ( a == null || b == null ) {
            // one is null and the other is NOT null
        	if ( a == null ) {
        	    // a is null, b is NOT null
        		return (nullOrdering == DSNullOrdering.NULL_FIRST) ?
    				DSCompareResult.LESS_THAN :
					DSCompareResult.GREATER_THAN;
        	} else {
        	    // a is NOT null, b is null
                return (nullOrdering == DSNullOrdering.NULL_FIRST) ?
                    DSCompareResult.GREATER_THAN :
                    DSCompareResult.LESS_THAN;

        	}
        } else {
        	// neither is null
            return rawComparator.compare(a, b);
        }
	}
}