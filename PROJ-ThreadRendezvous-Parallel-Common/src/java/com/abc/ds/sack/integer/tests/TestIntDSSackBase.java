package com.abc.ds.sack.integer.tests;

import java.util.*;

import com.abc.ds.action.integer.*;
import com.abc.ds.filter.integer.*;
import com.abc.ds.iterator.*;
import com.abc.ds.iterator.integer.*;
import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;
import com.programix.thread.*;

/* deliberate package access */
abstract class TestIntDSSackBase extends TestDSBase {
    protected final Integer[] INTEGER_ZERO_LEN_ARRAY = new Integer[0];
    protected final int[] INT_ZERO_LEN_ARRAY = new int[0];

    protected final IntDSSackFactory factory;
    protected final boolean allowDuplicates;
    protected final boolean orderMatters;
    protected final TestDSHelper<Integer> testHelper;

    protected TestIntDSSackBase(String subTitle,
                                IntDSSackFactory factory) {

        super("IntDSSack - " + subTitle);
        this.factory = factory;
        allowDuplicates = factory.allowDuplicates();
        orderMatters = factory.orderMatters();
        testHelper = new TestDSHelper.Builder<Integer>()
            .setItemType(Integer.class)
            .setTestAccess(testAccess)
            .setAllowDuplicates(allowDuplicates)
            .setOrderMatters(orderMatters)
            .setWrapItemsInQuotes(false)
            .create();
    }

    protected int[] getInts(int count) {
        return TestIntGenerator.getInts(count);
    }

    protected int[] getInts(int offset, int count) {
        return TestIntGenerator.getInts(offset, count);
    }

    protected IntDSSack createDS() {
        outln("Creating a new IntDSSack instance...");
        IntDSSack ds = factory.create();
        outln("   ...created: " + ds.getClass().getCanonicalName());
        ThreadTools.napRandom(100, 200);
        return ds;
    }

    protected void add(IntDSSack ds, int... items) {
        for ( int item : items ) {
            outln("add(" + item + ")");
            ds.add(item);
        }
    }

    protected void addExpectGood(IntDSSack ds, int... items) {
        for ( int item : items ) {
            boolean result = ds.add(item);
            outln("add(" + item + "), returned", result, true);
        }
    }

    protected void addExpectDuplicate(IntDSSack ds, int... items) {
        for ( int item : items ) {
            boolean result = ds.add(item);
            outln("add(" + item + ") [duplicate], returned",
                result, allowDuplicates);
        }
    }

    protected IntDSFilter createFilterLessThan(final int value) {
        return new IntDSFilter() {
            @Override
            public boolean matches(int item) {
                return item < value;
            }

            @Override
            public String toString() {
                return "IntDSFilter[item < " + value + "]";
            }
        };
    }

    protected Integer[] wrap(int[] values) {
        if ( values == null ) {
            return null;
        }
        Integer[] wrappedValues = new Integer[values.length];
        for ( int i = 0; i < wrappedValues.length; i++ ) {
            wrappedValues[i] = new Integer(values[i]);
        }
        return wrappedValues;
    }

    protected int[] unwrap(Integer[] values) {
        if ( values == null ) {
            return null;
        }
        int[] unwrappedValues = new int[values.length];
        for ( int i = 0; i < unwrappedValues.length; i++ ) {
            unwrappedValues[i] = values[i];
        }
        return unwrappedValues;
    }

    protected int[] unwrap(Collection<Integer> values) {
        if ( values == null ) {
            return null;
        }
        return unwrap(values.toArray(INTEGER_ZERO_LEN_ARRAY));
    }

    protected DSIterator<Integer> wrap(final IntDSIterator rawIter) {
        if ( rawIter == null ) return null;
        return new DSIterator<Integer>() {
            @Override
            public boolean hasNext() {
                return rawIter.hasNext();
            }

            @Override
            public Integer next() throws NoSuchElementException {
                return rawIter.next();
            }
        };
    }

    protected void checkPeekAll(IntDSSack ds,
                                int... expectedItems) {

        testHelper.check("peekAll()", wrap(ds.peekAll()), wrap(expectedItems));
    }

