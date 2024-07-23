package com.abc.pp.fifo.tests;

import com.abc.pp.fifo.*;

/* deliberate package access */
class TestPPBoundedFifoClear extends TestPPBoundedFifoBase {
    public TestPPBoundedFifoClear(PPBoundedFifoFactory factory) {
        super("clear tests", factory);
    }

    @Override
    protected void performTests() {
        testAddThenClear();
        testAddThenClearThenAdd();
    }

    private void testAddThenClear() {
        outln(" - add, then clear -");
        PPBoundedFifo<String> fifo = createDS();
        outln("adding 5:");
        add(fifo, getFruits(0, 5));
        outln("checking getCount()", fifo.getCount(), 5);
        outln("clear()");
        fifo.clear();
        outln("checking isEmpty()", fifo.isEmpty(), true);
        outln("checking getCount()", fifo.getCount(), 0);
    }

    private void testAddThenClearThenAdd() {
        outln(" - add, then clear, then add -");
        PPBoundedFifo<String> fifo = createDS();
        outln("adding 20:");
        add(fifo, getFruits(0, 20));
        outln("checking getCount()", fifo.getCount(), 20);

        outln("clear()");
        fifo.clear();
        outln("checking getCount()", fifo.getCount(), 0);
        outln("checking isEmpty()", fifo.isEmpty(), true);

        outln("adding 6:");
        add(fifo, getFruits(20, 6));
        outln("checking getgetCount()", fifo.getCount(), 6);
        outln("checking isEmpty()", fifo.isEmpty(), false);
    }
}
