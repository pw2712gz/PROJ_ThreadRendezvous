package com.abc.ds.fifo.tests;

import com.abc.ds.fifo.*;

/* deliberate package access */
class TestDSUnboundedFifoPeekThenRemove extends TestDSUnboundedFifoBase {
    public TestDSUnboundedFifoPeekThenRemove(DSUnboundedFifoFactory factory) {
        super("peek() then remove()", factory);
    }

    @Override
    protected void performTests() {
        testOneItem();
        testThreeItems();
    }

    private void testOneItem() {
        outln(" - one item -");
        DSUnboundedFifo<String> fifo = createDS();
        add(fifo, "apple");
        outln("peek()", fifo.peek(), "apple");
        outln("remove()", fifo.remove(), "apple");
    }

    private void testThreeItems() {
        outln(" - two items -");
        DSUnboundedFifo<String> fifo = createDS();
        add(fifo, "apple");
        add(fifo, "banana");
        add(fifo, "cherry");
        outln("peek()", fifo.peek(), "apple");
        outln("remove()", fifo.remove(), "apple");
        outln("peek()", fifo.peek(), "banana");
        outln("remove()", fifo.remove(), "banana");
        outln("peek()", fifo.peek(), "cherry");
        outln("remove()", fifo.remove(), "cherry");
    }

}
