package com.abc.ds.fifo.tests;

import com.abc.ds.fifo.*;

/* deliberate package access */
class TestDSUnboundedFifoBasicEmptyCount extends TestDSUnboundedFifoBase {
    public TestDSUnboundedFifoBasicEmptyCount(DSUnboundedFifoFactory factory) {
        super("empty and count tests", factory);
    }

    @Override
    protected void performTests() {
        testInitialState();
        testAddJustOne();
        testAddTwo();
        testAddFive();
    }

    private void testInitialState() {
        DSUnboundedFifo<String> fifo = createDS();
        outln("initial state: ");
        outln("checking isEmpty()", fifo.isEmpty(), true);
        outln("checking count()", fifo.count(), 0);
    }

    private void testAddJustOne() {
        outln(" - add just one -");
        DSUnboundedFifo<String> fifo = createDS();
        add(fifo, getFruits(1));
        outln("checking isEmpty()", fifo.isEmpty(), false);
        outln("checking count()", fifo.count(), 1);
    }

    private void testAddTwo() {
        outln(" - add two -");
        DSUnboundedFifo<String> fifo = createDS();
        add(fifo, getFruits(2));
        outln("checking isEmpty()", fifo.isEmpty(), false);
        outln("checking count()", fifo.count(), 2);
    }

    private void testAddFive() {
        outln(" - add five -");
        DSUnboundedFifo<String> fifo = createDS();
        add(fifo, getFruits(5));
        outln("checking isEmpty()", fifo.isEmpty(), false);
        outln("checking count()", fifo.count(), 5);
    }
}
