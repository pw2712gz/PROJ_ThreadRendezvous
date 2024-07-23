package com.abc.ds.fifo.tests;

import java.util.*;

import com.abc.ds.fifo.*;

/* deliberate package access */
class TestDSUnboundedFifoPeek extends TestDSUnboundedFifoBase {
    public TestDSUnboundedFifoPeek(DSUnboundedFifoFactory factory) {
        super("peek()", factory);
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
        outln("peek()", fifo.peek(), "apple");
    }

    private void testTwoItems() {
        outln(" - two items -");
        DSUnboundedFifo<String> fifo = createDS();
        add(fifo, "apple");
        add(fifo, "banana");
        outln("peek()", fifo.peek(), "apple");
    }

    private void testSeveralItems() {
        outln(" - several items -");
        DSUnboundedFifo<String> fifo = createDS();
        String[] fruits = getFruits(15);
        for ( String fruit : fruits ) {
            add(fifo, fruit);
        }
        outln("peek()", fifo.peek(), "apple");
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
            outln("empty, trying peek()...");
            fifo.peek();
        } catch ( NoSuchElementException x ) {
            outln("expected this exception: " + x.toString());
            success = true;
        } catch ( Exception x ) {
            failureExceptionWithStackTrace(x);
        }

        if ( success ) {
            outln("peek() on an empty fifo " +
                "threw NoSuchElementException", true);
        } else {
            outln("peek() on an empty list " +
                "did NOT throw NoSuchElementException", false);
        }
    }
}
