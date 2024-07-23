package com.abc.pp.fifo.tests;

import com.abc.pp.fifo.*;

/* deliberate package access */
class TestPPBoundedFifoAddAllUpToCapacity extends TestPPBoundedFifoBase {
    public TestPPBoundedFifoAddAllUpToCapacity(PPBoundedFifoFactory factory) {
        super("addAll() - non-blocking", factory);
    }

    @Override
    protected void performTests() {
        testAddAllToNewInstance();
        testAddAllWrapping();
        testAddAllExactlyUpToCapacity();
    }

    private void testAddAllToNewInstance() {
        try {
            outln(" - addAll() to new instance -");
            int capacity = 10;
            PPBoundedFifo<String> ds = createDS(capacity);
            String[] fruits = getFruits(capacity - 2);
            outln("addAll(" + formatCommaDelimited(fruits) + ")...");
            ds.addAll(fruits);
            outln("getCount()", ds.getCount(), fruits.length);
            checkRemoveAll(ds, fruits);
            outln("getCount()", ds.getCount(), 0);
        } catch ( InterruptedException x ) {
            // this should not happen, but it's a failure if it does
            failureExceptionWithStackTrace(x);
        }
    }

    private void testAddAllWrapping() {
        try {
            outln(" - addAll() after add/remove (wrapping around for array implementations) -");
            int capacity = 10;
            PPBoundedFifo<String> ds = createDS(capacity);

            String[] fruitSetA = getFruits(5);
            String[] fruitSetB = getFruits(fruitSetA.length, 2);
            String[] fruitSetC = getFruits(fruitSetA.length + fruitSetB.length, capacity - fruitSetB.length - 1);

            addBulk(ds, fruitSetA);
            addBulk(ds, fruitSetB);
            outln("getCount()", ds.getCount(), fruitSetA.length + fruitSetB.length);
            for ( String fruit : fruitSetA ) {
                outln("remove()", ds.remove(), fruit);
            }
            outln("getCount()", ds.getCount(), fruitSetB.length);

            outln("addAll(" + formatCommaDelimited(fruitSetC) + ")...");
            ds.addAll(fruitSetC);
            outln("getCount()", ds.getCount(), fruitSetB.length + fruitSetC.length);

            for ( String fruit : fruitSetB ) {
                outln("remove()", ds.remove(), fruit);
            }

            outln("getCount()", ds.getCount(), fruitSetC.length);
            checkRemoveAll(ds, fruitSetC);
            outln("getCount()", ds.getCount(), 0);
        } catch ( InterruptedException x ) {
            // this should not happen, but it's a failure if it does
            failureExceptionWithStackTrace(x);
        }
    }

    private void testAddAllExactlyUpToCapacity() {
        try {
            outln(" - addAll() up to capacity -");
            int capacity = 10;
            PPBoundedFifo<String> ds = createDS(capacity);

            String[] fruitSetA = getFruits(5);
            String[] fruitSetB = getFruits(fruitSetA.length, 2);
            String[] fruitSetC = getFruits(fruitSetA.length + fruitSetB.length, capacity - fruitSetB.length);

            addBulk(ds, fruitSetA);
            addBulk(ds, fruitSetB);
            outln("getCount()", ds.getCount(), fruitSetA.length + fruitSetB.length);
            for ( String fruit : fruitSetA ) {
                outln("remove()", ds.remove(), fruit);
            }
            outln("getCount()", ds.getCount(), fruitSetB.length);

            outln("addAll(" + formatCommaDelimited(fruitSetC) + ")...");
            ds.addAll(fruitSetC);
            outln("isFull()", ds.isFull(), true);
            outln("getCount()", ds.getCount(), fruitSetB.length + fruitSetC.length);

            for ( String fruit : fruitSetB ) {
                outln("remove()", ds.remove(), fruit);
            }

            outln("getCount()", ds.getCount(), fruitSetC.length);
            checkRemoveAll(ds, fruitSetC);
            outln("getCount()", ds.getCount(), 0);
        } catch ( InterruptedException x ) {
            // this should not happen, but it's a failure if it does
            failureExceptionWithStackTrace(x);
        }
    }
}
