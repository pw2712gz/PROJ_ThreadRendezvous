package com.abc.pp.fifo.tests;

import com.abc.pp.fifo.*;

/* deliberate package access */
class TestPPBoundedFifoRemoveAtLeastOne extends TestPPBoundedFifoBase {
    public TestPPBoundedFifoRemoveAtLeastOne(PPBoundedFifoFactory factory) {
        super("removeAtLeastOne()", factory);
    }

    @Override
    protected void performTests() {
        try {
            testOneItem();
            testTwoItems();
            testSeveralItems();
        } catch ( Exception x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    private void testOneItem() throws InterruptedException {
        outln(" - one item -");
        PPBoundedFifo<String> fifo = createDS();
        add(fifo, "apple");
        checkRemoveAtLeastOne(fifo, "apple");
        outln("isEmpty()", fifo.isEmpty(), true);
    }

    private void testTwoItems() throws InterruptedException {
        outln(" - two items -");
        PPBoundedFifo<String> fifo = createDS();
        add(fifo, "apple");
        add(fifo, "banana");
        checkRemoveAtLeastOne(fifo, "apple", "banana");
        outln("isEmpty()", fifo.isEmpty(), true);
    }

    private void testSeveralItems() throws InterruptedException {
        outln(" - several items -");
        PPBoundedFifo<String> fifo = createDS();
        String[] fruits = getFruits(15);
        for ( String fruit : fruits ) {
            add(fifo, fruit);
        }
        checkRemoveAtLeastOne(fifo, fruits);
        outln("isEmpty()", fifo.isEmpty(), true);
    }
}
