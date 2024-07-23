package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSLinkedListExtractPayloads extends TestDSLinkedListBase {
    public TestDSLinkedListExtractPayloads(DSLinkedListFactory factory) {
        super("DSLinkedList - extractPayloads()", factory);
    }

    @Override
    protected void performTests() {
        testExtractPayloadsEmpty();
        testExtractPayloadsThree();
    }

    private void testExtractPayloadsEmpty() {
        outln(" - extractPayloads() on empty -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("isEmpty()", ll.isEmpty(), true);

        outln("extractPayloads()...");
        checkArrayMatch(
            ll.extractPayloads(ll.peekAll()), StringTools.ZERO_LEN_ARRAY);
    }

    private void testExtractPayloadsThree() {
        outln(" - extractPayloads() on three -");
        DSLinkedList<String> ll = createDSLinkedList();
        String[] fruits = TestFruitGenerator.getFruits(3);
        insertLast(ll, fruits);

        outln("extractPayloads()...");
        checkArrayMatch(
            ll.extractPayloads(ll.peekAll()), fruits);
    }
}
