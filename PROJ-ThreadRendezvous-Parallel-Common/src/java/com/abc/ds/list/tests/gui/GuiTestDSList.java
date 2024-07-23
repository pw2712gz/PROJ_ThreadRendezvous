package com.abc.ds.list.tests.gui;

import com.abc.ds.list.tests.*;
import com.programix.testing.*;

public class GuiTestDSList {
    public static void runTests(final String title,
                                final DSListFactory factory) {

        ParallelTestingPane.createFramedInstance(new ParallelTestingPane.Control() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public TestChunk[] createNewTestChunks(TestThreadFactory threadFactory) {
                return TestSuiteDSList.createAllTestChunks(factory);
            }

            @Override
            public boolean shouldShowPoints() {
                return false;
            }
        });
    }
}
