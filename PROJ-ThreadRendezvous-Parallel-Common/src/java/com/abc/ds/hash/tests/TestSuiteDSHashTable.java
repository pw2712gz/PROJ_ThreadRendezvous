package com.abc.ds.hash.tests;

import com.programix.testing.*;
import com.programix.util.*;

public class TestSuiteDSHashTable {
    // no instances
    private TestSuiteDSHashTable() {
    }

    public static BaseTest[] createAllTests(DSHashTableFactory factory) {
        ObjectTools.paramNullCheck(factory, "factory");

        return new BaseTest[] {
            new TestDSHashTableFactoryCheck(factory),
            new TestDSHashTableKeyAndValueTypes(factory),
            new TestDSHashTableBasicEmptyCount(factory),
            new TestDSHashTableClear(factory),
            new TestDSHashTableContainsKey(factory),
            new TestDSHashTableDuplicateKeyCount(factory),
            new TestDSHashTableDuplicateKeyOldValue(factory),
            new TestDSHashTablePeekAll(factory),
            new TestDSHashTablePeekKeyMatches(factory),
            new TestDSHashTablePeekValue(factory),
        };
    }

    public static TestChunk[] createAllTestChunks(DSHashTableFactory factory) {
        return StandardTestChunk.createAll(createAllTests(factory));
    }
}
