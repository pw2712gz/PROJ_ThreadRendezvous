package com.abc.ds.hash.tests;

import com.abc.ds.hash.*;
import com.abc.ds.keyvalue.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSHashTablePeekValue extends TestDSHashTableBase {
    public TestDSHashTablePeekValue(DSHashTableFactory factory) {
        super("peekValue()", factory);
    }

    @Override
    protected void performTests() {
        testEmpty();
        testOneAndMatch();
        testOneAndMiss();
        testFiveAndMatch();
    }

    private void testEmpty() {
        outln(" - empty -");
        DSHashTable<String, String> ds = createDS();
        outln("peekValue(\"apple\")", ds.peekValue("apple"), null);
    }

    @SuppressWarnings("unchecked")
    private void testOneAndMatch() {
        outln(" - add one and peek for it -");
        DSHashTable<String, String> ds = createDS();
        DSKeyValuePair<String, String> singlePair = getFruitPairs(1)[0];
        insert(ds, singlePair);
        outln("peekValue(" + StringTools.quoteWrap(singlePair.getKey()) +
            ")", ds.peekValue(singlePair.getKey()), singlePair.getValue());
    }

    @SuppressWarnings("unchecked")
    private void testOneAndMiss() {
        outln(" - add one and peek for something else -");
        DSHashTable<String, String> ds = createDS();
        DSKeyValuePair<String, String> singlePair = getFruitPairs(1)[0];
        insert(ds, singlePair);
        String differentKey = "a-p-p-l-e-s";
        outln("peekValue(" + StringTools.quoteWrap(differentKey) +
            ")", ds.peekValue(differentKey), null);
    }

    private void testFiveAndMatch() {
        outln(" - add three and peek for one -");
        DSHashTable<String, String> ds = createDS();
        DSKeyValuePair<String, String>[] pairs = getFruitPairs(3);
        insert(ds, pairs);

        DSKeyValuePair<String, String> pairToLookFor = pairs[1];
        outln("peekValue(" + StringTools.quoteWrap(pairToLookFor.getKey()) +
            ")", ds.peekValue(pairToLookFor.getKey()), pairToLookFor.getValue());
    }
}
