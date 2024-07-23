package com.abc.ds.fifo.tests;

import com.abc.ds.fifo.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
abstract class TestDSUnboundedFifoBase extends TestDSBase {
    protected final DSUnboundedFifoFactory factory;
    protected final TestDSHelper<String> testHelper;

    protected TestDSUnboundedFifoBase(String titleSuffix,
                                      DSUnboundedFifoFactory factory) {
        super("DSUnboundedFifo - " + titleSuffix);
        this.factory = factory;
        testHelper = new TestDSHelper.Builder<String>()
            .setItemType(String.class)
            .setTestAccess(testAccess)
            .setAllowDuplicates(true)
            .setOrderMatters(true)
            .setWrapItemsInQuotes(true)
            .create();
    }

    protected DSUnboundedFifo<String> createDS() {
        outln("Creating a new DSUnboundedFifo<String> instance...");
        DSUnboundedFifo<String> fifo = factory.create(String.class);
        outln("   ...created: " + fifo.getClass().getCanonicalName());
        return fifo;
    }

    protected void add(DSUnboundedFifo<String> fifo, String... items) {
        for ( String item : items ) {
            fifo.add(item);
            outln("add(" + StringTools.quoteWrap(item) + ")");
        }
    }

    protected void checkPeekAll(DSUnboundedFifo<String> ds,
                                String... expectedItems) {

        testHelper.check("peekAll()", ds.peekAll(), expectedItems);
    }

    protected void checkPeekAllOnEmpty(DSUnboundedFifo<String> ds) {
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    protected void checkRemoveAll(DSUnboundedFifo<String> ds,
                                  String... expectedItems) {

        testHelper.check("removeAll()", ds.removeAll(), expectedItems);
    }
}
