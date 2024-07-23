package com.abc.ds.linkedlist.string;

import java.util.*;

/**
 * A doubly-linked list with a head pointer, a tail pointer, and sentinels.
 */
public class DoublyStringLinkedList implements StringLinkedList {
    private final DllNode head;
    private final DllNode tail;
    private int count;

    public DoublyStringLinkedList() {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public boolean isEmpty() {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public int count() {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public void clear() {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public NodeIterator createIterator() {
        throw new RuntimeException("not impl yet"); // FIXME
    }

    @Override
    public Node insertFirst(String newPayload) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public Node insertLast(String newPayload) {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public Node getFirst() throws NoSuchElementException {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    @Override
    public Node getLast() throws NoSuchElementException {
        throw new RuntimeException("not implemented yet"); // FIXME
    }

    private class DllNode implements StringLinkedList.Node {
        private String payload;
        private DllNode next;
        private DllNode prev;

        public DllNode(String payload) {
            this.payload = payload;
            next = null;
            prev = null;
        }

        @Override
        public String getPayload() {
            return payload;
        }

        @Override
        public void setPayload(String newPayload) {
            payload = newPayload;
        }

        @Override
        public String delete() {
            throw new RuntimeException("not implemented yet"); // FIXME
        }

        @Override
        public Node insertBefore(String newPayload) {
            throw new RuntimeException("not implemented yet"); // FIXME
        }

        @Override
        public Node insertAfter(String newPayload) {
            throw new RuntimeException("not implemented yet"); // FIXME
        }
    } // type DllNode

    private class DllIterator implements StringLinkedList.NodeIterator {

        public DllIterator() {
            throw new RuntimeException("not implemented yet"); // FIXME
        }

        @Override
        public boolean hasNext() {
            throw new RuntimeException("not implemented yet"); // FIXME
        }

        @Override
        public Node next() {
            throw new RuntimeException("not implemented yet"); // FIXME
        }
    } // type DllIterator
}
