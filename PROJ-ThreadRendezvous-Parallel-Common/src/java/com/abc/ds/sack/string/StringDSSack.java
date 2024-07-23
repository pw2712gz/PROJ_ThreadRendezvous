package com.abc.ds.sack.string;

import com.abc.ds.action.string.*;
import com.abc.ds.filter.string.*;
import com.abc.ds.iterator.string.*;

/**
 * A sack of references to String's.
 * May or may not be ordered and may or may not allow duplicates.
 */
public interface StringDSSack extends StringDSIterable {
    /** Returns the number of items currently in the sack. */
    int count();

    /** Returns true if there are no items currently in the sack. */
    boolean isEmpty();

    /** Removes all the items from the sack. */
    void clear();

    /**
     * Potentially adds the specified item to the sack returning true if
     * actually added.
     * If the item is a duplicate and the sack doesn't allow duplicates,
     * the request to add will be silently ignored and false is returned.
     * If sack is ordered, the specified item will be appended to the end.
     */
    boolean add(String item);

    /**
     * Potentially adds each of the specified items to the sack.
     * Behaves as if {@link #add} is called for each item.
     * @return the number of items actually added
     */
    int addAll(String... items);

    /**
     * Potentially adds each of the items in the otherSack to this sack.
     * Behaves as if {@link #add} is called for each item.
     * @return the number of items actually added
     */
    int addAll(StringDSSack otherSack);

    /**
     * Potentially removes one occurrence of the specified item from
     * the sack returning true if actually found and removed.
     * Returns false if not found.
     * If the sack allows duplicates, this only removes one match.
     * If the sack is ordered, the first one found is removed.
     * To remove <em>all</em> items matching, use:
     * <pre>
     * removeMatches(new EqualToStringDSFilter(item)}
     * </pre>
     */
    boolean remove(String item);

    /**
     * Removes all the items which match the specified filter.
     * If the sack allows duplicates, duplicate matches are all removed.
     * @return all the removed items.
     */
    String[] removeAndReturnMatches(StringDSFilter filter);

    /**
     * Removes all the items which match the specified filter.
     * If the sack allows duplicates, duplicate matches are all removed.
     * @return the number of items removed.
     */
    int removeAndCountMatches(StringDSFilter filter);

    /** Removes all the items as an array. */
    String[] removeAll();

    /** Returns true if the specified item is found. */
    boolean contains(String item);

    /** Finds and returns all the items which match the specified filter. */
    String[] peekMatches(StringDSFilter filter);

    /** Returns the number items which match the specified filter. */
    int countMatches(StringDSFilter filter);

    /** Returns all the items as an array. */
    String[] peekAll();

    /** Performs the specified action on all the items. */
    void performOnAll(StringDSAction action);

    /**
     * Performs the specified action on items which match the specified filter.
     * @return the number of items matched
     */
    int performOnMatches(StringDSFilter filter, StringDSAction action);

    /**
     * Iterates though all the items in the sack, but the order of iteration
     * may vary from call to call (unless the sack is ordered).
     */
    @Override
    StringDSIterator createIterator();
}
