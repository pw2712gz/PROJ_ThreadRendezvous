package com.abc.pp.stringhandoff.tests;

import com.programix.testing.*;
import com.programix.util.*;

public class TestSuitePPStringHandoff {
    // no instances
    private TestSuitePPStringHandoff() {
    }

    public static BaseTest[] createAllTests(StringHandoffFactory dsFactory,
                                            TestThreadFactory threadFactory) {

        ObjectTools.paramNullCheck(dsFactory, "factory");

        return new BaseTest[] {
            new TestPPStringHandoffFactoryCheck(dsFactory),
            new TestPPStringHandoffLockObjectCheck(dsFactory),
            new TestPPStringHandoffInterruptCheck(dsFactory, threadFactory),
            new TestPPStringHandoffSinglePassReceive(dsFactory, threadFactory),
            new TestPPStringHandoffMultiPassReceive(dsFactory, threadFactory),
            new TestPPStringHandoffPassWithPlentyOfTime(dsFactory, threadFactory),
            new TestPPStringHandoffReceiveWithPlentyOfTime(dsFactory, threadFactory),
            new TestPPStringHandoffPassExpectTimeout(dsFactory, threadFactory),
            new TestPPStringHandoffReceiveExpectTimeout(dsFactory, threadFactory),
            new TestPPStringHandoffTwoPassers(dsFactory, threadFactory),
            new TestPPStringHandoffTwoReceivers(dsFactory, threadFactory),
            new TestPPStringHandoffShutdownWhileIdle(dsFactory, threadFactory),
            new TestPPStringHandoffShutdownWhilePassing(dsFactory, threadFactory),
            new TestPPStringHandoffShutdownWhileReceiving(dsFactory, threadFactory),
            new TestPPStringHandoffAdvNullHandoff(dsFactory, threadFactory),
            new TestPPStringHandoffAdvLockHogSecondPasser(dsFactory, threadFactory),
            new TestPPStringHandoffAdvInterruptedBeforeCallingPass(dsFactory, threadFactory),
            new TestPPStringHandoffAdvPassSuccessWithNegativeTimeout(dsFactory, threadFactory),
        };
    }

    public static TestChunk[] createAllTestChunks(StringHandoffFactory dsFactory,
                                                  TestThreadFactory threadFactory) {

        return StandardTestChunk.createAll(createAllTests(dsFactory, threadFactory));
    }
}
