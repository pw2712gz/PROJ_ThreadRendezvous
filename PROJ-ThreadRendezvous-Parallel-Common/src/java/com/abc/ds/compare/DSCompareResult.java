package com.abc.ds.compare;

public enum DSCompareResult {
	LESS_THAN,
	EQUAL_TO,
	GREATER_THAN;

	/**
	 * Returns the instance for the specific int value.
	 * If value < 0, then {@link #LESS_THAN} is returned.
	 * If value > 0, then {@link #GREATER_THAN} is returned.
	 * If value == 0, then {@link #EQUAL_TO} is returned.
	 */
	public static DSCompareResult instanceFor(int value) {
		if ( value < 0 ) return LESS_THAN;
		if ( value > 0 ) return GREATER_THAN;
		return EQUAL_TO;
	}

    /**
     * Returns the instance for the specific int value.
     * If value < 0, then {@link #LESS_THAN} is returned.
     * If value > 0, then {@link #GREATER_THAN} is returned.
     * If value == 0, then {@link #EQUAL_TO} is returned.
     */
	public static DSCompareResult instanceFor(long value) {
        if ( value < 0L ) return LESS_THAN;
        if ( value > 0L ) return GREATER_THAN;
        return EQUAL_TO;
	}
}
