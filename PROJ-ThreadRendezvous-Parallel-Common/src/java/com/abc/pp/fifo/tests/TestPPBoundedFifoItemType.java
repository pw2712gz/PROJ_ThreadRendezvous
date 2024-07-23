package com.abc.pp.fifo.tests;

import com.abc.ds.*;
import com.abc.ds.keyvalue.*;
import com.abc.pp.fifo.*;

/* deliberate package access */
class TestPPBoundedFifoItemType extends TestPPBoundedFifoBase {
    public TestPPBoundedFifoItemType(PPBoundedFifoFactory factory) {
        super("item type", factory);
    }

    @Override
    protected void performTests() {
        testItemTypeStandard();
        testItemTypeOthers();
    }

    private void testItemTypeStandard() {
        outln(" - item type - String -");
        PPBoundedFifo<String> fifo = createDS();
        outln("fifo.getItemType()", fifo.getItemType(), String.class);
    }

    private void testItemTypeOthers() {
        try {
            outln(" - item type - others -");
            PPBoundedFifo<Integer> fifoInteger = factory.create(Integer.class, 10);
            outln("fifo.getItemType()", fifoInteger.getItemType(), Integer.class);

            IntegerStringKeyValuePair pairA = new IntegerStringKeyValuePair(1234, "fun, fun, fun");

            @SuppressWarnings("unchecked")
            Class<DSKeyValuePair<Integer, String>> fooArrayCompType =
                (Class<DSKeyValuePair<Integer, String>>) DSTools.coerceClassType(
                    DSKeyValuePair.class);

            PPBoundedFifo<DSKeyValuePair<Integer, String>> fifoKeyValue =
                factory.create(fooArrayCompType, 20);

            fifoKeyValue.add(pairA);
            outln("fifo.getItemType()",
                fifoKeyValue.getItemType(), DSKeyValuePair.class);

            DSKeyValuePair<Integer, String> removedPair = fifoKeyValue.remove();
            outln("removedPair.getKey()", removedPair.getKey(), pairA.getKey());
            outln("removedPair.getValue()", removedPair.getValue(), pairA.getValue());
        } catch ( InterruptedException x ) {
            failureExceptionWithStackTrace(x);
        }
    }

    private static class IntegerStringKeyValuePair
            extends AbstractDSKeyValuePair<Integer, String> {

        private final Integer key;
        private final String value;

        public IntegerStringKeyValuePair(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    } // type IntegerStringKeyValuePair
}
