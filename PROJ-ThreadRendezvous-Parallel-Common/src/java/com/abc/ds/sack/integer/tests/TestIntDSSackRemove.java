package com.abc.ds.sack.integer.tests;

import java.util.*;

import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestIntDSSackRemove extends TestIntDSSackBase {
    public TestIntDSSackRemove(IntDSSackFactory factory) {
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
        IntDSSack ds = createDS();
        outln("remove(444)", ds.remove(444), false);
        checkCount(ds, 0);
    }

    private void testRemoveOnOne() {
        outln(" - remove() one out of one -");
        IntDSSack ds = createDS();
        add(ds, 444);
        outln("remove(444)", ds.remove(444), true);
        checkCount(ds, 0);
    }

    private void testRemoveOnTwo() {
        outln(" - remove() one out of two -");
        IntDSSack ds = createDS();
        add(ds, 444);
        add(ds, 555);
        outln("remove(444)", ds.remove(444), true);
        checkCount(ds, 1);
    }

    private void testRemoveAllThenAddThenRemove() {
        outln(" - remove() all, then add, then remove some -");
        IntDSSack ds = createDS();

        int[] firstItems = new TestIntGenerator(
            TestIntGenerator.RANDOM_SEED_12).nextRandom(20);
        add(ds, firstItems);

        int[] firstItemsExpected = unwrap(
            testHelper.potentiallyRemoveDuplicatesAndShuffle(wrap(firstItems)));
        checkCount(ds, firstItemsExpected.length);

        for ( int item : firstItemsExpected ) {
            outln("remove(" + item + ")", ds.remove(item), true);
        }
        checkCount(ds, 0);

        outln("Should be empty again, now make sure it still works");
        int[] secondItems = getInts(5);
        add(ds, secondItems);
        checkCount(ds, 5);

        for ( int item : secondItems ) {
            outln("remove(" + item + ")", ds.remove(item), true);
        }
        checkCount(ds, 0);
    }

    private void testRemoveOnSeveral() {
        outln(" - remove() on several -");
        IntDSSack ds = createDS();

        int[] items = new TestIntGenerator(
            TestIntGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, items);

        Collection<Integer> collection = null;
        if ( allowDuplicates ) {
            outln("note: duplicates ARE allowed");
            collection = new ArrayList<>(Arrays.asList(wrap(items)));
        } else {
            outln("note: NO duplicates");
            collection = new LinkedHashSet<>(Arrays.asList(wrap(items)));
        }

        while ( collection.contains(230) ) {
            collection.remove(230);
            outln("remove(230)", ds.remove(230), true);
        }
        outln("remove(230)", ds.remove(230), false); // should be no more left

        outln("remove(290)", ds.remove(290), true);
        collection.remove(290);

        if ( allowDuplicates ) {
            outln("remove(290)", ds.remove(290), true);
            collection.remove(290);
        }

        outln("remove(320)", ds.remove(320), true);
        collection.remove(320);

        outln("remove(120)", ds.remove(120), true);
        collection.remove(120);

        outln("remove(110)", ds.remove(110), true);
        collection.remove(110);

        outln("remove(300)", ds.remove(300), false);
        outln("remove(220)", ds.remove(220), false);
        outln("remove(260)", ds.remove(260), false);
        outln("remove(180)", ds.remove(180), false);

        checkPeekAll(ds, unwrap(collection));
    }
}
