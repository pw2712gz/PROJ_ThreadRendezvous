package com.abc.pp.fifo.tests;

import com.programix.testing.*;
import com.programix.util.*;

public class TestSuitePPBoundedFifo {
    // no instances
    private TestSuitePPBoundedFifo() {
    }

    public static BaseTest[] createAllTests(PPBoundedFifoFactory dsFactory,
                                            TestThreadFactory threadFactory) {

        ObjectTools.paramNullCheck(dsFactory, "factory");

        return new BaseTest[] {
            new TestPPBoundedFifoFactoryCheck(dsFactory),
            new TestPPBoundedFifoItemType(dsFactory),
            new TestPPBoundedFifoBasicEmptyCount(dsFactory),
            new TestPPBoundedFifoCapacity(dsFactory),
            new TestPPBoundedFifoClear(dsFactory),
            new TestPPBoundedFifoRemove(dsFactory),
            new TestPPBoundedFifoRemoveAtLeastOne(dsFactory),
            new TestPPBoundedFifoRemoveAll(dsFactory),
            new TestPPBoundedFifoAddAndRemoveMoreThanCapacity(dsFactory, threadFactory),
            new TestPPBoundedFifoAddAllUpToCapacity(dsFactory),
            new TestPPBoundedFifoWaitUntilEmpty(dsFactory, threadFactory),
        };
    }

    public static TestChunk[] createAllTestChunks(PPBoundedFifoFactory dsFactory,
                                                  TestThreadFactory threadFactory) {

        return StandardTestChunk.createAll(createAllTests(dsFactory, threadFactory));
    }
}
