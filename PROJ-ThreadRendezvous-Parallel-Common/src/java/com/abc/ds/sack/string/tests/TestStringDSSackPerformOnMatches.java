package com.abc.ds.sack.string.tests;

import java.util.*;

import com.abc.ds.action.string.*;
import com.abc.ds.filter.string.*;
import com.abc.ds.sack.string.*;
import com.programix.util.*;

/* deliberate package access */
class TestStringDSSackPerformOnMatches extends TestStringDSSackBase {
    public TestStringDSSackPerformOnMatches(StringDSSackFactory factory) {
        super("performOnMatches(filter)", factory);
    }

    @Override
    protected void performTests() {
        testperformOnMatchesWithFilter();
    }

    private void testperformOnMatchesWithFilter() {
        outln(" - performOnMatches() with filter -");
        StringDSSack ds = createDS();

        int fruitCount = 50;

        String[] fruits = getFruits(fruitCount);
        add(ds, fruits);

        final StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);

        List<String> expectedLineList = new ArrayList<>();
        for ( String fruit : fruits ) {
            if ( filter.matches(fruit) ) {
                expectedLineList.add(processItem(fruit));
            }
        }

        final List<String> resultLineList = new ArrayList<>();
        StringDSAction action = new StringDSAction() {
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
        outln("performOnMatches(filter, action)");
        ds.performOnMatches(filter, action);

        testHelper.check("after performOnMatches()",
            StringTools.toArray(resultLineList),
            StringTools.toArray(expectedLineList));
    }

    private static String processItem(String item) {
        return item + ", length()=" + item.length();
    }
}
