package com.abc.ds.linkedlist.string;

import java.util.*;

public interface StringLinkedList {
    boolean isEmpty();
    int count();
    void clear();
    
    NodeIterator createIterator();
    
    Node insertFirst(String newPayload);
    Node insertLast(String newPayload);
    
    /** 
     * Return the first node or throws {@link NoSuchElementException} if empty.
     */
    Node getFirst() throws NoSuchElementException;

    /** 
     * Return the last node or throws {@link NoSuchElementException} if empty.
     */
    Node getLast() throws NoSuchElementException;
    
    public static interface Node {
        String getPayload();
        void setPayload(String newPayload);
        
        /** Deletes the node and return the payload which was held within it */
        String delete();
        
        Node insertBefore(String newPayload);
        Node insertAfter(String newPayload);
    } // type Node
    
    public static interface NodeIterator {
        boolean hasNext();
        Node next();
    } // type NodeIterator
}
