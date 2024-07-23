package com.abc.ds.sack.tests;

import com.programix.testing.*;
import com.programix.util.*;

public class TestSuiteDSSack {
    // no instances
    private TestSuiteDSSack() {
    }

    public static BaseTest[] createAllTests(DSSackFactory factory) {
        ObjectTools.paramNullCheck(factory, "factory");

        return new BaseTest[] {
            new TestDSSackFactoryCheck(factory),
            new TestDSSackItemType(factory),
            new TestDSSackBasicEmptyCount(factory),
            new TestDSSackClear(factory),
            new TestDSSackContains(factory),
            new TestDSSackPeekAll(factory),
            new TestDSSackPeekMatches(factory),
            new TestDSSackCountMatches(factory),
            new TestDSSackIterator(factory),
            new TestDSSackAddAll(factory),
            new TestDSSackAddAllOtherSack(factory),
            new TestDSSackRemove(factory),
            new TestDSSackRemoveAndCountMatches(factory),
            new TestDSSackRemoveAndReturnMatches(factory),
            new TestDSSackRemoveAll(factory),
            new TestDSSackPerformOnMatches(factory),
            new TestDSSackPerformOnAll(factory),
        };
    }

    public static TestChunk[] createAllTestChunks(DSSackFactory factory) {
        return StandardTestChunk.createAll(createAllTests(factory));
    }
}
