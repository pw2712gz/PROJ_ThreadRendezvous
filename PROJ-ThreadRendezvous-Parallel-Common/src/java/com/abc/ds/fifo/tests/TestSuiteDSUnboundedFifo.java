package com.abc.ds.fifo.tests;

import com.programix.testing.*;
import com.programix.util.*;

public class TestSuiteDSUnboundedFifo {
    // no instances
    private TestSuiteDSUnboundedFifo() {
    }

    public static BaseTest[] createAllTests(DSUnboundedFifoFactory factory) {
        ObjectTools.paramNullCheck(factory, "factory");

        return new BaseTest[] {
            new TestDSUnboundedFifoFactoryCheck(factory),
            new TestDSUnboundedFifoItemType(factory),
            new TestDSUnboundedFifoBasicEmptyCount(factory),
            new TestDSUnboundedFifoClear(factory),
            new TestDSUnboundedFifoPeek(factory),
            new TestDSUnboundedFifoPeekAll(factory),
            new TestDSUnboundedFifoRemove(factory),
            new TestDSUnboundedFifoRemoveAll(factory),
            new TestDSUnboundedFifoPeekThenRemove(factory),
            new TestDSUnboundedFifoMixedBag(factory),
        };
    }

    public static TestChunk[] createAllTestChunks(DSUnboundedFifoFactory factory) {
        return StandardTestChunk.createAll(createAllTests(factory));
    }
}
