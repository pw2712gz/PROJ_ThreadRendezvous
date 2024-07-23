package com.abc.ds.set.tests;

import com.abc.ds.set.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSetSubtract extends TestDSSetBase {
    public TestDSSetSubtract(DSSetFactory factory) {
        super("subtract(DSSet otherSet)", factory);
    }

    @Override
    protected void performTests() {
        testUnion();
    }

    private void testUnion() {
        outln(" - subtract() with otherSet -");

        String[] origSetFruits = getFruits(0, 6);
        String[] otherSetFruits = getFruits(3, 6);
        String[] expectedFruits = getFruits(0, 3);

        DSSet<String> otherSet = createAltStringSet();
        otherSet.addAll(otherSetFruits);
        outln("otherSet peekAll():" + StringTools.formatCommaDelimited(otherSet.peekAll()));

        DSSet<String> ds = createDS();
        add(ds, origSetFruits);
        outln("subtract(otherSet)...");
        ds.subtract(otherSet);
        checkPeekAll(ds, expectedFruits);
    }
}
