package com.abc.ds.fifo.tests;

import com.abc.ds.*;
import com.abc.ds.fifo.*;
import com.abc.ds.keyvalue.*;

/* deliberate package access */
class TestDSUnboundedFifoItemType extends TestDSUnboundedFifoBase {
    public TestDSUnboundedFifoItemType(DSUnboundedFifoFactory factory) {
        super("item type", factory);
    }

    @Override
    protected void performTests() {
        testItemTypeStandard();
        testItemTypeOthers();
    }

    private void testItemTypeStandard() {
        outln(" - item type - String -");
        DSUnboundedFifo<String> fifo = createDS();
        outln("fifo.getItemType()", fifo.getItemType(), String.class);
    }

    private void testItemTypeOthers() {
        outln(" - item type - others -");
        DSUnboundedFifo<Integer> fifoInteger = factory.create(Integer.class);
        outln("fifo.getItemType()", fifoInteger.getItemType(), Integer.class);

        IntegerStringKeyValuePair pairA = new IntegerStringKeyValuePair(1234, "fun, fun, fun");

        @SuppressWarnings("unchecked")
        Class<DSKeyValuePair<Integer, String>> fooArrayCompType =
            (Class<DSKeyValuePair<Integer, String>>) DSTools.coerceClassType(
                DSKeyValuePair.class);

        DSUnboundedFifo<DSKeyValuePair<Integer, String>> fifoKeyValue =
            factory.create(fooArrayCompType);

        fifoKeyValue.add(pairA);
        outln("fifo.getItemType()",
            fifoKeyValue.getItemType(), DSKeyValuePair.class);

        DSKeyValuePair<Integer, String> removedPair = fifoKeyValue.peek();
        outln("removedPair.getKey()", removedPair.getKey(), pairA.getKey());
        outln("removedPair.getValue()", removedPair.getValue(), pairA.getValue());
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
