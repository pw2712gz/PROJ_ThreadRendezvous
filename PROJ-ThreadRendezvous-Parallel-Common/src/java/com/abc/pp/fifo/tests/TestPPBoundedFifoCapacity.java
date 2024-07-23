package com.abc.pp.fifo.tests;

import com.abc.pp.fifo.*;

/* deliberate package access */
class TestPPBoundedFifoCapacity extends TestPPBoundedFifoBase {
    public TestPPBoundedFifoCapacity(PPBoundedFifoFactory factory) {
        super("capacity", factory);
    }

    @Override
    protected void performTests() {
        testGetCapacity();
        testAddUpToCapacity();
    }

    private void testGetCapacity() {
        outln(" - getCapacity() for different requested capacities -");
        int[] capacities = new int[] { 10, 1, 2, 25, 100, 1000 };

        for (int capacity : capacities) {
            PPBoundedFifo<String> ds = createDS(capacity);
            outln("getCapacity()", ds.getCapacity(), capacity);
        }
    }

    private void testAddUpToCapacity() {
        outln(" - add items up to the capacity (should not block) -");
        int[] capacities = new int[] { 10, 1, 2, 25 };

        for (int capacity : capacities) {
            PPBoundedFifo<String> ds = createDS(capacity);
            String[] fruits = getFruits(capacity);
            outln("requested capacity of " + capacity);
            addBulk(ds, fruits);
            outln("getCount()", ds.getCount(), fruits.length);
            outln("isEmpty()", ds.isEmpty(), false);
            outln("isFull()", ds.isFull(), true);
        }
    }
}
