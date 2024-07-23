package com.abc.ds.linkedlist.string;

import java.util.*;

/**
 * A singly-linked list with a head pointer, a tail pointer, and sentinels!
 */
public class SinglyLinkedList implements StringLinkedList {
    private final SllNode head;
    private final SllNode tail;
    private int count;

    public SinglyLinkedList() {
        head = new SllNode(null);
        tail = new SllNode(null);
        head.next = tail;
        count = 0;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public void clear() {
        head.next = tail;
        count = 0;
    }

    @Override
    public NodeIterator createIterator() {
        return new SllIterator();
    }

    @Override
    public Node insertFirst(String newPayload) {
        return head.insertAfter(newPayload);
    }

    @Override
    public Node insertLast(String newPayload) {
        return tail.insertBefore(newPayload);
    }

    @Override
    public Node getFirst() throws NoSuchElementException {
        if ( isEmpty() ) {
            throw new NoSuchElementException("list is empty");
        }
        return head.next;
    }

    @Override
    public Node getLast() throws NoSuchElementException {
        if ( isEmpty() ) {
            throw new NoSuchElementException("list is empty");
        }
        return getPrevNode(tail);
    }

    private SllNode getPrevNode(SllNode targetNode)
            throws IllegalArgumentException {

        if ( targetNode == head ) {
            throw new IllegalArgumentException(
                "there is no previous node to head");
        }

        SllNode ptr = head; // never null
        while ( ptr.next != targetNode ) {
            ptr = ptr.next;
            if ( ptr == null ) {
                // I'm on the tail sentinel node
                throw new IllegalArgumentException(
                    "targetNode is not in the list");
            }
        }

        return ptr;
    }

    private class SllNode implements StringLinkedList.Node {
        private String payload;
        private SllNode next;

        public SllNode(String payload) {
            this.payload = payload;
            next = null;
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
            SllNode prevNode = getPrevNode(this);
            prevNode.next = next;
            count--;
            return payload;
        }

        @Override
        public Node insertBefore(String newPayload) {
            return getPrevNode(this).insertAfter(newPayload);
        }

        @Override
        public Node insertAfter(String newPayload) {
            SllNode newNode = new SllNode(newPayload);
            newNode.next = next;
            next = newNode;
            count++;
            return newNode;
        }
    } // type SllNode

    private class SllIterator implements StringLinkedList.NodeIterator {
        private SllNode currentNode;

        public SllIterator() {
            currentNode = head;
        }

        @Override
        public boolean hasNext() {
            return currentNode.next != tail;
        }

        @Override
        public Node next() {
            if ( !hasNext() ) {
                throw new IllegalStateException(
                    "can't call next() when hasNext() returns false");
            }

            currentNode = currentNode.next;
            return currentNode;
        }
    } // type SllIterator
}
