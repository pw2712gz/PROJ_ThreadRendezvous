package com.abc.handoff.test;

import com.abc.handoff.*;
import com.abc.pp.stringhandoff.*;
import com.abc.pp.stringhandoff.tests.*;
import com.abc.pp.stringhandoff.tests.gui.*;

public class TestStringHandoff {
    public static void main(String[] args) {
        GuiTestPPStringHandoff.runTests(
                "Testing of StringHandoff",
                true,
                new StringHandoffFactory() {
                    @Override
                    public StringHandoff create() {
                        return new StringHandoffImpl();
                    }
                });
    }
}