package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSackAddAll extends TestDSSackBase {
    public TestDSSackAddAll(DSSackFactory factory) {
        super("addAll()", factory);
    }

    @Override
    protected void performTests() {
        testAddAllWithOne();
        testAddAllWithTwo();
        testaddAllWithSeveral();
        testAddAllWithNone();
        testAddAllWithNull();
    }

    private void testAddAllWithOne() {
        outln(" - addAll() with one -");
        DSSack<String> ds = createDS();
        outln("addAll(\"apple\")");
        ds.addAll("apple");
        checkPeekAll(ds, "apple");
    }

    private void testAddAllWithTwo() {
        outln(" - addAll() with two -");
        DSSack<String> ds = createDS();
        outln("addAll(\"apple\", \"banana\")");
        ds.addAll("apple", "banana");
        checkPeekAll(ds, "apple", "banana");
    }

    private void testaddAllWithSeveral() {
        outln(" - addAll() with several -");
        DSSack<String> ds = createDS();

        String[] fruits = getFruits(26);
        outln("addAll(" + StringTools.formatCommaDelimited(fruits) + ")");
        ds.addAll(fruits);
        checkPeekAll(ds, fruits);
    }

    private void testAddAllWithNone() {
        outln(" - addAll() with zero-length array -");
        DSSack<String> ds = createDS();
        outln("addAll(new String[0])");
        ds.addAll(new String[0]);
        checkIsEmpty(ds, true);
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    private void testAddAllWithNull() {
        outln(" - addAll() null -");
        DSSack<String> ds = createDS();
        String[] items = null;
        outln("items == null", items == null);
        outln("addAll(items)  // items is null, should quietly do nothing");
        ds.addAll(items);
        checkIsEmpty(ds, true);
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }
}
