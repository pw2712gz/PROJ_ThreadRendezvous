package com.abc.ds.set.tests;

import com.abc.ds.set.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSetIntersection extends TestDSSetBase {
    public TestDSSetIntersection(DSSetFactory factory) {
        super("intersection(DSSet otherSet)", factory);
    }

    @Override
    protected void performTests() {
        testUnion();
    }

    private void testUnion() {
        outln(" - intersection() with otherSet -");

        String[] origSetFruits = getFruits(0, 6);
        String[] otherSetFruits = getFruits(3, 6);
        String[] expectedFruits = getFruits(3, 3);

        DSSet<String> otherSet = createAltStringSet();
        otherSet.addAll(otherSetFruits);
        outln("otherSet peekAll():" + StringTools.formatCommaDelimited(otherSet.peekAll()));

        DSSet<String> ds = createDS();
        add(ds, origSetFruits);
        outln("intersection(otherSet)...");
        ds.intersection(otherSet);
        checkPeekAll(ds, expectedFruits);
    }
}
