package com.abc.ds.set.tests;

import java.util.*;

import com.abc.ds.sack.*;
import com.abc.ds.sack.tests.*;
import com.programix.testing.*;
import com.programix.util.*;

public class TestSuiteDSSet {
    // no instances
    private TestSuiteDSSet() {
    }

    public static BaseTest[] createAllTests(final DSSetFactory setFactory) {
        ObjectTools.paramNullCheck(setFactory, "factory");

        DSSackFactory sackFactory = new DSSackFactory() {
            @Override
            public boolean allowDuplicates() {
                return false;
            }

            @Override
            public boolean orderMatters() {
                return setFactory.orderMatters();
            }

            @Override
            public <T> DSSack<T> create(Class<T> itemType,
                                        int initialCapacity,
                                        int percentToGrowCapacity) {

                return setFactory.create(itemType, initialCapacity, percentToGrowCapacity);
            }

            @Override
            public <T> DSSack<T> create(Class<T> itemType) {
                return setFactory.create(itemType);
            }
        };

        BaseTest[] sackTests = TestSuiteDSSack.createAllTests(sackFactory);
        BaseTest[] setOnlyTests = new BaseTest[] {
            new TestDSSetFactoryCheck(setFactory),
            new TestDSSetUnion(setFactory),
            new TestDSSetIntersection(setFactory),
            new TestDSSetSubtract(setFactory),
        };

        List<BaseTest> allTestList = new ArrayList<>();
        allTestList.addAll(Arrays.asList(sackTests));
        allTestList.addAll(Arrays.asList(setOnlyTests));
        return allTestList.toArray(new BaseTest[0]);
    }

    public static TestChunk[] createAllTestChunks(DSSetFactory factory) {
        return StandardTestChunk.createAll(createAllTests(factory));
    }
}
