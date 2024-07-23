package com.abc.ds.linkedlist;

import java.util.*;

import com.abc.ds.filter.*;
import com.abc.ds.iterator.*;

public interface DSLinkedList<T> extends DSIterable<T> {
    /**
     * Returns the type of the items in this list.
     */
    Class<T> getItemType();

    int count();
    boolean isEmpty();
    void clear();

    @Override
    DSIterator<T> createIterator();

    DSIterator<T> createReverseIterator();

    DSLinkedList.Node<T> insertFirst(T newPayload);
    DSLinkedList.Node<T> insertLast(T newPayload);

    /** Return the node or {@link IndexOutOfBoundsException} if invalid. */
    DSLinkedList.Node<T> peekAtIndex(int index) throws IndexOutOfBoundsException;

    /** Return the first node or {@link NoSuchElementException} if empty. */
    DSLinkedList.Node<T> peekFirst() throws NoSuchElementException;

    /** Return the last node or {@link NoSuchElementException} if empty. */
    DSLinkedList.Node<T> peekLast() throws NoSuchElementException;

    /**
     * Finds and returns all the items which match the specified filter,
     * never null. If none match, a zero-length array is returned.
     */
    DSLinkedList.Node<T>[] peekMatches(DSFilter<? super T> filter);

    /**
     * Finds and returns all the items, never null.
     * If empty, a zero-length array is returned.
     */
    DSLinkedList.Node<T>[] peekAll();

    /**
     * Returns an array of the payloads found in the specified nodes, never null.
     * If nodes is null, or a zero-length array, then a zero-length array of
     * payloads is returned. If there's a null slot in the source array, the
     * result will have a null in the corresponding slot.
     */
    T[] extractPayloads(DSLinkedList.Node<T>[] nodes);

    public static interface Node<T> {
        T getPayload();
        void setPayload(T newPayload);

        /** Deletes the node and return the payload which was held within it */
        T delete();

        Node<T> insertBefore(T newPayload);
        Node<T> insertAfter(T newPayload);

        DSIterator<T> createIteratorThisToEnd();
        DSIterator<T> createIteratorNextToEnd();
        DSIterator<T> createReverseIteratorThisToStart();
        DSIterator<T> createReverseIteratorPrevToStart();
    } // type Node
}
