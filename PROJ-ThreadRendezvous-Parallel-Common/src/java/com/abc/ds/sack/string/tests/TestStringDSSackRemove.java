package com.abc.ds.sack.string.tests;

import java.util.*;

import com.abc.ds.sack.string.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestStringDSSackRemove extends TestStringDSSackBase {
    public TestStringDSSackRemove(StringDSSackFactory factory) {
        super("remove()", factory);
    }

    @Override
    protected void performTests() {
        testRemoveOnEmpty();
        testRemoveOnOne();
        testRemoveOnTwo();
        testRemoveAllThenAddThenRemove();
        testRemoveOnSeveral();
    }

    private void testRemoveOnEmpty() {
        outln(" - remove() on empty -");
        StringDSSack ds = createDS();
        outln("remove(\"apple\")", ds.remove("apple"), false);
        checkCount(ds, 0);
    }

    private void testRemoveOnOne() {
        outln(" - remove() on one -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        outln("remove(\"apple\")", ds.remove("apple"), true);
        checkCount(ds, 0);
    }

    private void testRemoveOnTwo() {
        outln(" - remove() one out of two -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        outln("remove(\"apple\")", ds.remove("apple"), true);
        checkCount(ds, 1);
    }

    private void testRemoveAllThenAddThenRemove() {
        outln(" - remove() all, then add, then remove some -");
        StringDSSack ds = createDS();

        String[] firstFruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_12).nextRandom(20);
        add(ds, firstFruits);

        String[] firstExpected =
            testHelper.potentiallyRemoveDuplicatesAndShuffle(firstFruits);
        checkCount(ds, firstExpected.length);

        for ( String item : firstExpected ) {
            outln("remove(" + StringTools.quoteWrap(item) + ")",
                ds.remove(item), true);
        }
        checkCount(ds, 0);

        outln("Should be empty again, now make sure it still works");
        String[] secondFruits = getFruits(5);
        add(ds, secondFruits);
        checkCount(ds, 5);

        for ( String item : secondFruits ) {
            outln("remove(" + StringTools.quoteWrap(item) + ")",
                ds.remove(item), true);
        }
        checkCount(ds, 0);
    }

    private void testRemoveOnSeveral() {
        outln(" - remove() on several -");
        StringDSSack ds = createDS();

        String[] fruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, fruits);

        Collection<String> collection = null;
        if ( allowDuplicates ) {
            outln("note: duplicates ARE allowed");
            collection = new ArrayList<>(Arrays.asList(fruits));
        } else {
            outln("note: NO duplicates");
            collection = new LinkedHashSet<>(Arrays.asList(fruits));
        }

        while ( collection.contains("mango") ) {
            collection.remove("mango");
            outln("remove(\"mango\")", ds.remove("mango"), true);
        }
        outln("remove(\"mango\")", ds.remove("mango"), false);

        outln("remove(\"strawberry\")", ds.remove("strawberry"), true);
        collection.remove("strawberry");

        if ( allowDuplicates ) {
            outln("remove(\"strawberry\")", ds.remove("strawberry"), true);
            collection.remove("strawberry");
        }

        outln("remove(\"vanilla\")", ds.remove("vanilla"), true);
        collection.remove("vanilla");

        outln("remove(\"banana\")", ds.remove("banana"), true);
        collection.remove("banana");

        outln("remove(\"apple\")", ds.remove("apple"), true);
        collection.remove("apple");

        outln("remove(\"lemon\")", ds.remove("lemon"), false);

        outln("remove(\"pear\")", ds.remove("pear"), false);

        outln("remove(\"honeydew\")", ds.remove("honeydew"), false);

        checkPeekAll(ds, StringTools.toArray(collection));
    }
}
