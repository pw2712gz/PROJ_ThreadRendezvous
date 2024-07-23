package com.abc.ds.set.tests;

import com.abc.ds.set.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSetUnion extends TestDSSetBase {
    public TestDSSetUnion(DSSetFactory factory) {
        super("union(DSSet otherSet)", factory);
    }

    @Override
    protected void performTests() {
        testUnion();
    }

    private void testUnion() {
        outln(" - union() with otherSet -");

        String[] origSetFruits = getFruits(0, 6);
        String[] otherSetFruits = getFruits(3, 6);
        String[] expectedFruits = getFruits(0, 9);

        DSSet<String> otherSet = createAltStringSet();
        otherSet.addAll(otherSetFruits);
        outln("otherSet peekAll():" + StringTools.formatCommaDelimited(otherSet.peekAll()));

        DSSet<String> ds = createDS();
        add(ds, origSetFruits);
        outln("union(otherSet)...");
        ds.union(otherSet);
        checkPeekAll(ds, expectedFruits);
    }
}
