package com.abc.ds.linkedlist.misc;

public interface SinglyLinkedList<P> {

	int count();
	boolean isEmpty();

//	void insertFirst(P payload);
	void appendLast(P payload);

	Node<P> getFirstNode();
//	Node<P> getLastNode();
//	Node<P> getNodeByIndex(int index);

//	Node<P> findNode(P payloadToMatch);





	public static interface Node<P> {
		P getPayload();
		P setPayload(P newPayload);

		boolean hasNext();
		Node<P> getNext();
//		void setNext(Node<P> next);

		void delete();

		Node<P> insertBefore(P payload);

	}
}
