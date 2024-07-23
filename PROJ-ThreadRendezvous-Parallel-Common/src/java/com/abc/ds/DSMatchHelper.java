package com.abc.ds;

import com.abc.ds.filter.*;
import com.abc.ds.iterator.*;

public class DSMatchHelper<T> {
    private final Class<T> itemType;
    private T[] slots;
    private int count;
    private int percentToGrowCapacity;
    private final boolean countOnly;

    private DSMatchHelper(Class<T> itemType,
                          int initialCapacity,
                          int percentToGrowCapacity) {

        this.itemType = itemType;

        initialCapacity = Math.max(1, initialCapacity);
        if ( percentToGrowCapacity < 1 ) {
            throw new IllegalArgumentException("percentToGrowCapacity=" +
                percentToGrowCapacity + ", but must be at least 1");
        }
        this.percentToGrowCapacity = percentToGrowCapacity;

        slots = DSTools.createArrayFromType(itemType, initialCapacity);
        count = 0;
        countOnly = false;
    }

    private DSMatchHelper(Class<T> itemType) {
        this.itemType = itemType;
        slots = null;
        count = 0;
        countOnly = true;
    }

    public static <T> DSMatchHelper<T>
            createMatchAndCount(Class<T> itemType,
                                int initialCapacity,
                                int percentToGrowCapacity) {

        return new DSMatchHelper<>(
            itemType, initialCapacity, percentToGrowCapacity);
    }

    public static <T> DSMatchHelper<T> createCountOnly(Class<T> itemType) {
        return new DSMatchHelper<>(itemType);
    }

    public static DSMatchHelper<?> createCountOnly() {
        return new DSMatchHelper<>(null);
    }

    private void growSlotsIfNeeded(int additionalCount) {
        if ( count + additionalCount > slots.length ) {
            int newCapacity = Math.max(
                count + additionalCount,
                (slots.length * (100 + percentToGrowCapacity)) / 100);
            T[] newSlots = DSTools.createArrayFromType(itemType, newCapacity);
            System.arraycopy(slots, 0, newSlots, 0, count);
            slots = newSlots;
        }
    }

    public DSMatchHelper<T> append(T match) {
        if ( !countOnly ) {
            growSlotsIfNeeded(1);
            slots[count] = match;
        }
        count++;
        return this;
    }

    /**
     * Uses the specified iterator and appends all of the items which match the
     * specified filter.
     */
    public DSMatchHelper<T> append(DSIterator<T> iterator,
                                   DSFilter<T> filter) {
        while ( iterator.hasNext() ) {
            T item = iterator.next();
            if ( filter.matches(item) ) {
                append(item);
            }
        }
        return this;
    }

    /**
     * Creates and iterator and appends all of the items which match the
     * specified filter.
     */
    public DSMatchHelper<T> append(DSIterable<T> iterable,
                                   DSFilter<T> filter) {
        return append(iterable.createIterator(), filter);
    }

    public T[] getMatches() {
        if ( countOnly ) {
            throw new IllegalStateException("this is a count-only version, " +
                "match references are not available");
        }
        T[] matches = DSTools.createArrayFromType(itemType, count);
        System.arraycopy(slots, 0, matches, 0, count);
        return matches;
    }

    public int getMatchCount() {
        return count;
    }
}
