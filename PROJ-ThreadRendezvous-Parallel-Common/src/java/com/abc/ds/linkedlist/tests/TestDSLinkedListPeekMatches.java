package com.abc.ds.linkedlist.tests;

import com.abc.ds.filter.*;
import com.abc.ds.linkedlist.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSLinkedListPeekMatches extends TestDSLinkedListBase {
    private final DSFilter<String> lengthOfFiveFilter;

    public TestDSLinkedListPeekMatches(DSLinkedListFactory factory) {
        super("DSLinkedList - peekMatches()", factory);
        lengthOfFiveFilter = new DSFilter<String>() {
            @Override
            public boolean matches(String item) {
                return item != null && item.length() == 5;
            }
        };
    }

    @Override
    protected void performTests() {
        testPeekMatchesOnEmpty();
        testPeekMatchesOnTwo();
        testPeekMatchesOnSeveral();
    }

    private void testPeekMatchesOnEmpty() {
        outln(" - peekMatches() on empty -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("isEmpty()", ll.isEmpty(), true);

        confirmPeekAll(ll, StringTools.ZERO_LEN_ARRAY);

        outln("peekMatches(lengthOfFiveFilter)...");
        String[] results = extractPayloads(ll.peekMatches(lengthOfFiveFilter));
        checkArrayMatch(results, StringTools.ZERO_LEN_ARRAY);
    }

    private void testPeekMatchesOnTwo() {
        outln(" - peekMatches() on two item, one match -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        insertLast(ll, "banana");
        outln("peekMatches(lengthOfFiveFilter)...");
        String[] results = extractPayloads(ll.peekMatches(lengthOfFiveFilter));
        checkArrayMatch(results, new String[] { "apple" });
    }

    private void testPeekMatchesOnSeveral() {
        outln(" - peekMatches() on 20 items, 5 match -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, TestFruitGenerator.getFruits(20));
        outln("peekMatches(lengthOfFiveFilter)...");
        String[] results = extractPayloads(ll.peekMatches(lengthOfFiveFilter));
        checkArrayMatch(results, new String[] {
            "apple", "grape", "ilama", "lemon", "mango" });
    }
}
