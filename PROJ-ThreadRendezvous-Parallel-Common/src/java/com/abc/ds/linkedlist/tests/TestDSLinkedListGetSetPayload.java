package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;
import com.abc.ds.linkedlist.DSLinkedList.Node;

/* deliberate package access */
class TestDSLinkedListGetSetPayload extends TestDSLinkedListBase {
    public TestDSLinkedListGetSetPayload(DSLinkedListFactory factory) {
        super("DSLinkedList - get/set payload on Node", factory);
    }

    @Override
    protected void performTests() {
        testOneNode();
    }

    private void testOneNode() {
        outln(" - one node -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("insertLast(\"apple\")...");
        Node<String> node = ll.insertLast("apple");
        outln("node.getPayload()", node.getPayload(), "apple");
        outln("node.setPayload(\"banana\")...");
        node.setPayload("banana");
        outln("node.getPayload()", node.getPayload(), "banana");
    }
}
