package com.abc.ds.linkedlist.tests;

import com.programix.testing.*;
import com.programix.util.*;

public class TestSuiteDSLinkedList {
    // no instances
    private TestSuiteDSLinkedList() {
    }

    public static BaseTest[] createAllTests(DSLinkedListFactory factory) {
        ObjectTools.paramNullCheck(factory, "factory");

        return new BaseTest[] {
            new TestDSLinkedListFactoryCheck(factory),
            new TestDSLinkedListBasicEmptyCount(factory),
            new TestDSLinkedListClear(factory),
            new TestDSLinkedListGetSetPayload(factory),
            new TestDSLinkedListPeekFirstLast(factory),
            new TestDSLinkedListPeekAtIndex(factory),
            new TestDSLinkedListPeekAll(factory),
            new TestDSLinkedListPeekMatches(factory),
            new TestDSLinkedListExtractPayloads(factory),
            new TestDSLinkedListIterator(factory),
            new TestDSLinkedListReverseIterator(factory),
            new TestDSLinkedListInsertBefore(factory),
            new TestDSLinkedListInsertAfter(factory),
            new TestDSLinkedListInsertFirst(factory),
            new TestDSLinkedListInsertLast(factory),
        };
    }

    public static TestChunk[] createAllTestChunks(DSLinkedListFactory factory) {
        return StandardTestChunk.createAll(createAllTests(factory));
    }
}
