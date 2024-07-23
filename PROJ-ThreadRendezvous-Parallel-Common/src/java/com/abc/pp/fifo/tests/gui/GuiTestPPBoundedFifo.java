package com.abc.pp.fifo.tests.gui;

import com.abc.pp.fifo.tests.*;
import com.programix.testing.*;

public class GuiTestPPBoundedFifo {
    public static void runTests(final String title,
                                final PPBoundedFifoFactory factory) {

        ParallelTestingPane.createFramedInstance(new ParallelTestingPane.Control() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public TestChunk[] createNewTestChunks(TestThreadFactory threadFactory) {
                return TestSuitePPBoundedFifo.createAllTestChunks(factory, threadFactory);
            }

            @Override
            public boolean shouldShowPoints() {
                return false;
            }
        });
    }
}
