package com.abc.pp.stringhandoff.tests.gui;

import com.abc.pp.stringhandoff.tests.*;
import com.programix.testing.*;

public class GuiTestPPStringHandoff {
    public static void runTests(final String title,
                                final boolean showPoints,
                                final StringHandoffFactory factory) {

        ParallelTestingPane.createFramedInstance(new ParallelTestingPane.Control() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public TestChunk[] createNewTestChunks(TestThreadFactory threadFactory) {
                return TestSuitePPStringHandoff.createAllTestChunks(factory, threadFactory);
            }

            @Override
            public boolean shouldShowPoints() {
                return showPoints;
            }
        });
    }
}
