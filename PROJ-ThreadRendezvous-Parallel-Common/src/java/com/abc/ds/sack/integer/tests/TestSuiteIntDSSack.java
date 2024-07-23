package com.abc.ds.sack.integer.tests;

import com.programix.testing.*;
import com.programix.util.*;

public class TestSuiteIntDSSack {
    // no instances
    private TestSuiteIntDSSack() {
    }

    public static BaseTest[] createAllTests(IntDSSackFactory factory) {
        ObjectTools.paramNullCheck(factory, "factory");

        return new BaseTest[] {
            new TestIntDSSackFactoryCheck(factory),
            new TestIntDSSackBasicEmptyCount(factory),
            new TestIntDSSackClear(factory),
            new TestIntDSSackContains(factory),
            new TestIntDSSackPeekAll(factory),
            new TestIntDSSackPeekMatches(factory),
            new TestIntDSSackCountMatches(factory),
            new TestIntDSSackIterator(factory),
            new TestIntDSSackAddAll(factory),
            new TestIntDSSackAddAllOtherSack(factory),
            new TestIntDSSackRemove(factory),
            new TestIntDSSackRemoveAndCountMatches(factory),
            new TestIntDSSackRemoveAndReturnMatches(factory),
            new TestIntDSSackRemoveAll(factory),
            new TestIntDSSackPerformOnMatches(factory),
            new TestIntDSSackPerformOnAll(factory),
        };
    }

    public static TestChunk[] createAllTestChunks(IntDSSackFactory factory) {
        return StandardTestChunk.createAll(createAllTests(factory));
    }
}
