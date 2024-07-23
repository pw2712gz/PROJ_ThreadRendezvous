package com.abc.pp.fifo.tests;

import java.util.*;

import com.abc.ds.tests.*;
import com.abc.pp.fifo.*;
import com.programix.testing.*;
import com.programix.util.*;

/* deliberate package access */
class TestPPBoundedFifoWaitUntilEmpty extends TestPPBoundedFifoBase {
    public TestPPBoundedFifoWaitUntilEmpty(PPBoundedFifoFactory factory,
                                           TestThreadFactory threadFactory) {
        super("waitUntilEmpty", factory, threadFactory);
    }

    @Override
    protected void performTests() {
        testAddUpToCapacity();
    }

    private void testAddUpToCapacity() {
        try {
            outln(" - add some, then remove, then add more -");
            int[] capacities = new int[] { 10, 20 };

            TestWackyWaiter wackyWaiter = null;
            TestNastyNotifier nastyNotifier = null;
            for (int capacity : capacities) {
                try {
                    int extraCount = 3;
                    PPBoundedFifo<String> ds = createDS(capacity);
                    wackyWaiter = kickoffWackyWaiter(ds.getLockObject());
                    nastyNotifier = kickoffNastyNotifier(ds.getLockObject());

                    String[] fruitSetA = getFruits(capacity - 2);
                    String[] fruitSetB = getFruits(fruitSetA.length, 2);
                    String[] fruitSetC = getFruits(
                        fruitSetA.length + fruitSetB.length, extraCount);

                    int totalAddCount =
                        fruitSetA.length + fruitSetB.length + fruitSetC.length;
                    outln(String.format(
                        "Although capacity is only %d, " +
                        "we're adding a total of %d items " +
                        "(add %d, remove %d, add %d, add %d)",
                        capacity, totalAddCount, fruitSetA.length, extraCount,
                        fruitSetB.length, fruitSetC.length
                        ));

                    List<String> removeList = new ArrayList<>();

                    outln("requested capacity of " + capacity);
                    addBulk(ds, fruitSetA);
                    removeList.addAll(Arrays.asList(fruitSetA));
                    outln("getCount()", ds.getCount(), fruitSetA.length);

                    for (int i = 0; i < extraCount; i++) {
                        outln("remove()", ds.remove(), removeList.remove(0));
                    }
                    outln("getCount()", ds.getCount(), removeList.size());

                    for (String fruit : fruitSetB) {
                        add(ds, fruit);
                        removeList.add(fruit);
                    }
                    outln("getCount()", ds.getCount(), removeList.size());
                    outln("isEmpty()", ds.isEmpty(), false);
                    outln("isFull()", ds.isFull(), false);

                    for (String fruit : fruitSetC) {
                        add(ds, fruit);
                        removeList.add(fruit);
                    }
                    outln("getCount()", ds.getCount(), removeList.size());
                    outln("isEmpty()", ds.isEmpty(), false);
                    outln("isFull()", ds.isFull(), true);

                    checkRemoveAll(ds, removeList.toArray(StringTools.ZERO_LEN_ARRAY));
                    outln("isEmpty()", ds.isEmpty(), true);
                } finally {
                    if (wackyWaiter != null) {
                        wackyWaiter.stopRequest();
                        wackyWaiter.waitUntilDone(2000);
                    }
                    if (nastyNotifier != null) {
                        nastyNotifier.stopRequest();
                        nastyNotifier.waitUntilDone(2000);
                    }
                }
            }
        } catch ( InterruptedException x ) {
            // this should not happen, but it's a failure if it does
            failureExceptionWithStackTrace(x);
        } finally {
        }
    }
}
