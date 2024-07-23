package com.abc.ds.list.tests;

import java.util.*;

import com.abc.ds.sack.*;
import com.abc.ds.sack.tests.*;
import com.programix.testing.*;
import com.programix.util.*;

public class TestSuiteDSList {
    // no instances
    private TestSuiteDSList() {
    }

    public static BaseTest[] createAllTests(final DSListFactory listFactory) {
        ObjectTools.paramNullCheck(listFactory, "listFactory");

        DSSackFactory sackFactory = new DSSackFactory() {
            @Override
            public boolean allowDuplicates() {
                return true;
            }

            @Override
            public boolean orderMatters() {
                return true;
            }

            @Override
            public <T> DSSack<T> create(Class<T> itemType,
                                        int initialCapacity,
                                        int percentToGrowCapacity) {

                return listFactory.create(itemType, initialCapacity, percentToGrowCapacity);
            }

            @Override
            public <T> DSSack<T> create(Class<T> itemType) {
                return listFactory.create(itemType);
            }
        };

        BaseTest[] sackTests = TestSuiteDSSack.createAllTests(sackFactory);
        BaseTest[] listOnlyTests = new BaseTest[] {
            new TestDSListFactoryCheck(listFactory),
            new TestDSListInsertBefore(listFactory),
            new TestDSListInsertFirst(listFactory),
            new TestDSListIndexOf(listFactory),
        };

        List<BaseTest> allTestList = new ArrayList<>();
        allTestList.addAll(Arrays.asList(sackTests));
        allTestList.addAll(Arrays.asList(listOnlyTests));
        return allTestList.toArray(new BaseTest[0]);
    }

    public static TestChunk[] createAllTestChunks(DSListFactory factory) {
        return StandardTestChunk.createAll(createAllTests(factory));
    }
}
