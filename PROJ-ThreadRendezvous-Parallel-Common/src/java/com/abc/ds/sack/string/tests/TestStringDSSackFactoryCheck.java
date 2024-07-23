package com.abc.ds.sack.string.tests;

import com.abc.ds.sack.string.*;

/* deliberate package access */
class TestStringDSSackFactoryCheck extends TestStringDSSackBase {
    public TestStringDSSackFactoryCheck(StringDSSackFactory factory) {
        super("factory check", factory);
    }

    @Override
    protected void performTests() {
        testCreate();
        testCreateWithCapacity();
    }

    private void testCreate() {
        outln("Creating a new StringDSSack with factory.create()...");
        StringDSSack sack = factory.create();
        outln("sack != null", sack != null, true);
    }

    private void testCreateWithCapacity() {
        outln("Creating a new StringDSSack with factory.create(2000, 10)...");
        StringDSSack sack = factory.create(2000, 10);
        outln("sack != null", sack != null, true);
    }
}
