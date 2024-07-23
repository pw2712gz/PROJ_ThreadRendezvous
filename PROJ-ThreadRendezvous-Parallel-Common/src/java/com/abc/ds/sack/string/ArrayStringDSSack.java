package com.abc.ds.sack.string;

import java.util.*;

import com.abc.ds.action.string.*;
import com.abc.ds.filter.string.*;
import com.abc.ds.iterator.string.*;
import com.programix.util.*;

public class ArrayStringDSSack implements StringDSSack {
    private static final int NOT_FOUND_INDEX = -1;

    private String[] slots;
    private int count;
    private final int percentToGrowCapacity;

    public ArrayStringDSSack(int initialCapacity,
                             int percentToGrowCapacity) {

        initialCapacity = Math.max(1, initialCapacity);
        if ( percentToGrowCapacity < 1 ) {
            throw new IllegalArgumentException("percentToGrowCapacity=" +
                percentToGrowCapacity + ", but must be at least 1");
        }
        this.percentToGrowCapacity = percentToGrowCapacity;

        slots = new String[initialCapacity];
        count = 0;
    }

    public ArrayStringDSSack() {
        this(100, 20);
    }

    private void growSlotsIfNeeded(int additionalCount) {
        if ( count + additionalCount > slots.length ) {
            int newCapacity = Math.max(
                count + additionalCount,
                (slots.length * (100 + percentToGrowCapacity)) / 100);
            String[] newSlots = new String[newCapacity];
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
        for ( int i = 0; i < count; i++ ) {
            slots[i] = null;
        }
        count = 0;
    }

    @Override
    public boolean add(String item) {
        growSlotsIfNeeded(1);
        slots[count] = item;
        count++;
        return true;
    }

    @Override
    public int addAll(String... items) {
        if ( ObjectTools.isEmpty(items) ) {
            return 0;
        }
        growSlotsIfNeeded(items.length);
        for ( String item : items ) {
            slots[count] = item;
            count++;
        }
        return items.length;
    }

    @Override
    public int addAll(StringDSSack otherSack) {
        if ( otherSack == null || otherSack.count() == 0 ) {
            return 0;
        }
        return addAll(otherSack.peekAll());
    }

    private int findFirstMatchIndex(StringDSFilter filter) {
        for ( int i = 0; i < count; i++ ) {
            if ( filter.matches(slots[i]) ) {
                return i;
            }
        }
        return NOT_FOUND_INDEX;
    }

    private int findFirstIndexOf(String item) {
        return findFirstMatchIndex(new EqualToStringDSFilter(item));
    }

    @Override
    public boolean remove(String item) {
        int idx = findFirstIndexOf(item);
        if ( idx == NOT_FOUND_INDEX ) {
            return false;
        }
        for ( int i = idx; i < count - 1; i++ ) {
            slots[i] = slots[i + 1];
        }
        slots[count - 1] = null; // don't inhibit garbage collection
        count--;
        return true;
    }

    @Override
    public String[] removeAndReturnMatches(StringDSFilter filter) {
        if ( isEmpty() ) {
            return StringTools.ZERO_LEN_ARRAY;
        }
        ItemList list = ItemList.createItemsAndCountInstance(count);
        removeMatchesCommon(filter, list);
        return list.toArray();
    }

    @Override
    public int removeAndCountMatches(StringDSFilter filter) {
        if ( isEmpty() ) {
            return 0;
        }
        ItemList list = ItemList.createOnlyKeepCountInstance();
        removeMatchesCommon(filter, list);
        return list.getCount();
    }

    private void removeMatchesCommon(StringDSFilter filter, ItemList list) {
        int dstIdx = 0;
        for ( int srcIdx = 0; srcIdx < count; srcIdx++ ) {
            String item = slots[srcIdx];
            if ( filter.matches(item) ) {
                list.append(item);
            } else {
                if ( dstIdx < srcIdx ) {
                    slots[dstIdx] = item;
                }
                dstIdx++;
            }
        }

        for ( ; dstIdx < count; dstIdx++ ) {
            slots[dstIdx] = null; // don't inhibit garbage collection
        }

        count -= list.getCount();
    }

    @Override
    public String[] removeAll() {
        String[] results = peekAll();
        clear();
        return results;
    }

    @Override
    public boolean contains(String item) {
        return findFirstIndexOf(item) != NOT_FOUND_INDEX;
    }

    @Override
    public String[] peekMatches(StringDSFilter filter) {
        if ( isEmpty() ) {
            return StringTools.ZERO_LEN_ARRAY;
        }
        ItemList list = ItemList.createItemsAndCountInstance(count);
        peekMatchesCommon(filter, list);
        return list.toArray();
    }

    @Override
    public int countMatches(StringDSFilter filter) {
        if ( isEmpty() ) {
            return 0;
        }
        ItemList list = ItemList.createOnlyKeepCountInstance();
        peekMatchesCommon(filter, list);
        return list.getCount();
    }

    private void peekMatchesCommon(StringDSFilter filter, ItemList list) {
        for ( int i = 0; i < count; i++ ) {
            if ( filter.matches(slots[i]) ) {
                list.append(slots[i]);
            }
        }
    }

    @Override
    public String[] peekAll() {
        if ( isEmpty() ) {
            return StringTools.ZERO_LEN_ARRAY;
        }
        String[] results = new String[count];
        System.arraycopy(slots, 0, results, 0, count);
        return results;
    }

    @Override
    public void performOnAll(StringDSAction action) {
        // do this via peekAll() since we don't know if the action will
        // add or remove anything from this sack.
        for ( String item : peekAll() ) {
            action.perform(item);
        }
    }

    @Override
    public int performOnMatches(StringDSFilter filter, StringDSAction action) {
        // do this via peekMatches() since we don't know if the action will
        // add or remove anything from this sack.
        String[] matches = peekMatches(filter);
        for ( String item : matches ) {
            action.perform(item);
        }
        return matches.length;
    }

    @Override
    public StringDSIterator createIterator() {
        return new InternalIterator();
    }

    private class InternalIterator implements StringDSIterator {
        private int currentIndex;

        public InternalIterator() {
            currentIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return (currentIndex + 1) < count;
        }

        @Override
        public String next() throws NoSuchElementException {
            currentIndex++;
            return slots[currentIndex];
        }
    } // type InternalIterator

    private static class ItemList {
        private final boolean onlyKeepCount;
        private String[] slots;
        private int count;

        private ItemList(int initialCapacity) {
            onlyKeepCount = initialCapacity < 1;
            slots = onlyKeepCount ? null : new String[initialCapacity];
            count = 0;
        }

        public static ItemList createItemsAndCountInstance(int initialCapacity) {
            return new ItemList(initialCapacity);
        }

        public static ItemList createOnlyKeepCountInstance() {
            return new ItemList(-1);
        }

        public void append(String item) {
            if ( !onlyKeepCount ) {
                if ( count == slots.length ) {
                    int newCapacity =
                        slots.length + Math.max(1, slots.length * 120 / 100);
                    String[] newSlots = new String[newCapacity];
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

        public String[] toArray() {
            if ( onlyKeepCount ) {
                throw new IllegalStateException("this instance was only keeping count so there's no array to return");
            }
            if ( count == slots.length ) {
                return slots.clone();
            }
            String[] copy = new String[count];
            System.arraycopy(slots, 0, copy, 0, count);
            return copy;
        }
    } // type ItemList
}
