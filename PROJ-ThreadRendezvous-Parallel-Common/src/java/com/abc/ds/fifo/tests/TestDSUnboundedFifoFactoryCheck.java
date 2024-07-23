package com.abc.ds.fifo.tests;

import com.abc.ds.fifo.*;

/* deliberate package access */
class TestDSUnboundedFifoFactoryCheck extends TestDSUnboundedFifoBase {
    public TestDSUnboundedFifoFactoryCheck(DSUnboundedFifoFactory factory) {
        super("factory check", factory);
    }

    @Override
    protected void performTests() {
        testCreate();
    }

    private void testCreate() {
        outln("Creating a new DSUnboundedFifo with " +
            "factory.create(String.class)...");
        DSUnboundedFifo<String> fifo = factory.create(String.class);
        outln("fifo != null", fifo != null, true);
    }
}
