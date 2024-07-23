package com.abc.ds.fifo.tests.gui;

import com.abc.ds.fifo.tests.*;
import com.programix.testing.*;

public class GuiTestDSUnboundedFifo {
    public static void runTests(final String title,
                                final DSUnboundedFifoFactory factory) {

        ParallelTestingPane.createFramedInstance(new ParallelTestingPane.Control() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public TestChunk[] createNewTestChunks(TestThreadFactory threadFactory) {
                return TestSuiteDSUnboundedFifo.createAllTestChunks(factory);
            }

            @Override
            public boolean shouldShowPoints() {
                return false;
            }
        });
    }
}
