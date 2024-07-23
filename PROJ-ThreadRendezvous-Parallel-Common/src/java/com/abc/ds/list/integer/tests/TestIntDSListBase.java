package com.abc.ds.list.integer.tests;

import java.util.*;

import com.abc.ds.action.integer.*;
import com.abc.ds.filter.integer.*;
import com.abc.ds.iterator.*;
import com.abc.ds.iterator.integer.*;
import com.abc.ds.list.integer.*;
import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;
import com.programix.thread.*;

/* deliberate package access */
abstract class TestIntDSListBase extends TestDSBase {
    protected final Integer[] INTEGER_ZERO_LEN_ARRAY = new Integer[0];
    protected final int[] INT_ZERO_LEN_ARRAY = new int[0];

    protected final IntDSListFactory factory;
    protected final TestDSHelper<Integer> testHelper;

    protected TestIntDSListBase(String subTitle,
                                IntDSListFactory factory) {

        super("IntDSList - " + subTitle);
        this.factory = factory;
        testHelper = new TestDSHelper.Builder<Integer>()
            .setItemType(Integer.class)
            .setTestAccess(testAccess)
            .setAllowDuplicates(true)
            .setOrderMatters(true)
            .setWrapItemsInQuotes(false)
            .create();
    }

    protected int[] getInts(int count) {
        return TestIntGenerator.getInts(count);
    }

    protected int[] getInts(int offset, int count) {
        return TestIntGenerator.getInts(offset, count);
    }

    protected IntDSList createDS() {
        outln("Creating a new IntDSList instance...");
        IntDSList ds = factory.create();
        outln("   ...created: " + ds.getClass().getCanonicalName());
        ThreadTools.napRandom(100, 200);
        return ds;
    }

    protected void add(IntDSList ds, int... items) {
        for ( int item : items ) {
            outln("add(" + item + ")");
            ds.add(item);
        }
    }

    protected void addExpectGood(IntDSList ds, int... items) {
        for ( int item : items ) {
            boolean result = ds.add(item);
            outln("add(" + item + "), returned", result, true);
        }
    }

    protected void addExpectDuplicate(IntDSList ds, int... items) {
        for ( int item : items ) {
            boolean result = ds.add(item);
            outln("add(" + item + ") [duplicate], returned",
                result, true);
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

    protected void checkPeekAll(IntDSList ds,
                                int... expectedItems) {

        testHelper.check("peekAll()", wrap(ds.peekAll()), wrap(expectedItems));
    }

    protected void checkPeekAllOnEmpty(IntDSList ds) {
        checkPeekAll(ds, INT_ZERO_LEN_ARRAY);
    }

    protected void checkPeekMatches(IntDSList ds,
                                    IntDSFilter filter,
                                    int... expectedItems) {

        testHelper.check("peekMatches(filter)",
            wrap(ds.peekMatches(filter)), wrap(expectedItems));
    }

//    protected void checkRemoveMatches(IntDSList ds,
//                                      IntDSFilter filter,
//                                      int expectedRemoveCount) {
//
//        int actualRemoveCount = ds.removeMatches(filter);
//        outln("removeMatches(filter), returned remove count",
//            actualRemoveCount, expectedRemoveCount);
//    }

    protected void checkRemoveAll(IntDSList ds, int... expectedItems) {
        testHelper.check("removeAll()",
            wrap(ds.removeAll()), wrap(expectedItems));
    }

    protected void checkIterator(IntDSList ds, int... expectedItems) {
        testHelper.check("createIterator()",
            wrap(ds.createIterator()), wrap(expectedItems));
    }

    protected void checkContains(IntDSList ds,
                                 int item,
                                 boolean expectedResult) {

        outln("contains(" + item + ")", ds.contains(item), expectedResult);
    }

    protected void checkCount(IntDSList ds, int expectedResult) {
        outln("count()", ds.count(), expectedResult);
    }

    protected void checkIsEmpty(IntDSList ds, boolean expectedResult) {
        outln("isEmpty()", ds.isEmpty(), expectedResult);
    }

    protected void checkBadIndex(IntDSList ds,
                                 MethodAccess methodAccess) {
        boolean success = false;
        String methodText = methodAccess.formattedMethod();
        try {
            outln("count() is " + ds.count() +
                ", trying " + methodText + "...");
            methodAccess.execute(ds);
        } catch ( IndexOutOfBoundsException x ) {
            outln("expected this exception: " + x.toString());
            success = true;
        } catch ( Exception x ) {
            failureExceptionWithStackTrace(x);
        }

        if ( success ) {
            outln(methodText + " threw IndexOutOfBoundsException", true);
        } else {
            outln(methodText + " did NOT throw IndexOutOfBoundsException", false);
        }
    }

    protected static interface MethodAccess {
        String formattedMethod();
        void execute(IntDSList ds);
    } // type MethodAccess


    protected IntDSList createAltIntSack() {
        return new QuickAndDirtyDSList();
    }

    private class QuickAndDirtyDSList implements IntDSList {
        private List<Integer> list;

        public QuickAndDirtyDSList() {
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
        public void insertBefore(int index, int item)
                throws IndexOutOfBoundsException {

            list.add(index, item);
        }

        @Override
        public void insertFirst(int item) {
            list.add(0, item);
        }

        @Override
        public int removeAtIndex(int index) throws IndexOutOfBoundsException {
            return list.remove(index);
        }

        @Override
        public int removeFirst() throws NoSuchElementException {
            return list.remove(0);
        }

        @Override
        public int removeLast() throws NoSuchElementException {
            return list.remove(list.size() - 1);
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
        public int peekAtIndex(int index) throws IndexOutOfBoundsException {
            return list.get(index);
        }

        @Override
        public int peekFirst() throws NoSuchElementException {
            return list.get(0);
        }

        @Override
        public int peekLast() throws NoSuchElementException {
            return list.get(list.size() - 1);
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
        public void replaceAtLindex(int index, int replacementItem)
            throws IndexOutOfBoundsException {

            list.set(index, replacementItem);
        }

        @Override
        public int firstIndexOf(int item) {
            return list.indexOf(item);
        }

        @Override
        public int firstIndexOf(int item, int fromIndex) {
            for ( int i = Math.max(0, fromIndex); i < list.size(); i++ ) {
                if ( list.get(i) == item ) {
                    return i;
                }
            }
            return NOT_FOUND_INDEX;
        }

        @Override
        public int lastIndexOf(int item) {
            return list.lastIndexOf(item);
        }

        @Override
        public int lastIndexOf(int item, int fromIndex) {
            for ( int i = Math.min(fromIndex, list.size() - 1); i >= 0; i--) {
                if ( list.get(i) == item ) {
                    return i;
                }
            }
            return NOT_FOUND_INDEX;
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

        @Override
        public IntDSIterator createReverseIterator() {
            throw new RuntimeException("not implemented yet"); // FIXME
        }
    } // type QuickAndDirtyDSSack
}
