package com.abc.ds.sack.integer.tests;

import java.util.*;

import com.abc.ds.action.integer.*;
import com.abc.ds.filter.integer.*;
import com.abc.ds.sack.integer.*;

/* deliberate package access */
class TestIntDSSackPerformOnMatches extends TestIntDSSackBase {
    public TestIntDSSackPerformOnMatches(IntDSSackFactory factory) {
        super("performOnMatches(filter, action)", factory);
    }

    @Override
    protected void performTests() {
        testperformOnMatchesWithFilter();
    }

    private void testperformOnMatchesWithFilter() {
        outln(" - performOnMatches(filter, action) doubling int's -");
        IntDSSack ds = createDS();

        int[] items = getInts(50);
        add(ds, items);

        int targetLessThanValue = 250;

        List<Integer> expectedResultList = new ArrayList<>();
        for ( int item : items ) {
            if ( item < targetLessThanValue ) {
                expectedResultList.add(processItem(item));
            }
        }

        final List<Integer> resultList = new ArrayList<>();
        IntDSAction action = new IntDSAction() {
            @Override
            public void perform(int item) {
                int result = processItem(item);
                resultList.add(result);
            }
        };

        IntDSFilter filter = createFilterLessThan(targetLessThanValue);

        outln("IntDSAction is to double each int value");
        outln("IntDSFilter is: " + filter);

        outln("performOnMatches(filter, action)...");
        ds.performOnMatches(filter, action);

        testHelper.check("after performOnMatches(filter, action)",
                resultList.toArray(INTEGER_ZERO_LEN_ARRAY),
                expectedResultList.toArray(INTEGER_ZERO_LEN_ARRAY)
            );
    }

    private static int processItem(int item) {
        return item * 2;
    }
}
