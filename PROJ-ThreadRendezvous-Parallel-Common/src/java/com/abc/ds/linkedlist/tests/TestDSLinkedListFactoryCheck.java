package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;

/* deliberate package access */
class TestDSLinkedListFactoryCheck extends TestDSLinkedListBase {
    public TestDSLinkedListFactoryCheck(DSLinkedListFactory factory) {
        super("DSLinkedList - factory check", factory);
    }

    @Override
    protected void performTests() {
        testCreate();
    }

    private void testCreate() {
        outln("Creating a new DSLinkedList with " +
            "factory.create(String.class)...");
        DSLinkedList<String> linkedList = factory.create(String.class);
        outln("linkedList != null", linkedList != null, true);
    }
}
