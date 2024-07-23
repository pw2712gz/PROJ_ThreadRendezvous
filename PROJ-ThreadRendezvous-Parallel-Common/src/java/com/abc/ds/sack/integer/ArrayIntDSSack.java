package com.abc.ds.sack.integer;

import java.util.*;

import com.abc.ds.action.integer.*;
import com.abc.ds.filter.integer.*;
import com.abc.ds.iterator.integer.*;
import com.programix.util.*;

public class ArrayIntDSSack implements IntDSSack {
    private static final int NOT_FOUND_INDEX = -1;
    private static final int[] INT_ZERO_LEN_ARRAY = new int[0];

    private int[] slots;
    private int count;
    private final int percentToGrowCapacity;

    public ArrayIntDSSack(int initialCapacity,
                          int percentToGrowCapacity) {

        if ( percentToGrowCapacity < 1 ) {
            throw new IllegalArgumentException("percentToGrowCapacity=" +
                percentToGrowCapacity + ", but must be at least 1");
        }
        this.percentToGrowCapacity = percentToGrowCapacity;

        slots = new int[initialCapacity];
        count = 0;
    }

    public ArrayIntDSSack() {
        this(100, 20);
    }

    private void growSlotsIfNeeded(int additionalCount) {
        if ( count + additionalCount > slots.length ) {
            int newCapacity = Math.max(
                count + additionalCount,
                (slots.length * (100 + percentToGrowCapacity)) / 100);
            int[] newSlots = new int[newCapacity];
            System.arraycopy(slots, 0, newSlots, 0, count);
            slots = newSlots;
        }
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public void clear() {
        Arrays.fill(slots, 0);
        count = 0;
    }

    @Override
    public boolean add(int item) {
        growSlotsIfNeeded(1);
        slots[count] = item;
        count++;
        return true;
    }

    @Override
    public int addAll(int... items) {
        if ( items == null || items.length == 0 ) {
            return 0;
        }

        growSlotsIfNeeded(items.length);
        for ( int item : items ) {
            slots[count] = item;
            count++;
        }
        return items.length;
    }

    @Override
    public int addAll(IntDSSack otherSack) {
        if ( otherSack == null || otherSack.isEmpty() ) {
            return 0;
        }
        return addAll(otherSack.peekAll());
    }

    private int findFirstIndexOf(int item) {
        for ( int i = 0; i < count; i++ ) {
            if ( ObjectTools.isSame(item, slots[i]) ) {
                return i;
            }
        }
        return NOT_FOUND_INDEX;
    }

    @Override
    public boolean remove(int item) {
        int idx = findFirstIndexOf(item);
        if ( idx == NOT_FOUND_INDEX ) {
            return false;
        }
        for ( int i = idx; i < count - 1; i++ ) {
            slots[i] = slots[i + 1];
        }
        slots[count - 1] = 0;
        count--;
        return true;
    }

    @Override
    public int[] removeAndReturnMatches(IntDSFilter filter) {
        if ( isEmpty() ) {
            return INT_ZERO_LEN_ARRAY;
        }
        ItemList list = ItemList.createItemsAndCountInstance(count);
        removeMatchesCommon(filter, list);
        return list.toArray();
    }

    @Override
    public int removeAndCountMatches(IntDSFilter filter) {
        if ( isEmpty() ) {
            return 0;
        }
        ItemList list = ItemList.createOnlyKeepCountInstance();
        removeMatchesCommon(filter, list);
        return list.getCount();
    }

    private void removeMatchesCommon(IntDSFilter filter, ItemList list) {
        int dstIdx = 0;
        for ( int srcIdx = 0; srcIdx < count; srcIdx++ ) {
            int item = slots[srcIdx];
            if ( filter.matches(item) ) {
                list.append(item);
            } else {
                if ( dstIdx < srcIdx ) {
                    slots[dstIdx] = item;
                }
                dstIdx++;
            }
        }

        count -= list.getCount();
    }

    @Override
    public int[] removeAll() {
        int[] results = peekAll();
        clear();
        return results;
    }

    @Override
    public boolean contains(int item) {
        return findFirstIndexOf(item) != NOT_FOUND_INDEX;
    }

    @Override
    public int[] peekMatches(IntDSFilter filter) {
        if ( isEmpty() ) {
            return INT_ZERO_LEN_ARRAY;
        }
        ItemList list = ItemList.createItemsAndCountInstance(count);
        peekMatchesCommon(filter, list);
        return list.toArray();
    }

    @Override
    public int countMatches(IntDSFilter filter) {
        if ( isEmpty() ) {
            return 0;
        }
        ItemList list = ItemList.createOnlyKeepCountInstance();
        peekMatchesCommon(filter, list);
        return list.getCount();
    }

    private void peekMatchesCommon(IntDSFilter filter, ItemList list) {
        for ( int i = 0; i < count; i++ ) {
            if ( filter.matches(slots[i]) ) {
                list.append(slots[i]);
            }
        }
    }

    @Override
    public int[] peekAll() {
        int[] results = new int[count];
        for ( int i = 0; i < results.length; i++ ) {
            results[i] = slots[i];
        }
        return results;
    }

    @Override
    public void performOnAll(IntDSAction action) {
        // do this via peekAll() since we don't know if the action will
        // add or remove anything from this sack.
        for ( int item : peekAll() ) {
            action.perform(item);
        }
    }

    @Override
    public int performOnMatches(IntDSFilter filter, IntDSAction action) {
        // do this via peekMatches() since we don't know if the action will
        // add or remove anything from this sack.
        int[] matches = peekMatches(filter);
        for ( int item : matches ) {
            action.perform(item);
        }
        return matches.length;
    }

    @Override
    public IntDSIterator createIterator() {
        return new InternalIterator();
    }

    private class InternalIterator implements IntDSIterator {
        private int currentIndex;

        public InternalIterator() {
            currentIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return (currentIndex + 1) < count;
        }

        @Override
        public int next() throws NoSuchElementException {
            currentIndex++;
            return slots[currentIndex];
        }
    } // type InternalIterator

    private static class ItemList {
        private final boolean onlyKeepCount;
        private int[] slots;
        private int count;

        private ItemList(int initialCapacity) {
            onlyKeepCount = initialCapacity < 1;
            slots = onlyKeepCount ? null : new int[initialCapacity];
            count = 0;
        }

        public static ItemList createItemsAndCountInstance(int initialCapacity) {
            return new ItemList(initialCapacity);
        }

        public static ItemList createOnlyKeepCountInstance() {
            return new ItemList(-1);
        }

        public void append(int item) {
            if ( !onlyKeepCount ) {
                if ( count == slots.length ) {
                    int newCapacity =
                        slots.length + Math.max(1, slots.length * 120 / 100);
                    int[] newSlots = new int[newCapacity];
                    System.arraycopy(slots, 0, newSlots, 0, slots.length);
                    slots = newSlots;
                }
                slots[count] = item;
            }
            count++;
        }

        public int getCount() {
            return count;
        }

        public int[] toArray() {
            if ( onlyKeepCount ) {
                throw new IllegalStateException("this instance was only keeping count so there's no array to return");
            }
            if ( count == slots.length ) {
                return slots.clone();
            }
            int[] copy = new int[count];
            System.arraycopy(slots, 0, copy, 0, count);
            return copy;
        }
    } // type ItemList
}
