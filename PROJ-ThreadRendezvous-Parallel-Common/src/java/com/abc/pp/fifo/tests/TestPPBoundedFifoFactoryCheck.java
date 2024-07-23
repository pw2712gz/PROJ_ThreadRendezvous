package com.abc.pp.fifo.tests;

import com.abc.pp.fifo.*;

public class TestPPBoundedFifoFactoryCheck extends TestPPBoundedFifoBase {
    public TestPPBoundedFifoFactoryCheck(PPBoundedFifoFactory factory) {
        super("factory check", factory);
    }

    @Override
    protected void performTests() {
        testCreate();
    }

    private void testCreate() {
        outln("Creating a new DSUnboundedFifo with " +
            "factory.create(String.class)...");
        PPBoundedFifo<String> fifo = factory.create(String.class, 10);
        outln("fifo != null", fifo != null, true);
    }
}
