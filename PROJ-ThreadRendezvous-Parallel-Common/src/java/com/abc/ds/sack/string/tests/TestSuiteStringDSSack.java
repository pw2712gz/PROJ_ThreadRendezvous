package com.abc.ds.sack.string.tests;

import com.programix.testing.*;
import com.programix.util.*;

public class TestSuiteStringDSSack {
    // no instances
    private TestSuiteStringDSSack() {
    }

    public static BaseTest[] createAllTests(final StringDSSackFactory factory) {
        ObjectTools.paramNullCheck(factory, "factory");

        return new BaseTest[] {
            new TestStringDSSackFactoryCheck(factory),
            new TestStringDSSackBasicEmptyCount(factory),
            new TestStringDSSackClear(factory),
            new TestStringDSSackContains(factory),
            new TestStringDSSackPeekAll(factory),
            new TestStringDSSackPeekMatches(factory),
            new TestStringDSSackCountMatches(factory),
            new TestStringDSSackIterator(factory),
            new TestStringDSSackAddAll(factory),
            new TestStringDSSackAddAllOtherSack(factory),
            new TestStringDSSackRemove(factory),
            new TestStringDSSackRemoveAndCountMatches(factory),
            new TestStringDSSackRemoveAndReturnMatches(factory),
            new TestStringDSSackRemoveAll(factory),
            new TestStringDSSackPerformOnMatches(factory),
            new TestStringDSSackPerformOnAll(factory),
        };
    }

    public static TestChunk[] createAllTestChunks(StringDSSackFactory factory) {
        return StandardTestChunk.createAll(createAllTests(factory));
    }
}
