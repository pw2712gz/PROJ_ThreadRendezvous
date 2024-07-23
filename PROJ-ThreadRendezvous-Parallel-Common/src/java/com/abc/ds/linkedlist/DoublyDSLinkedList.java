package com.abc.ds.linkedlist;

import java.util.*;

import com.abc.ds.*;
import com.abc.ds.filter.*;
import com.abc.ds.iterator.*;
import com.programix.util.*;

public class DoublyDSLinkedList<T> implements DSLinkedList<T> {
    private final Class<T> itemType;
    private final Class<DSLinkedList.Node<T>> nodeType;
    private final DllNode<T> sentinel;
    private int count;

    public DoublyDSLinkedList(Class<T> itemType) {
        ObjectTools.paramNullCheck(itemType, "itemType");

        this.itemType = itemType;
        nodeType = createNodeType();

        sentinel = new DllNode<T>(this, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        count = 0;
    }

    @SuppressWarnings("unchecked")
    private Class<DSLinkedList.Node<T>> createNodeType() {
        return (Class<DSLinkedList.Node<T>>)
            DSTools.coerceClassType(DSLinkedList.Node.class);
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
        for ( DllNode<T> node = sentinel.next;
              node != sentinel; node = node.next ) {

            node.owner = null;
        }

        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        count = 0;
    }

    @Override
    public DSIterator<T> createIterator() {
        return new NextIterator<T>(sentinel);
    }

    @Override
    public DSIterator<T> createReverseIterator() {
        return new PrevIterator<T>(sentinel);
    }

    @Override
    public DSLinkedList.Node<T> insertFirst(T newPayload) {
        return sentinel.insertAfter(newPayload);
    }

    @Override
    public DSLinkedList.Node<T> insertLast(T newPayload) {
        return sentinel.insertBefore(newPayload);
    }

    @Override
    public DSLinkedList.Node<T> peekAtIndex(int index)
            throws IndexOutOfBoundsException {

        if ( index < 0 || index >= count ) {
            throw new IndexOutOfBoundsException("invalid index=" + index +
                (count == 0 ? ", empty list" :
                    ", must be in range 0.." + (count - 1)));
        }

        DllNode<T> node = sentinel;
        for ( int i = 0; i <= index; i++ ) {
            node = node.next;
        }
        return node;
    }

    @Override
    public DSLinkedList.Node<T> peekFirst() throws NoSuchElementException {
        if ( isEmpty() ) {
            throw new NoSuchElementException("list is empty");
        }
        return sentinel.next;
    }

    @Override
    public DSLinkedList.Node<T> peekLast() throws NoSuchElementException {
        if ( isEmpty() ) {
            throw new NoSuchElementException("list is empty");
        }
        return sentinel.prev;
    }

    @Override
    public DSLinkedList.Node<T>[] peekMatches(DSFilter<? super T> filter) {
        DSMatchHelper<DSLinkedList.Node<T>> matchHelper =
            DSMatchHelper.createMatchAndCount(nodeType, count / 4, 50);

        for ( DllNode<T> node = sentinel.next;
              node != sentinel;
              node = node.next ) {

            if ( filter.matches(node.getPayload()) ) {
                matchHelper.append(node);
            }
        }
        return matchHelper.getMatches();
    }

    @Override
    public DSLinkedList.Node<T>[] peekAll() {
        return peekMatches(new MatchEverythingDSFilter<T>());
    }

    @Override
    public T[] extractPayloads(DSLinkedList.Node<T>[] nodes) {
        T[] payloads = DSTools.createArrayFromType(itemType, nodes.length);
        for ( int i = 0; i < payloads.length; i++ ) {
            payloads[i] = nodes[i].getPayload();
        }
        return payloads;
    }

    private static class DllNode<T> implements Node<T> {
        public DoublyDSLinkedList<T> owner;
        public DllNode<T> next;
        public DllNode<T> prev;
        private T payload;

        public DllNode(DoublyDSLinkedList<T> owner, T payload) {
            ObjectTools.paramNullCheck(owner, "owner");
            this.owner = owner;
            this.payload = payload;
        }

        private void confirmStillInList() throws IllegalStateException {
            if ( owner == null ) {
                throw new IllegalStateException(
                    "operations cannot be done; node is no longer in a list");
            }
        }

        @Override
        public T getPayload() {
            return payload;
        }

        @Override
        public void setPayload(T newPayload) {
            this.payload = newPayload;
        }

        @Override
        public T delete() {
            confirmStillInList();
            prev.next = next;
            next.prev = prev;
            owner.count--;
            owner = null; // no longer in this list
            return payload;
        }

        @Override
        public DSLinkedList.Node<T> insertBefore(T newPayload) {
            confirmStillInList();
            DllNode<T> newNode = new DllNode<T>(owner, newPayload);
            newNode.prev = prev;
            newNode.next = this;
            newNode.prev.next = newNode;
            newNode.next.prev = newNode;
            owner.count++;
            return newNode;
        }

        @Override
        public DSLinkedList.Node<T> insertAfter(T newPayload) {
            return next.insertBefore(newPayload);
        }

        @Override
        public DSIterator<T> createIteratorThisToEnd() {
            confirmStillInList();
            return new NextIterator<T>(prev);
        }

        @Override
        public DSIterator<T> createIteratorNextToEnd() {
            confirmStillInList();
            return new NextIterator<T>(this);
        }

        @Override
        public DSIterator<T> createReverseIteratorThisToStart() {
            confirmStillInList();
            return new PrevIterator<T>(next);
        }

        @Override
        public DSIterator<T> createReverseIteratorPrevToStart() {
            confirmStillInList();
            return new PrevIterator<T>(this);
        }
    } // type DllNode

    private static class NextIterator<T> implements DSIterator<T> {
        private DllNode<T> node;

        public NextIterator(DllNode<T> node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node.next != node.owner.sentinel;
        }

        @Override
        public T next() throws NoSuchElementException {
            if ( !hasNext() ) {
                throw new NoSuchElementException(
                    "next() cannot be called because hasNext() is false");
            }

            node = node.next;
            return node.getPayload();
        }
    } // type NextIterator

    private static class PrevIterator<T> implements DSIterator<T> {
        private DllNode<T> node;

        public PrevIterator(DllNode<T> node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node.prev != node.owner.sentinel;
        }

        @Override
        public T next() throws NoSuchElementException {
            if ( !hasNext() ) {
                throw new NoSuchElementException(
                    "next() cannot be called because hasNext() is false");
            }

            node = node.prev;
            return node.getPayload();
        }
    } // type PrevIterator
}
