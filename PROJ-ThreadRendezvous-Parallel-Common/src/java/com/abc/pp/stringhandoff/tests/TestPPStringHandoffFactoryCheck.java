package com.abc.pp.stringhandoff.tests;

import com.abc.pp.stringhandoff.*;
import com.programix.testing.*;


public class TestPPStringHandoffFactoryCheck extends TestPPStringHandoffBase {
    public TestPPStringHandoffFactoryCheck(StringHandoffFactory factory) {
        super("factory check", new BasicScoringInfo(10, 1), factory);
    }

    @Override
    protected void performTests() {
        testCreate();
    }

    private void testCreate() {
        outln("Creating a new StringHandoff with " +
            "factory.create()...");
        StringHandoff sh = factory.create();
        outln("stringHandoff != null", sh != null, true);
    }
}
