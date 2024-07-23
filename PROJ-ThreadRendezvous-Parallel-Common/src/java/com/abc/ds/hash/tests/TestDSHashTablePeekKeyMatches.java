package com.abc.ds.hash.tests;

import com.abc.ds.filter.*;
import com.abc.ds.hash.*;

/* deliberate package access */
class TestDSHashTablePeekKeyMatches extends TestDSHashTableBase {
    public TestDSHashTablePeekKeyMatches(DSHashTableFactory factory) {
        super("peekKeyMatches()", factory);
    }

    @Override
    protected void performTests() {
        testPeekKeyMatchesOnEmpty();
        testPeekKeyMatchesOnOne();
        testPeekKeyMatchesOnTwo();
        testPeekKeyMatchesOnFive();
    }

    private void testPeekKeyMatchesOnEmpty() {
        outln(" - peekKeyMatches() on empty -");
        DSHashTable<String, String> ds = createDS();
        checkIsEmpty(ds, true);

        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        checkPeekKeyMatches(ds, filter, STRING_KV_ZERO_LEN_ARRAY);
    }

    @SuppressWarnings("unchecked")
    private void testPeekKeyMatchesOnOne() {
        outln(" - peekKeyMatches() on one -");
        DSHashTable<String, String> ds = createDS();
        insert(ds, PAIR_VOLCANO);

        DSFilter<String> filter = createStringLengthFilter(7);
        outln("filtering with: " + filter);
        checkPeekKeyMatches(ds, filter, PAIR_VOLCANO);
    }

    @SuppressWarnings("unchecked")
    private void testPeekKeyMatchesOnTwo() {
        outln(" - peekKeyMatches() on two -");
        DSHashTable<String, String> ds = createDS();
        insert(ds, PAIR_OCEAN, PAIR_GLACIER);

        DSFilter<String> filter = createStringLengthFilter(7);
        outln("filtering with: " + filter);
        checkPeekKeyMatches(ds, filter, PAIR_GLACIER);
    }

    @SuppressWarnings("unchecked")
    private void testPeekKeyMatchesOnFive() {
        outln(" - peekKeyMatches() on five -");
        DSHashTable<String, String> ds = createDS();
        insert(ds, PAIR_BEACH, PAIR_GLACIER, PAIR_LAKE, PAIR_OCEAN, PAIR_SCHOOL, PAIR_VOLCANO);

        DSFilter<String> filter = createStringLengthFilter(7);
        outln("filtering with: " + filter);
        checkPeekKeyMatches(ds, filter,
            StringKeyValue.createArray(PAIR_VOLCANO, PAIR_GLACIER));
    }
}
