package com.abc.ds.fifo.tests;

import com.abc.ds.fifo.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSUnboundedFifoPeekAll extends TestDSUnboundedFifoBase {
    public TestDSUnboundedFifoPeekAll(DSUnboundedFifoFactory factory) {
        super("peekAll()", factory);
    }

    @Override
    protected void performTests() {
        testPeekAllOnEmpty();
        testPeekAllOnOne();
        testPeekAllOnTwo();
        testPeekAllOnSeveral();
    }

    private void testPeekAllOnEmpty() {
        outln(" - peekAll() on empty -");
        DSUnboundedFifo<String> ds = createDS();
        outln("isEmpty()", ds.isEmpty(), true);

        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    private void testPeekAllOnOne() {
        outln(" - peekAll() on one -");
        DSUnboundedFifo<String> ds = createDS();
        add(ds, "apple");
        checkPeekAll(ds, "apple");
    }

    private void testPeekAllOnTwo() {
        outln(" - peekAll() on two -");
        DSUnboundedFifo<String> ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        checkPeekAll(ds, "apple", "banana");
    }

    private void testPeekAllOnSeveral() {
        outln(" - peekAll() on several -");
        DSUnboundedFifo<String> ds = createDS();
        outln("adding some junk to be cleared before peekAll() test...");
        add(ds, "JUNK A");
        add(ds, "JUNK B");
        add(ds, "JUNK C");
        outln("clear()...");
        ds.clear();

        String[] fruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, fruits);
        checkPeekAll(ds, fruits);
    }
}
