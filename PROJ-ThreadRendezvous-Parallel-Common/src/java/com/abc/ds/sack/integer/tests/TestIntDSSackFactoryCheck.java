package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;

/* deliberate package access */
class TestIntDSSackFactoryCheck extends TestIntDSSackBase {
    public TestIntDSSackFactoryCheck(IntDSSackFactory factory) {
        super("factory check", factory);
    }

    @Override
    protected void performTests() {
        testCreate();
        testCreateWithCapacity();
    }

    private void testCreate() {
        outln("Creating a new IntDSSack with " +
            "factory.create()...");
        IntDSSack sack = factory.create();
        outln("sack != null", sack != null, true);
    }

    private void testCreateWithCapacity() {
        outln("Creating a new IntDSSack with " +
            "factory.create(2000, 10)...");
        IntDSSack sack = factory.create(2000, 10);
        outln("sack != null", sack != null, true);
    }
}
