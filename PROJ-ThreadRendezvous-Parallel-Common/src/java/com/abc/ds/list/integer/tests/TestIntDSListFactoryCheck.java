package com.abc.ds.list.integer.tests;

import com.abc.ds.list.integer.*;

/* deliberate package access */
class TestIntDSListFactoryCheck extends TestIntDSListBase {
    public TestIntDSListFactoryCheck(IntDSListFactory factory) {
        super("factory check", factory);
    }

    @Override
    protected void performTests() {
        testCreate();
        testCreateWithCapacity();
    }

    private void testCreate() {
        outln("Creating a new IntDSList with " +
            "factory.create()...");
        IntDSList list = factory.create();
        outln("list != null", list != null, true);
    }

    private void testCreateWithCapacity() {
        outln("Creating a new IntDSSack with " +
            "factory.create(2000, 10)...");
        IntDSList list = factory.create(2000, 10);
        outln("list != null", list != null, true);
    }
}
