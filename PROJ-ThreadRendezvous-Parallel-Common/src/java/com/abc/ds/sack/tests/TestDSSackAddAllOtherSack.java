package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSackAddAllOtherSack extends TestDSSackBase {
    public TestDSSackAddAllOtherSack(DSSackFactory factory) {
        super("addAll(DSSack otherSack)", factory);
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

        DSSack<String> otherSack = createAltStringSack();
        otherSack.add("apple");
        outln("otherSack contents: " + formatCommaDelimited(otherSack.peekAll()));

        DSSack<String> ds = createDS();
        outln("addAll(otherSack)");
        ds.addAll(otherSack);
        checkPeekAll(ds, "apple");
    }

    private void testAddAllWithTwo() {
        outln(" - addAll() with two -");
        DSSack<String> otherSack = createAltStringSack();
        otherSack.add("apple");
        otherSack.add("banana");
        outln("otherSack contents: " + formatCommaDelimited(otherSack.peekAll()));

        DSSack<String> ds = createDS();
        outln("addAll(otherSack)");
        ds.addAll(otherSack);
        checkPeekAll(ds, "apple", "banana");
    }

    private void testaddAllWithSeveral() {
        outln(" - addAll() with several -");

        String[] fruits = getFruits(26);
        DSSack<String> otherSack = createAltStringSack();
        otherSack.addAll(fruits);
        outln("otherSack contents: " + formatCommaDelimited(otherSack.peekAll()));

        DSSack<String> ds = createDS();
        outln("addAll(otherSack)");
        ds.addAll(otherSack);
        checkPeekAll(ds, fruits);
    }

    private void testAddAllWithNone() {
        outln(" - addAll() with zero-length array -");
        DSSack<String> ds = createDS();
        DSSack<String> otherSack = createAltStringSack();
        outln("otherSack.count()", otherSack.count(), 0);
        outln("addAll(otherSack)");
        ds.addAll(otherSack);

        checkIsEmpty(ds, true);
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    private void testAddAllWithNull() {
        outln(" - addAll() with null -");
        DSSack<String> ds = createDS();
        DSSack<String> otherSack = null;
        outln("otherSack == null", otherSack == null);
        outln("addAll(otherSack)  // otherSack is null, should quietly do nothing");
        ds.addAll(otherSack);
        checkIsEmpty(ds, true);
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }
}
