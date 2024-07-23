package com.abc.ds.compare;

public interface DSComparator<T> {
	DSCompareResult compare(T a, T b);
}
