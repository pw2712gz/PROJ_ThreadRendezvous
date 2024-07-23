package com.abc.ds.linkedlist.misc;

import java.util.NoSuchElementException;

public interface StringSinglyLinkedList {
    /** Returns the number of items currently in the list. */
    int count();

    /** Returns true if {@link #count()} is currently 0. */
    boolean isEmpty();

    /** Removes all the items from the list. */
    void clear();

    Node getFirstNode() throws NoSuchElementException;
    Node getLastNode() throws NoSuchElementException;
    Node getNode(int index) throws IndexOutOfBoundsException;

    Node findFirstNodeMatching(String valueToMatch); // not found return null
    Node findLastNodeMatching(String valueToMatch); // not found return null
    int findFirstIndexMatching(String valueToMatch); // not found return -1
    int findLastIndexMatching(String valueToMatch); // not found return -1

    void insertBeforeFirst(String value);
    void insertAfterLast(String value);


	public static interface Node {
		String getValue();
		String setValue(String value);

		void remove();
		Node insertNodeBefore(String newValueForNewNode);
		Node insertNodeAfter(String newValueForNewNode);

		void swapPositionWith(Node otherNode);
		void moveToFirst();
		void moveToLast();
		void moveToBefore(Node nodeToBePlaceBefore);
		void moveToAfter(Node nodeToBePlaceAfter);

		boolean hasNext();
		Node getNext() throws NoSuchElementException;
		boolean hasPrev();
		Node getPrev() throws NoSuchElementException;


	} // type Node
}
