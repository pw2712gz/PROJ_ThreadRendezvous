package com.abc.ds.sack.tests;

import java.util.*;

import com.abc.ds.action.*;
import com.abc.ds.sack.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSackPerformOnAll extends TestDSSackBase {
    public TestDSSackPerformOnAll(DSSackFactory factory) {
        super("performOnAll()", factory);
    }

    @Override
    protected void performTests() {
        testperformOnMatchesWithFilter();
    }

    private void testperformOnMatchesWithFilter() {
        outln(" - performOnAll() string formatting -");
        DSSack<String> ds = createDS();

        int fruitCount = 50;

        String[] fruits = getFruits(fruitCount);
        add(ds, fruits);

        List<String> expectedLineList = new ArrayList<>();
        for ( String fruit : fruits ) {
            expectedLineList.add(processItem(fruit));
        }

        final List<String> resultLineList = new ArrayList<>();
        DSAction<String> action = new DSAction<String>() {
            @Override
            public void perform(String item) {
                String line;
                if ( item == null ) {
                    line = "item passed to perform() was null";
                } else {
                    line = processItem(item);
                }
                resultLineList.add(line);
            }
        };

        outln("DSAction is to format each item");
        outln("performOnAll(action)...");
        ds.performOnAll(action);

        testHelper.check("after perform()",
            StringTools.toArray(resultLineList),
            StringTools.toArray(expectedLineList));
    }

    private static String processItem(String item) {
        return ">" + item + "< with a length of " + item.length();
    }
}
