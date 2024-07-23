package com.abc.ds.set;

import com.abc.ds.*;
import com.abc.ds.action.*;
import com.abc.ds.filter.*;
import com.abc.ds.iterator.*;
import com.abc.ds.sack.*;
import com.programix.util.*;

public class ArrayDSSet<T> implements DSSet<T> {
    private final Class<T> itemType;
    private final int percentToGrowCapacity;
    private T[] slots;
    private int count;

    public ArrayDSSet(Class<T> itemType,
                      int initialCapacity,
                      int percentToGrowCapacity) {

        ObjectTools.paramNullCheck(itemType, "itemType");
        this.itemType = itemType;
        this.percentToGrowCapacity = Math.max(1, percentToGrowCapacity);

        slots = DSTools.createArrayFromType(itemType, initialCapacity);
        count = 0;
    }

    public ArrayDSSet(Class<T> itemType) {
        this(itemType, 100, 20);
    }

    private void growIfNeeded() {
        if ( count < slots.length ) {
            return;
        }

        int newCapacity = slots.length +
            Math.max(1, slots.length * percentToGrowCapacity / 100);

        T[] newSlots = DSTools.createArrayFromType(itemType, newCapacity);
        System.arraycopy(slots, 0, newSlots, 0, count);
        slots = newSlots;
    }


    @Override
    public Class<T> getItemType() {
        return itemType;
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
    public boolean add(T item) {
        if ( contains(item) ) {
            return false;
        }

        growIfNeeded();
        slots[count] = item;
        count++;
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int addAll(T... items) {
        if ( ObjectTools.isEmpty(items) ) {
            return 0;
        }

        int countBefore = count;
        for ( T item : items ) {
            add(item);
        }
        return count - countBefore;
    }

    @Override
    public int addAll(DSSack<T> otherSack) {
        if ( DSTools.isEmpty(otherSack) ) {
            return 0;
        }

        return addAll(otherSack.peekAll());
    }

    @Override
    public boolean remove(T item) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public int removeAndCountMatches(DSFilter<T> filter) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public T[] removeAndReturnMatches(DSFilter<T> filter) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public T[] removeAll() {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public boolean contains(T item) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public int countMatches(DSFilter<T> filter) {
        return peekMatches(filter).length;
    }

    @Override
    public T[] peekMatches(DSFilter<T> filter) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public T[] peekAll() {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public void performOnAll(DSAction<T> action) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public int performOnMatches(DSFilter<T> filter, DSAction<T> action) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public DSIterator<T> createIterator() {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public int union(DSSet<T> otherSet) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public void intersection(DSSet<T> otherSet) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public void subtract(DSSet<T> otherSet) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }
}
