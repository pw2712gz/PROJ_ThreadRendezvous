package com.abc.ds.set.tests.gui;

import com.abc.ds.set.tests.*;
import com.programix.testing.*;

public class GuiTestDSSet {
    public static void runTests(final String title,
                                final DSSetFactory factory) {

        ParallelTestingPane.createFramedInstance(new ParallelTestingPane.Control() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public TestChunk[] createNewTestChunks(TestThreadFactory threadFactory) {
                return TestSuiteDSSet.createAllTestChunks(factory);
            }

            @Override
            public boolean shouldShowPoints() {
                return false;
            }
        });
    }
}
