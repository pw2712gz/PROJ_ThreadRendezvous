package com.abc.ds.compare;

public final class ReverseDSComparator<T> implements DSComparator<T> {
    private final DSComparator<T> forwardComparator;

    /**
     * Takes the supplied <i>forward</i> {@link DSComparator} and
     * holds it for use when the reversing is needed.
     */
    public ReverseDSComparator(DSComparator<T> forwardComparator) {
        this.forwardComparator = forwardComparator;
    }

    /**
     * This implementation of the
     * {@link DSComparator#compare(Object, Object) compare} method on
     * {@link DSComparator} will quickly check
     * if both references are the same using <tt>==</tt> (which
     * evaluates to <tt>true</tt> if both references are pointing
     * to the same object or both are <tt>null</tt>).
     * If the references are the same, then this will quickly return
     * <tt>0</tt>.
     * <p>
     * If the references are not the same, then the forward comparator supplied
     * at construction is used.
     */
    @Override
    public DSCompareResult compare(T a, T b) {
        if ( a == b ) {
            // pointing to the same object, or both are null.
            return DSCompareResult.EQUAL_TO;
        } else {
            return forwardComparator.compare(b, a); // yes, switched order
        }
    }
}