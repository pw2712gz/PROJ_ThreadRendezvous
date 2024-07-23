package com.abc.ds.linkedlist.misc;

import com.abc.ds.filter.DSFilter;

public interface MinimalLinkedList<P> {
	int count();
	boolean isEmpty();
	void clear();

	Node<P> getAtIndex(int index);
	// convenience:
	Node<P> getFirst();
	Node<P> getLast();

	Node<P> insertFirst(P newPayload);
	Node<P> insertLast(P newPayload);


	Node<P>[] findMatchingNodes(DSFilter<P> filter);
	Node<P> findFirstMatchingNode(DSFilter<P> filter);
	Node<P> findLastMatchingNode(DSFilter<P> filter);

	NodeIterator<P> createIterator();
	NodeIterator<P> createDescendingIterator();


//	Node<P> insertBeforeNode(Node<P> existingNode, P newPayload);
//	Node<P> insertAtIndex(int index, P newPayload);
//	Node<P> appendAfterLast(P newPayload);
//
//	P deleteNode(Node<P> node);
//
//	// Convenience
//	P deleteFirst();
//	P deleteLast();
//	P deleteAtIndex(int index);

	public static interface Node<P> {
		P getPayload();
		void setPayload(P newPayload);

		Node<P> insertBefore(P newPayload);
		Node<P> insertAfter(P newPayload);
		P delete();
	}

	public static interface NodeIterator<P> {
		boolean hasNext();
		Node<P> next();
	}
}
