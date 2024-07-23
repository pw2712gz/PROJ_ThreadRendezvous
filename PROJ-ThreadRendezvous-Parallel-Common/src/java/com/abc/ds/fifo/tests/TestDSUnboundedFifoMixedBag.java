package com.abc.ds.fifo.tests;

import com.abc.ds.fifo.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSUnboundedFifoMixedBag extends TestDSUnboundedFifoBase {
    public TestDSUnboundedFifoMixedBag(DSUnboundedFifoFactory factory) {
        super("mixed bag", factory);
    }

    @Override
    protected void performTests() {
        testA();
    }

    private void testA() {
        outln(" - Test A -");
        DSUnboundedFifo<String> fifo = createDS();

        TestFruitGenerator fruitGen =
            new TestFruitGenerator(TestFruitGenerator.RANDOM_SEED_15);

        String[] fruitsA = fruitGen.next(2);
        add(fifo, fruitsA);
        outln("checking count()", fifo.count(), 2);
        for ( String fruit : fruitsA ) {
            outln("remove()", fifo.remove(), fruit);
        }
        outln("checking count()", fifo.count(), 0);

        String[] fruitsB = fruitGen.next(3);
        add(fifo, fruitsB);
        for ( String fruit : fruitsB ) {
            outln("remove()", fifo.remove(), fruit);
        }
        outln("checking count()", fifo.count(), 0);

        String[] fruitsC = fruitGen.next(40);
        outln("addAll(" + StringTools.formatCommaDelimited(fruitsC) + ")...");
        fifo.addAll(fruitsC);
        outln("removeAll()", fifo.removeAll(), fruitsC);

        String[] colorA = new String[] { "red", null, "green", "red", null };
        outln("addAll(" + StringTools.formatCommaDelimited(colorA) + ")...");
        fifo.addAll(colorA);
        outln("checking count()", fifo.count(), 5);
        outln("remove()", fifo.remove(), colorA[0]);
        outln("remove()", fifo.remove(), colorA[1]);
        outln("checking count()", fifo.count(), 3);
        String[] colorB = new String[] { "blue", "white", null, "orange" };
        for ( String color : colorB ) {
            add(fifo, color);
        }
        outln("checking count()", fifo.count(), 7);
        outln("remove()", fifo.remove(), colorA[2]);
        outln("remove()", fifo.remove(), colorA[3]);
        outln("remove()", fifo.remove(), colorA[4]);
        outln("remove()", fifo.remove(), colorB[0]);
        outln("remove()", fifo.remove(), colorB[1]);
        outln("checking count()", fifo.count(), 2);

        outln("clear()...");
        fifo.clear();
        outln("checking count()", fifo.count(), 0);

        String[] colorC = new String[] { "yellow", "green", "pink" };
        add(fifo, colorC);
        outln("checking count()", fifo.count(), 3);
        outln("remove()", fifo.remove(), colorC[0]);
        outln("remove()", fifo.remove(), colorC[1]);
        outln("remove()", fifo.remove(), colorC[2]);
    }
}
