package com.abc.ds.list.string;

import java.util.*;

import com.abc.ds.iterator.*;
import com.abc.ds.sack.*;

/**
 * A sack of references to objects of type T
 * (always ordered and duplicates are allowed).
 * This {@link StringDSList} IS-A {@link DSSack}.
 */
public interface StringDSList<T> extends DSSack<T> {
    /** Used as an int value to indicate that nothing was found. */
    public static final int NOT_FOUND_INDEX = -1;

    /**
     * Appends the specified item to the end of the list and always returns true.
     */
    @Override
	boolean add(T item);

    /**
     * Insert a new item into the list at the specified index and shift
     * the following items (if any) towards the end of the list.
     * If index == count(), then the item is appended to the end of the list.
     *
     * @throws IndexOutOfBoundsException if index < 0 or index > count()
     */
    void insertBefore(int index, T item) throws IndexOutOfBoundsException;

    /**
     * Inserts a new item at the beginning of the list.
     */
    void insertFirst(T item);

    /**
     * Searches forward from the specified fromIndex of the list
     * for the next occurrence of the specified item and,
     * if found, returns the index where it was found.
     * If not found, then {@link #NOT_FOUND_INDEX} is returned.
     * If fromIndex is less than 0, is it silently increased to 0.
     * If fromIndex is greater than or equal to count, then
     * {@link #NOT_FOUND_INDEX} is returned.
     */
    int firstIndexOf(T item, int fromIndex);

    /**
     * Searches forward from the beginning of the list
     * for the first occurrence of the specified item and,
     * if found, returns the index where it was found.
     * If not found, then {@link #NOT_FOUND_INDEX} is returned.
     */
    int firstIndexOf(T item);

    /**
     * Searches backward from the specified fromIndex of the list
     * for the next occurrence of the specified item and,
     * if found, returns the index where it was found.
     * If not found, then {@link #NOT_FOUND_INDEX} is returned.
     * If fromIndex is less than 0, then {@link #NOT_FOUND_INDEX} is returned.
     * If fromIndex is greater than or equal to count, then it is silently
     * reduced to (count - 1).
     */
    int lastIndexOf(T item, int fromIndex);

    /**
     * Searches backward from the end of the list
     * for the first occurrence of the specified item and,
     * if found, returns the index where it was found.
     * If not found, then {@link #NOT_FOUND_INDEX} is returned.
     */
    int lastIndexOf(T item);

    /**
     * Remove the item at the specified index and shift the following items
     * (if any) towards the beginning of the list.
     *
     * @throws IndexOutOfBoundsException if index < 0 or index >= count()
     */
    T removeAtIndex(int index) throws IndexOutOfBoundsException;

    /**
     * Removes the first item in the list and returns the item.
     * @throws NoSuchElementException if currently empty, check
     * {@link #isEmpty()} first to avoid this exception.
     */
    T removeFirst() throws NoSuchElementException;

    /**
     * Removes the last item in the list and returns the item.
     * @throws NoSuchElementException if currently empty, check
     * {@link #isEmpty()} first to avoid this exception.
     */
    T removeLast() throws NoSuchElementException;

    /**
     * Returns the item found at the specified index.
     * @throws IndexOutOfBoundsException if index < 0 or index >= count()
     */
    int peekAtIndex(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the first item in the list, if not empty.
     * @throws NoSuchElementException if currently empty, check
     * {@link #isEmpty()} first to avoid this exception.
     */
    T peekFirst() throws NoSuchElementException;

    /**
     * Returns the last item in the list, if not empty.
     * @throws NoSuchElementException if currently empty, check
     * {@link #isEmpty()} first to avoid this exception.
     */
    T peekLast() throws NoSuchElementException;

    /**
     * Changes the item stored at the specified index.
     * @throws IndexOutOfBoundsException if index < 0 or index >= count()
     */
    void replaceAtLindex(int index, T replacementItem)
        throws IndexOutOfBoundsException;

    /**
     * Create a new instance of {@link DSIterator} for traversing
     * the items in order starting the beginning of the list.
     * Each call returns a new, independent iterator.
     * In general, any alterations to the data structure while iterating
     * render the iterator invalid.
     */
    @Override
    DSIterator<T> createIterator();

    /**
     * Create a new instance of {@link DSIterator} for traversing
     * the items in reverse-order starting the end of the list.
     * Each call returns a new, independent iterator.
     * In general, any alterations to the data structure while iterating
     * render the iterator invalid.
     */
    DSIterator<T> createReverseIterator();
}
