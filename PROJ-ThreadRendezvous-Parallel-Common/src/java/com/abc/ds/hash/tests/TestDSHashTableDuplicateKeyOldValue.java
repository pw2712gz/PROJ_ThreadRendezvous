package com.abc.ds.hash.tests;

import com.abc.ds.hash.*;
import com.abc.ds.keyvalue.*;

/* deliberate package access */
class TestDSHashTableDuplicateKeyOldValue extends TestDSHashTableBase {
    public TestDSHashTableDuplicateKeyOldValue(DSHashTableFactory factory) {
        super("insert duplicate - old value return check", factory);
    }

    @Override
    protected void performTests() {
        testNonNull();
        testCorrectOldValueReturned();
    }

    private void testNonNull() {
        outln(" - non-null returned -");
        DSHashTable<String, String> ht = createDS();
        DSKeyValuePair<String, String>[] pairs = getFruitPairs(5);
        insert(ht, pairs);
        outln("checking count()", ht.count(), pairs.length);

        String duplicateKey = pairs[1].getKey();
        String newValue = "new value for dup key";

        outln("attepting to insert duplicate key, should return non-null...");
        outln("insert(\"" + duplicateKey + "\", \"" + newValue + "\") != null",
            ht.insert(duplicateKey, newValue) != null, true);
        outln("checking count() [should not have changed]",
            ht.count(), pairs.length);
    }

    private void testCorrectOldValueReturned() {
        outln(" - correct old value returned -");
        DSHashTable<String, String> ht = createDS();
        DSKeyValuePair<String, String>[] pairs = getFruitPairs(5);
        insert(ht, pairs);
        outln("checking count()", ht.count(), pairs.length);

        DSKeyValuePair<String, String> dupKeyPair = pairs[1];
        String duplicateKey = dupKeyPair.getKey();
        String newValue = "new value for dup key";
        String expectedOldValue = dupKeyPair.getValue();

        outln("attepting to insert duplicate key...");
        outln("insert(\"" + duplicateKey + "\", \"" + newValue + "\") returned",
            ht.insert(duplicateKey, newValue), expectedOldValue);

        outln("checking count() [should not have changed]",
            ht.count(), pairs.length);
    }
}