    protected void checkPeekAllOnEmpty(IntDSSack ds) {
        checkPeekAll(ds, INT_ZERO_LEN_ARRAY);
    }

    protected void checkPeekMatches(IntDSSack ds,
                                    IntDSFilter filter,
                                    int... expectedItems) {

        testHelper.check("peekMatches(filter)",
            wrap(ds.peekMatches(filter)), wrap(expectedItems));
    }

//    protected void checkRemoveMatches(IntDSSack ds,
//                                      IntDSFilter filter,
//                                      int expectedRemoveCount) {
//
//        int actualRemoveCount = ds.removeMatches(filter);
//        outln("removeMatches(filter), returned remove count",
//            actualRemoveCount, expectedRemoveCount);
//    }

    protected void checkRemoveAll(IntDSSack ds, int... expectedItems) {
        testHelper.check("removeAll()",
            wrap(ds.removeAll()), wrap(expectedItems));
    }

    protected void checkIterator(IntDSSack ds, int... expectedItems) {
        testHelper.check("createIterator()",
            wrap(ds.createIterator()), wrap(expectedItems));
    }

    protected void checkContains(IntDSSack ds,
                                 int item,
                                 boolean expectedResult) {

        outln("contains(" + item + ")", ds.contains(item), expectedResult);
    }

    protected void checkCount(IntDSSack ds, int expectedResult) {
        outln("count()", ds.count(), expectedResult);
    }

    protected void checkIsEmpty(IntDSSack ds, boolean expectedResult) {
        outln("isEmpty()", ds.isEmpty(), expectedResult);
    }

    protected IntDSSack createAltIntSack() {
        return new QuickAndDirtyDSSack();
    }

    private class QuickAndDirtyDSSack implements IntDSSack {
        private List<Integer> list;

        public QuickAndDirtyDSSack() {
            list = new ArrayList<>();
        }

        @Override
        public int count() {
            return list.size();
        }

        @Override
        public boolean isEmpty() {
            return count() == 0;
        }

        @Override
        public void clear() {
            list.clear();
        }

        @Override
        public boolean add(int item) {
            return list.add(item);
        }

        @Override
        public int addAll(int... items) {
            if ( items == null ) {
                return 0;
            }

            int addCount = 0;
            for ( int item : items ) {
                if ( add(item) ) {
                    addCount++;
                }
            }
            return addCount;
        }

        @Override
        public int addAll(IntDSSack otherSack) {
            return addAll(otherSack.peekAll());
        }

        @Override
        public boolean remove(int item) {
            return list.remove((Integer) item);
        }

        @Override
        public int[] removeAndReturnMatches(IntDSFilter filter) {
            int[] results = peekMatches(filter);
            for ( int item : results ) {
               list.remove(new Integer(item));
            }
            return results;
        }

        @Override
        public int removeAndCountMatches(IntDSFilter filter) {
            return removeAndReturnMatches(filter).length;
        }

        @Override
        public int[] removeAll() {
            int[] results = peekAll();
            clear();
            return results;
        }

        @Override
        public boolean contains(int item) {
            return list.contains(item);
        }

        @Override
        public int[] peekMatches(IntDSFilter filter) {
            List<Integer> resultList = new ArrayList<>();
            for ( int item : list ) {
                if ( filter.matches(item) ) {
                    resultList.add(item);
                }
            }
            return unwrap(resultList);
        }

        @Override
        public int countMatches(IntDSFilter filter) {
            return peekMatches(filter).length;
        }

        @Override
        public int[] peekAll() {
            return unwrap(list);
        }

        @Override
        public void performOnAll(IntDSAction action) {
            performOnMatches(new IntDSFilter() {
                @Override
                public boolean matches(int item) {
                    return true;
                }
            }, action);
        }

        @Override
        public int performOnMatches(IntDSFilter filter, IntDSAction action) {
            int[] matches = peekMatches(filter);
            for ( int item : matches ) {
                action.perform(item);
            }
            return matches.length;
        }

        @Override
        public IntDSIterator createIterator() {
            return new IntDSIterator() {
                private final Iterator<Integer> iterator = list.iterator();

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public int next() throws NoSuchElementException {
                    return iterator.next();
                }
            };
        }
    } // type QuickAndDirtyDSSack
}
