package com.abc.ds.filter;

import com.abc.ds.compare.*;
import com.programix.util.*;

/**
 * A {@link DSFilter} which matches items using a {@link DSComparator}.
 * For example:
 * <pre>
 * DSFilter&lt;String&gt; filter = new ComparatorDSFilter&lt;&gt;(
 *     DSCompareMatchType.LESS_THAN_OR_EQUAL_TO,
 *     "banana",
 *     StringDSComparator.NULL_LAST_CASE_INSENSITIVE_ASC);
 *
 * String[] words = new String[] { "Apple", "Banana", null, "Cherry" };
 * for ( String word : words ) {
 *     System.out.println("filter.matches(" + word + ")=" + filter.matches(word));
 * }
 * </pre>
 * produces:
 * <pre>
 * filter.matches(Apple)=true
 * filter.matches(Banana)=true
 * filter.matches(null)=false
 * filter.matches(Cherry)=false
 * </pre>
 */
public class ComparatorDSFilter<T> implements DSFilter<T> {
    private final T valueToCompareAgainst;
    private final DSCompareMatchType compareMatchType;
    private final DSComparator<T> comparator;

    public ComparatorDSFilter(DSCompareMatchType compareMatchType,
                              T valueToCompareAgainst,
                              DSComparator<T> comparator) {

        ObjectTools.paramNullCheck(valueToCompareAgainst, "valueToCompareAgainst");
        ObjectTools.paramNullCheck(compareMatchType, "compareMatchType");
        ObjectTools.paramNullCheck(comparator, "comparator");

        this.valueToCompareAgainst = valueToCompareAgainst;
        this.comparator = comparator;
        this.compareMatchType = compareMatchType;
    }

    @Override
    public boolean matches(T item) {
        DSCompareResult compareResult = comparator.compare(item, valueToCompareAgainst);
        return compareMatchType.matches(compareResult);
    }
}
