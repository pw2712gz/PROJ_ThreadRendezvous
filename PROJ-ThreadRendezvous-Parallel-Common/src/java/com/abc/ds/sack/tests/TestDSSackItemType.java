package com.abc.ds.sack.tests;

import com.abc.ds.*;
import com.abc.ds.keyvalue.*;
import com.abc.ds.sack.*;

/* deliberate package access */
class TestDSSackItemType extends TestDSSackBase {
    public TestDSSackItemType(DSSackFactory factory) {
        super("item type", factory);
    }

    @Override
    protected void performTests() {
        testItemTypeStandard();
        testItemTypeOthers();
    }

    private void testItemTypeStandard() {
        outln(" - item type - String -");
        DSSack<String> ds = createDS();
        outln("getItemType()", ds.getItemType(), String.class);
    }

    private void testItemTypeOthers() {
        outln(" - item type - others -");
        outln("creating for Integer...");
        DSSack<Integer> dsInteger = factory.create(Integer.class);
        outln("getItemType()", dsInteger.getItemType(), Integer.class);


        @SuppressWarnings("unchecked")
        Class<DSKeyValuePair<Integer, String>> specialType =
            (Class<DSKeyValuePair<Integer, String>>) DSTools.coerceClassType(
                DSKeyValuePair.class);

        outln("creating for DSKeyValuePair<Integer, String>...");
        DSSack<DSKeyValuePair<Integer, String>> dsKeyValue =
            factory.create(specialType);

        outln("confirming that an item of that type can be added...");
        dsKeyValue.add(new IntegerStringKeyValuePair(1234, "fun, fun, fun"));
        outln("getItemType()", dsKeyValue.getItemType(), specialType);
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
