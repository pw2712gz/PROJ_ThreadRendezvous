package com.abc.ds.hash.tests;

import java.math.*;

import com.abc.ds.hash.*;

/* deliberate package access */
class TestDSHashTableKeyAndValueTypes extends TestDSHashTableBase {
    public TestDSHashTableKeyAndValueTypes(DSHashTableFactory factory) {
        super("key type and value type", factory);
    }

    @Override
    protected void performTests() {
        testItemTypeStandard();
        testItemTypeDifferentKeyAndValueTypes();
    }

    private void testItemTypeStandard() {
        outln(" - types: <String, String> -");
        DSHashTable<String, String> ds = createDS();
        outln("getKeyType()", ds.getKeyType(), String.class);
        outln("getValueType()", ds.getValueType(), String.class);
    }

    private void testItemTypeDifferentKeyAndValueTypes() {
        outln(" - types: <Integer, BigDecimal> -");
        DSHashTable<Integer, BigDecimal> ds =
            factory.create(Integer.class, BigDecimal.class, 11, 0.8);
        outln("getKeyType()", ds.getKeyType(), Integer.class);
        outln("getValueType()", ds.getValueType(), BigDecimal.class);
    }
}
