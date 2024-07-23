package com.abc.ds.sack.string.tests;

import java.util.*;

import com.abc.ds.action.string.*;
import com.abc.ds.filter.string.*;
import com.abc.ds.iterator.*;
import com.abc.ds.iterator.string.*;
import com.abc.ds.sack.string.*;
import com.abc.ds.tests.*;
import com.programix.thread.*;
import com.programix.util.*;

/* deliberate package access */
abstract class TestStringDSSackBase extends TestDSBase {
    protected final StringDSSackFactory factory;
    protected final boolean allowDuplicates;
    protected final boolean orderMatters;
    protected final TestDSHelper<String> testHelper;

    protected TestStringDSSackBase(String subTitle,
                             StringDSSackFactory factory) {

        super("StringDSSack - " + subTitle);
        this.factory = factory;
        allowDuplicates = factory.allowDuplicates();
        orderMatters = factory.orderMatters();
        testHelper = new TestDSHelper.Builder<String>()
            .setItemType(String.class)
            .setTestAccess(testAccess)
            .setAllowDuplicates(allowDuplicates)
            .setOrderMatters(orderMatters)
            .setWrapItemsInQuotes(true)
            .create();
    }

    protected StringDSSack createDS() {
        outln("Creating a new StringDSSack instance...");
        StringDSSack ds = factory.create();
        outln("   ...created: " + ds.getClass().getCanonicalName());
        ThreadTools.napRandom(100, 200);
        return ds;
    }

    protected void add(StringDSSack ds, String... items) {
        for ( String item : items ) {
            outln("add(" + StringTools.quoteWrap(item) + ")");
            ds.add(item);
        }
    }

    protected void addExpectGood(StringDSSack ds, String... items) {
        for ( String item : items ) {
            boolean result = ds.add(item);
            outln("add(" + StringTools.quoteWrap(item) + "), returned",
                result, true);
        }
    }

    protected void addExpectDuplicate(StringDSSack ds, String... items) {
        for ( String item : items ) {
            boolean result = ds.add(item);
            outln("add(" + StringTools.quoteWrap(item) +
                ") [duplicate], returned", result, allowDuplicates);
        }
    }

    protected DSIterator<String> wrap(final StringDSIterator rawIter) {
        if ( rawIter == null ) return null;
        return new DSIterator<String>() {
            @Override
            public boolean hasNext() {
                return rawIter.hasNext();
            }

            @Override
            public String next() throws NoSuchElementException {
                return rawIter.next();
            }
        };
    }

    protected StringDSFilter createMatchLengthStringDSFilter(final int lengthToMatch) {
        return new StringDSFilter() {
            @Override
            public boolean matches(String item) {
                return item != null && item.length() == lengthToMatch;
            }

            @Override
            public String toString() {
                return "DSFilter[string length = " + lengthToMatch + "]";
            }
        };
    }


    protected void checkPeekAll(StringDSSack ds,
                                String... expectedItems) {

        testHelper.check("peekAll()", ds.peekAll(), expectedItems);
    }

    protected void checkPeekAllOnEmpty(StringDSSack ds) {
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    protected void checkPeekMatches(StringDSSack ds,
                                    StringDSFilter filter,
                                    String... expectedItems) {

        testHelper.check("peekMatches(filter)", ds.peekMatches(filter), expectedItems);
    }

//    protected void checkRemoveMatches(StringDSSack ds,
//                                      StringDSFilter filter,
//                                      int expectedRemoveCount) {
//
//        int actualRemoveCount = ds.removeMatches(filter);
//        outln("removeMatches(filter), returned remove count",
//            actualRemoveCount, expectedRemoveCount);
//    }

    protected void checkRemoveAll(StringDSSack ds,
                                String... expectedItems) {

        testHelper.check("removeAll()", ds.removeAll(), expectedItems);
    }

    protected void checkIterator(StringDSSack ds, String... expectedItems) {
        testHelper.check("createIterator()", wrap(ds.createIterator()), expectedItems);
    }

    protected void checkContains(StringDSSack ds,
                                 String item,
                                 boolean expectedResult) {

        outln("contains(" + StringTools.quoteWrap(item) + ")",
            ds.contains(item), expectedResult);
    }

    protected void checkCount(StringDSSack ds, int expectedResult) {
        outln("count()", ds.count(), expectedResult);
    }

    protected void checkIsEmpty(StringDSSack ds, boolean expectedResult) {
        outln("isEmpty()", ds.isEmpty(), expectedResult);
    }

    protected StringDSSack createAltStringSack() {
        return new QuickAndDirtyDSSack();
    }

    private static class QuickAndDirtyDSSack implements StringDSSack {
        private List<String> list;

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
        public boolean add(String item) {
            return list.add(item);
        }

        @Override
        public int addAll(String... items) {
            if ( ObjectTools.isEmpty(items) ) {
                return 0;
            }

            int addCount = 0;
            for ( String item : items ) {
                if ( add(item) ) {
                    addCount++;
                }
            }
            return addCount;
        }

        @Override
        public int addAll(StringDSSack otherSack) {
            return addAll(otherSack.peekAll());
        }

        @Override
        public boolean remove(String item) {
            return list.remove(item);
        }

        @Override
        public String[] removeAndReturnMatches(StringDSFilter filter) {
            String[] matches = peekMatches(filter);
            for ( String item : matches ) {
                list.remove(item);
            }
            return matches;
        }

        @Override
        public int removeAndCountMatches(StringDSFilter filter) {
            // not the most efficient, but gets the job done 'quick and dirty'
            return removeAndReturnMatches(filter).length;
        }

        @Override
        public String[] removeAll() {
            String[] results = peekAll();
            clear();
            return results;
        }

        @Override
        public boolean contains(String item) {
            return list.contains(item);
        }

        @Override
        public String[] peekMatches(StringDSFilter filter) {
            List<String> resultList = new ArrayList<>();
            for ( String item : list ) {
                if ( filter.matches(item) ) {
                    resultList.add(item);
                }
            }
            return StringTools.toArray(resultList);
        }

        @Override
        public int countMatches(StringDSFilter filter) {
            return peekMatches(filter).length;
        }

        @Override
        public String[] peekAll() {
            return StringTools.toArray(list);
        }

        @Override
        public void performOnAll(StringDSAction action) {
            performOnMatches(new MatchEverythingStringDSFilter(), action);
        }

        @Override
        public int performOnMatches(StringDSFilter filter,
                                    StringDSAction action) {

            String[] matches = peekMatches(filter);
            for ( String item : matches ) {
                action.perform(item);
            }
            return matches.length;
        }

        @Override
        public StringDSIterator createIterator() {
            return new StringDSIterator() {
                private final Iterator<String> iterator = list.iterator();

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public String next() throws NoSuchElementException {
                    return iterator.next();
                }
            };
        }
    } // type QuickAndDirtyDSSack
}
