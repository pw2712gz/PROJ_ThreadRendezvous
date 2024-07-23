package com.abc.ds.linkedlist.misc;

public interface StandardLinkedList<P> {
	int count();
	boolean isEmpty();

	Node<P> getFirst();
	Node<P> getLast();
	Node<P> getAtIndex(int index);

	Node<P> insertBeforeFirst(P newPayload);
	Node<P> insertBeforeNodeAtIndex(int index, P newPayload);
	Node<P> insertBeforeNode(Node<P> existingNode, P newPayload);
	Node<P> appendAfterLast(P newPayload);

	P deleteNode(Node<P> node);

	// Convenience
	P deleteFirst();
	P deleteLast();
	P deleteAtIndex(int index);

	public static interface Node<P> {
		P getPayload();
		void setPayload(P newPayload);

		void delete();
	}

	public static interface NodeIterator<P> {
		boolean hasNext();
		Node<P> next();
		void delete();
	}
}
