package com.abc.ds.hash.tests;

import com.abc.ds.hash.*;
import com.abc.ds.keyvalue.*;

/* deliberate package access */
class TestDSHashTableContainsKey extends TestDSHashTableBase {
    public TestDSHashTableContainsKey(DSHashTableFactory factory) {
        super("containsKey()", factory);
    }

    @Override
    protected void performTests() {
        testOneKey();
        testSeveralKeys();
    }

    private void testOneKey() {
        outln(" - one key -");
        DSHashTable<String, String> ht = createDS();
        DSKeyValuePair<String, String>[] pairs = getFruitPairs(5);
        insert(ht, pairs);

        outln("ht.containsKey(\"" + pairs[0].getKey() +
            "\")", ht.containsKey(pairs[0].getKey()), true);
    }

    private void testSeveralKeys() {
        outln(" - several keys -");
        DSHashTable<String, String> ht = createDS(7);
        DSKeyValuePair<String, String>[] pairs = getFruitPairs(15);
        insert(ht, pairs);

        for ( int i = 0; i < pairs.length; i += 3 ) {
            outln("ht.containsKey(\"" + pairs[i].getKey() +
                "\")", ht.containsKey(pairs[i].getKey()), true);
        }
    }
}
