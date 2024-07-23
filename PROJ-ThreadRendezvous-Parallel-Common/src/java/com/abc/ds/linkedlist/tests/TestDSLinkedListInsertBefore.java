package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;

/* deliberate package access */
class TestDSLinkedListInsertBefore extends TestDSLinkedListBase {
    public TestDSLinkedListInsertBefore(DSLinkedListFactory factory) {
        super("DSLinkedList - insertBefore()", factory);
    }

    @Override
    protected void performTests() {
        testInsertBeforeInTheMiddle();
        testInsertBeforeTheLast();
        testInsertBeforeTheFirst();
        testInsertBeforeTheOnly();
        testInsertBeforeMultipleTimesInTheMiddle();
        testInsertBeforeMultipleTimesTheFirst();
    }

    private void testInsertBeforeInTheMiddle() {
        outln(" - insertBefore() in the middle -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[1];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("targetNode.insertBefore(\"kiwi\")");
        targetNode.insertBefore("kiwi");
        confirmPeekAll(ll, "apple", "kiwi", "banana", "cherry");
    }

    private void testInsertBeforeTheLast() {
        outln(" - insertBefore() the last -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[2];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("targetNode.insertBefore(\"kiwi\")");
        targetNode.insertBefore("kiwi");
        confirmPeekAll(ll, "apple", "banana", "kiwi", "cherry");
    }

    private void testInsertBeforeTheFirst() {
        outln(" - insertBefore() the first -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[0];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("targetNode.insertBefore(\"kiwi\")");
        targetNode.insertBefore("kiwi");
        confirmPeekAll(ll, "kiwi", "apple", "banana", "cherry");
    }

    private void testInsertBeforeTheOnly() {
        outln(" - insertBefore() a list with just one -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[0];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("targetNode.insertBefore(\"kiwi\")");
        targetNode.insertBefore("kiwi");
        confirmPeekAll(ll, "kiwi", "apple");
    }

    private void testInsertBeforeMultipleTimesInTheMiddle() {
        outln(" - insertBefore() multiple times in the middle -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[1];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("kiwiNode = targetNode.insertBefore(\"kiwi\")");
        DSLinkedList.Node<String> kiwiNode = targetNode.insertBefore("kiwi");
        outln("grapeNode = kiwiNode.insertBefore(\"grape\")");
        DSLinkedList.Node<String> grapeNode = kiwiNode.insertBefore("grape");
        outln("orangeNode = grapeNode.insertBefore(\"orange\")");
        DSLinkedList.Node<String> orangeNode = grapeNode.insertBefore("orange");
        outln("orangeNode.getPayload()", orangeNode.getPayload(), "orange");
        confirmPeekAll(ll, "apple", "orange", "grape", "kiwi", "banana", "cherry");
    }

    private void testInsertBeforeMultipleTimesTheFirst() {
        outln(" - insertBefore() multiple times before the first -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana", "cherry");
        DSLinkedList.Node<String> targetNode = ll.peekAll()[0];
        outln("targetNode.getPayload()=" + targetNode.getPayload());
        outln("kiwiNode = targetNode.insertBefore(\"kiwi\")");
        DSLinkedList.Node<String> kiwiNode = targetNode.insertBefore("kiwi");
        outln("grapeNode = kiwiNode.insertBefore(\"grape\")");
        DSLinkedList.Node<String> grapeNode = kiwiNode.insertBefore("grape");
        outln("orangeNode = grapeNode.insertBefore(\"orange\")");
        DSLinkedList.Node<String> orangeNode = grapeNode.insertBefore("orange");
        outln("orangeNode.getPayload()", orangeNode.getPayload(), "orange");
        confirmPeekAll(ll, "orange", "grape", "kiwi", "apple", "banana", "cherry");
    }
}
