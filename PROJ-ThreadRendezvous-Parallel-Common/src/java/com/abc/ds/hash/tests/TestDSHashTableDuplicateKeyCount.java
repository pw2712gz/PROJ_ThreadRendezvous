package com.abc.ds.hash.tests;

import java.util.*;

import com.abc.ds.hash.*;
import com.abc.ds.keyvalue.*;

/* deliberate package access */
class TestDSHashTableDuplicateKeyCount extends TestDSHashTableBase {
    public TestDSHashTableDuplicateKeyCount(DSHashTableFactory factory) {
        super("insert duplicate - count() checks", factory);
    }

    @Override
    protected void performTests() {
        testOneDup();
        testMultipleDup();
    }

    private void testOneDup() {
        outln(" - one duplicate -");
        DSHashTable<String, String> ht = createDS();
        DSKeyValuePair<String, String>[] pairs = getFruitPairs(5);
        insert(ht, pairs);
        outln("checking count() before dup", ht.count(), pairs.length);
        String duplicateKey = pairs[2].getKey();
        String newValue = "new value for dup key";
        insert(ht, duplicateKey, newValue);
        outln("checking count() after dup", ht.count(), pairs.length);
    }

    private void testMultipleDup() {
        outln(" - multiple duplicates -");
        DSHashTable<String, String> ht = createDS();
        DSKeyValuePair<String, String>[] pairs = getFruitPairs(12);
        insert(ht, pairs);
        outln("checking count() before duplicates", ht.count(), pairs.length);

        List<DSKeyValuePair<String, String>> list =
            new ArrayList<>(Arrays.asList(pairs));
        Collections.shuffle(list, new Random(1234));
        for ( DSKeyValuePair<String, String> pair : list ) {
            insert(ht, pair.getKey(), "NewValue-" + pair.getValue());
        }
        outln("checking count() after adding duplicates", ht.count(), pairs.length);
    }
}
