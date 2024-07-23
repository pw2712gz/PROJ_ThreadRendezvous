package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffLockObjectCheck extends TestPPStringHandoffBase {
    public TestPPStringHandoffLockObjectCheck(StringHandoffFactory factory) {
        super("lock object check", new BasicScoringInfo(10, 1), factory);
    }

    @Override
    protected void performTests() {
        testCreate();
    }

    private void testCreate() {
        outln("Creating a new StringHandoff with " +
            "factory.create()...");
        StringHandoff sh = factory.create();
        outln("stringHandoff.getLockObject() != null", sh.getLockObject() != null, true);
    }
}
