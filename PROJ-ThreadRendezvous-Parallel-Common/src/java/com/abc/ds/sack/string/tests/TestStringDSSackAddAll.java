package com.abc.ds.sack.string.tests;

import com.abc.ds.sack.string.*;
import com.programix.util.*;

/* deliberate package access */
class TestStringDSSackAddAll extends TestStringDSSackBase {
    public TestStringDSSackAddAll(StringDSSackFactory factory) {
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
        StringDSSack ds = createDS();
        outln("addAll(\"apple\")");
        ds.addAll("apple");
        checkPeekAll(ds, "apple");
    }

    private void testAddAllWithTwo() {
        outln(" - addAll() with two -");
        StringDSSack ds = createDS();
        outln("addAll(\"apple\", \"banana\")");
        ds.addAll("apple", "banana");
        checkPeekAll(ds, "apple", "banana");
    }

    private void testaddAllWithSeveral() {
        outln(" - addAll() with several -");
        StringDSSack ds = createDS();

        String[] fruits = getFruits(26);
        outln("addAll(" + StringTools.formatCommaDelimited(fruits) + ")");
        ds.addAll(fruits);
        checkPeekAll(ds, fruits);
    }

    private void testAddAllWithNone() {
        outln(" - addAll() with zero-length array -");
        StringDSSack ds = createDS();
        outln("addAll(new String[0])");
        ds.addAll(new String[0]);
        checkIsEmpty(ds, true);
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    private void testAddAllWithNull() {
        outln(" - addAll() null -");
        StringDSSack ds = createDS();
        String[] items = null;
        outln("items == null", items == null);
        outln("addAll(items)  // items is null, should quietly do nothing");
        ds.addAll(items);
        checkIsEmpty(ds, true);
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }
}
