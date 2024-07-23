package com.abc.ds.sack.integer.tests;

import java.util.*;

import com.abc.ds.action.integer.*;
import com.abc.ds.sack.integer.*;

/* deliberate package access */
class TestIntDSSackPerformOnAll extends TestIntDSSackBase {
    public TestIntDSSackPerformOnAll(IntDSSackFactory factory) {
        super("performOnAll(action)", factory);
    }

    @Override
    protected void performTests() {
        testperformOnAll();
    }

    private void testperformOnAll() {
        outln(" - performOnAll(action) doubling int's -");
        IntDSSack ds = createDS();

        int[] items = getInts(50);
        add(ds, items);

        List<Integer> expectedResultList = new ArrayList<>();
        for ( int item : items ) {
            expectedResultList.add(processItem(item));
        }

        final List<Integer> resultList = new ArrayList<>();
        IntDSAction action = new IntDSAction() {
            @Override
            public void perform(int item) {
                int result = processItem(item);
                resultList.add(result);
            }
        };

        outln("IntDSAction is to double each int value");
        outln("performOnAll(action)...");
        ds.performOnAll(action);

        testHelper.check("after perform()",
                resultList.toArray(INTEGER_ZERO_LEN_ARRAY),
                expectedResultList.toArray(INTEGER_ZERO_LEN_ARRAY)
            );
    }

    private static int processItem(int item) {
        return item * 2;
    }
}
