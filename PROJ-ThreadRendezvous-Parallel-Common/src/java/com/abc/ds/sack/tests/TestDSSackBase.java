package com.abc.ds.sack.tests;

import java.util.*;

import com.abc.ds.action.*;
import com.abc.ds.filter.*;
import com.abc.ds.iterator.*;
import com.abc.ds.sack.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
abstract class TestDSSackBase extends TestDSBase {
    protected final DSSackFactory factory;
    protected final boolean allowDuplicates;
    protected final boolean orderMatters;
    protected final TestDSHelper<String> testHelper;

    protected TestDSSackBase(String subTitle,
                             DSSackFactory factory) {

        super("DSSack - " + subTitle);
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

    protected DSSack<String> createDS() {
        outln("Creating a new DSSack<String> instance...");
        DSSack<String> ds = factory.create(String.class);
        outln("   ...created: " + ds.getClass().getCanonicalName());
        return ds;
    }

    protected void add(DSSack<String> ds, String... items) {
        for ( String item : items ) {
            outln("add(" + StringTools.quoteWrap(item) + ")");
            ds.add(item);
        }
    }

    protected void addExpectGood(DSSack<String> ds, String... items) {
        for ( String item : items ) {
            boolean result = ds.add(item);
            outln("add(" + StringTools.quoteWrap(item) + "), returned",
                result, true);
        }
    }

    protected void addExpectDuplicate(DSSack<String> ds, String... items) {
        for ( String item : items ) {
            boolean result = ds.add(item);
            outln("add(" + StringTools.quoteWrap(item) +
                ") [duplicate], returned", result, allowDuplicates);
        }
    }

    protected void checkPeekAll(DSSack<String> ds,
                                String... expectedItems) {

        testHelper.check("peekAll()", ds.peekAll(), expectedItems);
    }

    protected void checkPeekAllOnEmpty(DSSack<String> ds) {
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    protected void checkPeekMatches(DSSack<String> ds,
                                    DSFilter<String> filter,
                                    String... expectedItems) {

        testHelper.check("peekMatches(filter)", ds.peekMatches(filter), expectedItems);
    }

//    protected void checkRemoveMatches(DSSack<String> ds,
//                                      DSFilter<String> filter,
//                                      int expectedRemoveCount) {
//
//        int actualRemoveCount = ds.removeMatches(filter);
//        outln("removeMatches(filter), returned remove count",
//            actualRemoveCount, expectedRemoveCount);
//    }

    protected void checkRemoveAll(DSSack<String> ds,
                                String... expectedItems) {

        testHelper.check("removeAll()", ds.removeAll(), expectedItems);
    }

    protected void checkIterator(DSSack<String> ds, String... expectedItems) {
        testHelper.check("createIterator()", ds.createIterator(), expectedItems);
    }

    protected void checkContains(DSSack<String> ds,
                                 String item,
                                 boolean expectedResult) {

        outln("contains(" + StringTools.quoteWrap(item) + ")",
            ds.contains(item), expectedResult);
    }

    protected void checkCount(DSSack<String> ds, int expectedResult) {
        outln("count()", ds.count(), expectedResult);
    }

    protected void checkIsEmpty(DSSack<String> ds, boolean expectedResult) {
        outln("isEmpty()", ds.isEmpty(), expectedResult);
    }

    protected DSSack<String> createAltStringSack() {
        return new QuickAndDirtyDSSack();
    }

    private static class QuickAndDirtyDSSack implements DSSack<String> {
        private List<String> list;

        public QuickAndDirtyDSSack() {
            list = new ArrayList<>();
        }

        @Override
        public Class<String> getItemType() {
            return String.class;
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
        public int addAll(DSSack<String> otherSack) {
            return addAll(otherSack.peekAll());
        }

        @Override
        public boolean remove(String item) {
            return list.remove(item);
        }

        @Override
        public String[] removeAndReturnMatches(DSFilter<String> filter) {
            String[] matches = peekMatches(filter);
            for ( String item : matches ) {
                list.remove(item);
            }
            return matches;
        }

        @Override
        public int removeAndCountMatches(DSFilter<String> filter) {
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
        public String[] peekMatches(DSFilter<String> filter) {
            List<String> resultList = new ArrayList<>();
            for ( String item : list ) {
                if ( filter.matches(item) ) {
                    resultList.add(item);
                }
            }
            return StringTools.toArray(resultList);
        }

        @Override
        public int countMatches(DSFilter<String> filter) {
            return peekMatches(filter).length;
        }

        @Override
        public String[] peekAll() {
            return StringTools.toArray(list);
        }

        @Override
        public void performOnAll(DSAction<String> action) {
            performOnMatches(new MatchEverythingDSFilter<String>(), action);
        }

        @Override
        public int performOnMatches(DSFilter<String> filter,
                                    DSAction<String> action) {

            String[] matches = peekMatches(filter);
            for ( String item : matches ) {
                action.perform(item);
            }
            return matches.length;
        }

        @Override
        public DSIterator<String> createIterator() {
            return new DSIterator<String>() {
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
