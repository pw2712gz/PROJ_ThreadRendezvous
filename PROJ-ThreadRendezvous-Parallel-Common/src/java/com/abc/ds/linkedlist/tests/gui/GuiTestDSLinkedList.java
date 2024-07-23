package com.abc.ds.linkedlist.tests.gui;

import com.abc.ds.linkedlist.tests.*;
import com.programix.testing.*;

public class GuiTestDSLinkedList {
    public static void runTests(final String title,
                                final DSLinkedListFactory factory) {

        ParallelTestingPane.createFramedInstance(new ParallelTestingPane.Control() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public TestChunk[] createNewTestChunks(TestThreadFactory threadFactory) {
                return TestSuiteDSLinkedList.createAllTestChunks(factory);
            }

            @Override
            public boolean shouldShowPoints() {
                return false;
            }
        });
    }
}
