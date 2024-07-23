package com.abc.ds.fifo.tests;

import com.abc.ds.fifo.*;

/* deliberate package access */
class TestDSUnboundedFifoClear extends TestDSUnboundedFifoBase {
    public TestDSUnboundedFifoClear(DSUnboundedFifoFactory factory) {
        super("clear tests", factory);
    }

    @Override
    protected void performTests() {
        testAddThenClear();
        testAddThenClearThenAdd();
    }

    private void testAddThenClear() {
        outln(" - add, then clear -");
        DSUnboundedFifo<String> fifo = createDS();
        outln("adding 5:");
        add(fifo, getFruits(0, 5));
        outln("checking count()", fifo.count(), 5);
        outln("clear()");
        fifo.clear();
        outln("checking isEmpty()", fifo.isEmpty(), true);
        outln("checking count()", fifo.count(), 0);
    }

    private void testAddThenClearThenAdd() {
        outln(" - add, then clear, then add -");
        DSUnboundedFifo<String> fifo = createDS();
        outln("adding 20:");
        add(fifo, getFruits(0, 20));
        outln("checking count()", fifo.count(), 20);

        outln("clear()");
        fifo.clear();
        outln("checking count()", fifo.count(), 0);
        outln("checking isEmpty()", fifo.isEmpty(), true);

        outln("adding 6:");
        add(fifo, getFruits(20, 6));
        outln("checking count()", fifo.count(), 6);
        outln("checking isEmpty()", fifo.isEmpty(), false);
    }
}
