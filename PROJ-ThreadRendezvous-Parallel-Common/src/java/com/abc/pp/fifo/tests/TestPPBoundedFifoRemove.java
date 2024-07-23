package com.abc.pp.fifo.tests;

import com.abc.pp.fifo.*;

/* deliberate package access */
class TestPPBoundedFifoRemove extends TestPPBoundedFifoBase {
    public TestPPBoundedFifoRemove(PPBoundedFifoFactory factory) {
        super("remove()", factory);
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
        outln("remove()", fifo.remove(), "apple");
        outln("isEmpty()", fifo.isEmpty(), true);
    }

    private void testTwoItems() throws InterruptedException {
        outln(" - two items -");
        PPBoundedFifo<String> fifo = createDS();
        add(fifo, "apple");
        add(fifo, "banana");
        outln("remove()", fifo.remove(), "apple");
        outln("remove()", fifo.remove(), "banana");
        outln("isEmpty()", fifo.isEmpty(), true);
    }

    private void testSeveralItems() throws InterruptedException {
        outln(" - several items -");
        PPBoundedFifo<String> fifo = createDS();
        String[] fruits = getFruits(15);
        for ( String fruit : fruits ) {
            add(fifo, fruit);
        }
        for ( String fruit : fruits ) {
            outln("remove()", fifo.remove(), fruit);
        }
        outln("isEmpty()", fifo.isEmpty(), true);
    }
}
