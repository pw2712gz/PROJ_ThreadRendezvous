package com.abc.pp.fifo.tests;

import com.abc.pp.fifo.*;

/* deliberate package access */
class TestPPBoundedFifoBasicEmptyCount extends TestPPBoundedFifoBase {
    public TestPPBoundedFifoBasicEmptyCount(PPBoundedFifoFactory factory) {
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
        PPBoundedFifo<String> fifo = createDS();
        outln("initial state: ");
        outln("checking isEmpty()", fifo.isEmpty(), true);
        outln("checking getCount()", fifo.getCount(), 0);
    }

    private void testAddJustOne() {
        outln(" - add just one -");
        PPBoundedFifo<String> fifo = createDS();
        add(fifo, getFruits(1));
        outln("checking isEmpty()", fifo.isEmpty(), false);
        outln("checking getCount()", fifo.getCount(), 1);
    }

    private void testAddTwo() {
        outln(" - add two -");
        PPBoundedFifo<String> fifo = createDS();
        add(fifo, getFruits(2));
        outln("checking isEmpty()", fifo.isEmpty(), false);
        outln("checking getCount()", fifo.getCount(), 2);
    }

    private void testAddFive() {
        outln(" - add five -");
        PPBoundedFifo<String> fifo = createDS();
        add(fifo, getFruits(5));
        outln("checking isEmpty()", fifo.isEmpty(), false);
        outln("checking getCount()", fifo.getCount(), 5);
    }
}
