package com.abc.ds.fifo.tests;

import java.util.*;

import com.abc.ds.fifo.*;

/* deliberate package access */
class TestDSUnboundedFifoRemove extends TestDSUnboundedFifoBase {
    public TestDSUnboundedFifoRemove(DSUnboundedFifoFactory factory) {
        super("remove()", factory);
    }

    @Override
    protected void performTests() {
        testOneItem();
        testTwoItems();
        testSeveralItems();
        testEmpty();
    }

    private void testOneItem() {
        outln(" - one item -");
        DSUnboundedFifo<String> fifo = createDS();
        add(fifo, "apple");
        outln("remove()", fifo.remove(), "apple");
        outln("isEmpty()", fifo.isEmpty(), true);
    }

    private void testTwoItems() {
        outln(" - two items -");
        DSUnboundedFifo<String> fifo = createDS();
        add(fifo, "apple");
        add(fifo, "banana");
        outln("remove()", fifo.remove(), "apple");
        outln("remove()", fifo.remove(), "banana");
        outln("isEmpty()", fifo.isEmpty(), true);
    }

    private void testSeveralItems() {
        outln(" - several items -");
        DSUnboundedFifo<String> fifo = createDS();
        String[] fruits = getFruits(15);
        for ( String fruit : fruits ) {
            add(fifo, fruit);
        }
        for ( String fruit : fruits ) {
            outln("remove()", fifo.remove(), fruit);
        }
        outln("isEmpty()", fifo.isEmpty(), true);
    }

    private void testEmpty() {
        outln(" - empty (NoSuchElementException) -");
        DSUnboundedFifo<String> fifo = createDS();

        outln("confirming isEmpty()", fifo.isEmpty(), true);
        if ( !fifo.isEmpty() ) {
            outlnErrorText("Cannot proceed with peek on empty tests: " +
                "newly created should be empty but is not");
            return;
        }

        testEmptyHelper(fifo);

        add(fifo, getFruits(5));
        outln("clear()");
        fifo.clear();

        testEmptyHelper(fifo);
    }

    private void testEmptyHelper(DSUnboundedFifo<?> fifo) {
        if ( !fifo.isEmpty() ) {
            outln("can't attempt - isEmpty() is not returning true", false);
            return;
        }

        boolean success = false;
        try {
            outln("empty, trying remove()...");
            fifo.remove();
        } catch ( NoSuchElementException x ) {
            outln("expected this exception: " + x.toString());
            success = true;
        } catch ( Exception x ) {
            failureExceptionWithStackTrace(x);
        }

        if ( success ) {
            outln("remove() on an empty fifo " +
                "threw NoSuchElementException", true);
        } else {
            outln("remove() on an empty list " +
                "did NOT throw NoSuchElementException", false);
        }
    }
}
