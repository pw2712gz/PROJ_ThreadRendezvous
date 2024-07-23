package com.abc.ds.filter;

import java.util.ArrayList;
import java.util.List;

import com.programix.util.ObjectTools;

/**
 * A {@link DSFilter} which does a logical AND on all the filters passed in.
 * A item is matches only if it matches all of the filters.
 * For efficiency, specify the filters most likely to not match first
 * (short-circuiting AND logic).
 */
public class AndDSFilter<T> implements DSFilter<T> {
    private final DSFilter<T>[] filters;

    @SafeVarargs
    public AndDSFilter(DSFilter<T>... filters) {
        if ( ObjectTools.isAnySlotEmpty(filters) ) {
            throw new IllegalArgumentException("at least one filter must be " +
                "specified and none of them can be a null reference");
        }
        this.filters = filters;
    }

    /**
     * Creates an {@link AndDSFilter} which has a {@link NotNullDSFilter}
     * prefixed onto the specified filters.
     * If, during filtering, any item is null, then false is immediately
     * returned and there's no need to evaluate the remaining filters.
     */
    @SafeVarargs
    public static <T> AndDSFilter<T> createWithNotNullScreen(DSFilter<T>... filters) {
        Builder<T> builder = new Builder<T>();
        builder.add(new NotNullDSFilter<T>());
        builder.addAll(filters);
        return builder.create();
    }

    @Override
    public boolean matches(T item) {
        for ( DSFilter<T> filter : filters ) {
            if ( !filter.matches(item) ) {
                return false;
            }
        }
        return true;
    }

    public static class Builder<T> {
        private final List<DSFilter<T>> filterList;

        public Builder() {
            filterList = new ArrayList<DSFilter<T>>();
        }

        public AndDSFilter<T> create() {
            if ( filterList.isEmpty() ) {
                throw new IllegalArgumentException(
                    "at least one filter must be added");
            }
            return new AndDSFilter<>(listToArray(filterList));
        }

        @SuppressWarnings("unchecked")
        private DSFilter<T>[] listToArray(List<DSFilter<T>> list) {
            return list.toArray(new DSFilter[0]);
        }

        public void add(DSFilter<T> filter) {
            ObjectTools.paramNullCheck(filter, "filter");
            filterList.add(filter);
        }

        @SafeVarargs
        public final void addAll(DSFilter<T>... filters) {
            if ( ObjectTools.isAnySlotEmpty(filters) ) {
                throw new IllegalArgumentException(
                    "at least one filter must be " +
                    "specified and none of them can be a null reference");
            }
            for ( DSFilter<T> filter : filters ) {
                add(filter);
            }
        }
    } // type Builder
}
