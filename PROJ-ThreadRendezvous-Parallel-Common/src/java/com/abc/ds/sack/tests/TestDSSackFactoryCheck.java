package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;

/* deliberate package access */
class TestDSSackFactoryCheck extends TestDSSackBase {
    public TestDSSackFactoryCheck(DSSackFactory factory) {
        super("factory check", factory);
    }

    @Override
    protected void performTests() {
        testCreate();
        testCreateWithCapacity();
    }

    private void testCreate() {
        outln("Creating a new DSSack with " +
            "factory.create(String.class)...");
        DSSack<String> sack = factory.create(String.class);
        outln("sack != null", sack != null, true);
    }

    private void testCreateWithCapacity() {
        outln("Creating a new DSSack with " +
            "factory.create(String.class, 2000, 10)...");
        DSSack<String> sack = factory.create(String.class, 2000, 10);
        outln("sack != null", sack != null, true);
    }
}
