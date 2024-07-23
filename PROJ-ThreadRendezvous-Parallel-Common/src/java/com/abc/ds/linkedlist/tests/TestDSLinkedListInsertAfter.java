package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;

/* deliberate package access */
class TestDSLinkedListInsertAfter extends TestDSLinkedListBase {
    public TestDSLinkedListInsertAfter(DSLinkedListFactory factory) {
        super("DSLinkedList - insertAfter()", factory);
    }

    @Override
    protected void performTests() {
        testInsertAfterInTheMiddle();
        testInsertAfterTheLast();
        testInsertAfterTheFirst();
        testInsertAfterTheOnly();
        testInsertAfterMultipleTimesInTheMiddle();
        testInsertAfterMultipleTimesTheLast();
    }

    private void testInsertAfterInTheMiddle() {
        outln(" - insertAfter() in the middle -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[1];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("targetNode.insertAfter(\"kiwi\")");
        targetNode.insertAfter("kiwi");
        confirmPeekAll(ll, "apple", "banana", "kiwi", "cherry");
    }

    private void testInsertAfterTheLast() {
        outln(" - insertAfter() the last -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[2];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("targetNode.insertAfter(\"kiwi\")");
        targetNode.insertAfter("kiwi");
        confirmPeekAll(ll, "apple", "banana", "cherry", "kiwi");
    }

    private void testInsertAfterTheFirst() {
        outln(" - insertAfter() the first -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[0];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("targetNode.insertAfter(\"kiwi\")");
        targetNode.insertAfter("kiwi");
        confirmPeekAll(ll, "apple", "kiwi", "banana", "cherry");
    }

    private void testInsertAfterTheOnly() {
        outln(" - insertAfter() a list with just one -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[0];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("targetNode.insertAfter(\"kiwi\")");
        targetNode.insertAfter("kiwi");
        confirmPeekAll(ll, "apple", "kiwi");
    }

    private void testInsertAfterMultipleTimesInTheMiddle() {
        outln(" - insertAfter() multiple times in the middle -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[1];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("kiwiNode = targetNode.insertAfter(\"kiwi\")");
        DSLinkedList.Node<String> kiwiNode = targetNode.insertAfter("kiwi");
        outln("grapeNode = kiwiNode.insertAfter(\"grape\")");
        DSLinkedList.Node<String> grapeNode = kiwiNode.insertAfter("grape");
        outln("orangeNode = grapeNode.insertAfter(\"orange\")");
        DSLinkedList.Node<String> orangeNode = grapeNode.insertAfter("orange");
        outln("orangeNode.getPayload()", orangeNode.getPayload(), "orange");
        confirmPeekAll(ll, "apple", "banana", "kiwi", "grape", "orange", "cherry");
    }

    private void testInsertAfterMultipleTimesTheLast() {
        outln(" - insertAfter() multiple times after the last -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[2];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("kiwiNode = targetNode.insertAfter(\"kiwi\")");
        DSLinkedList.Node<String> kiwiNode = targetNode.insertAfter("kiwi");
        outln("grapeNode = kiwiNode.insertAfter(\"grape\")");
        DSLinkedList.Node<String> grapeNode = kiwiNode.insertAfter("grape");
        outln("orangeNode = grapeNode.insertAfter(\"orange\")");
        DSLinkedList.Node<String> orangeNode = grapeNode.insertAfter("orange");
        outln("orangeNode.getPayload()", orangeNode.getPayload(), "orange");
        confirmPeekAll(ll, "apple", "banana", "cherry", "kiwi", "grape", "orange");
    }
}
