package com.abc.ds.list.integer.tests;

import java.util.*;

import com.abc.ds.list.integer.*;
import com.abc.ds.sack.integer.*;
import com.abc.ds.sack.integer.tests.*;
import com.programix.testing.*;
import com.programix.util.*;

public class TestSuiteIntDSList {
    // no instances
    private TestSuiteIntDSList() {
    }

    public static BaseTest[] createAllTests(final IntDSListFactory listFactory) {
        ObjectTools.paramNullCheck(listFactory, "listFactory");

        IntDSSackFactory sackFactory = new IntDSSackFactory() {
            @Override
            public boolean allowDuplicates() {
                return true;
            }

            @Override
            public boolean orderMatters() {
                return true;
            }

            @Override
            public IntDSList create(int initialCapacity,
                                    int percentToGrowCapacity) {

                return listFactory.create(initialCapacity, percentToGrowCapacity);
            }

            @Override
            public IntDSSack create() {
                return listFactory.create();
            }
        };

        BaseTest[] sackTests = TestSuiteIntDSSack.createAllTests(sackFactory);
        BaseTest[] listOnlyTests = new BaseTest[] {
            new TestIntDSListFactoryCheck(listFactory),
            new TestIntDSListInsertBefore(listFactory),
            new TestIntDSListInsertFirst(listFactory),
        };

        List<BaseTest> allTestList = new ArrayList<>();
        allTestList.addAll(Arrays.asList(sackTests));
        allTestList.addAll(Arrays.asList(listOnlyTests));
        return allTestList.toArray(new BaseTest[0]);
    }

    public static TestChunk[] createAllTestChunks(IntDSListFactory factory) {
        return StandardTestChunk.createAll(createAllTests(factory));
    }
}
