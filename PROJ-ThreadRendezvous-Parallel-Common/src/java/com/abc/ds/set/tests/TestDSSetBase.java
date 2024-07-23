package com.abc.ds.set.tests;

import java.util.*;

import com.abc.ds.action.*;
import com.abc.ds.filter.*;
import com.abc.ds.iterator.*;
import com.abc.ds.sack.*;
import com.abc.ds.set.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
abstract class TestDSSetBase extends TestDSBase {
    protected final DSSetFactory factory;
    protected final boolean allowDuplicates;
    protected final boolean orderMatters;
    protected final TestDSHelper<String> testHelper;

    protected TestDSSetBase(String subTitle,
                             DSSetFactory factory) {

        super("DSSet - " + subTitle);
        this.factory = factory;
        allowDuplicates = false;
        orderMatters = factory.orderMatters();
        testHelper = new TestDSHelper.Builder<String>()
            .setItemType(String.class)
            .setTestAccess(testAccess)
            .setAllowDuplicates(allowDuplicates)
            .setOrderMatters(orderMatters)
            .setWrapItemsInQuotes(true)
            .create();
    }

    protected DSSet<String> createDS() {
        outln("Creating a new DSSet<String> instance...");
        DSSet<String> ds = factory.create(String.class);
        outln("   ...created: " + ds.getClass().getCanonicalName());
        return ds;
    }

    protected void add(DSSet<String> ds, String... items) {
        for ( String item : items ) {
            outln("add(" + StringTools.quoteWrap(item) + ")");
            ds.add(item);
        }
    }

    protected void addExpectGood(DSSet<String> ds, String... items) {
        for ( String item : items ) {
            boolean result = ds.add(item);
            outln("add(" + StringTools.quoteWrap(item) + "), returned",
                result, true);
        }
    }

    protected void addExpectDuplicate(DSSet<String> ds, String... items) {
        for ( String item : items ) {
            boolean result = ds.add(item);
            outln("add(" + StringTools.quoteWrap(item) +
                ") [duplicate], returned", result, allowDuplicates);
        }
    }

    protected void checkPeekAll(DSSet<String> ds,
                                String... expectedItems) {

        testHelper.check("peekAll()", ds.peekAll(), expectedItems);
    }

    protected void checkPeekAllOnEmpty(DSSet<String> ds) {
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    protected void checkPeekMatches(DSSet<String> ds,
                                    DSFilter<String> filter,
                                    String... expectedItems) {

        testHelper.check("peekMatches(filter)", ds.peekMatches(filter), expectedItems);
    }

    protected void checkRemoveAll(DSSet<String> ds,
                                String... expectedItems) {

        testHelper.check("removeAll()", ds.removeAll(), expectedItems);
    }

    protected void checkIterator(DSSet<String> ds, String... expectedItems) {
        testHelper.check("createIterator()", ds.createIterator(), expectedItems);
    }

    protected void checkContains(DSSet<String> ds,
                                 String item,
                                 boolean expectedResult) {

        outln("contains(" + StringTools.quoteWrap(item) + ")",
            ds.contains(item), expectedResult);
    }

    protected void checkCount(DSSet<String> ds, int expectedResult) {
        outln("count()", ds.count(), expectedResult);
    }

    protected void checkIsEmpty(DSSet<String> ds, boolean expectedResult) {
        outln("isEmpty()", ds.isEmpty(), expectedResult);
    }


    protected DSSet<String> createAltStringSet() {
        return new QuickAndDirtyDSSet();
    }

    private static class QuickAndDirtyDSSet implements DSSet<String> {
        private Set<String> set;

        public QuickAndDirtyDSSet() {
            set = new HashSet<>();
        }

        @Override
        public Class<String> getItemType() {
            return String.class;
        }

        @Override
        public int count() {
            return set.size();
        }

        @Override
        public boolean isEmpty() {
            return count() == 0;
        }

        @Override
        public void clear() {
            set.clear();
        }

        @Override
        public boolean add(String item) {
            return set.add(item);
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
            return set.remove(item);
        }

        @Override
        public int removeAndCountMatches(DSFilter<String> filter) {
            int removeCount = 0;
            for ( String item : peekMatches(filter) ) {
                if ( set.remove(item) ) {
                    removeCount++;
                }
            }
            return removeCount;
        }

        @Override
        public String[] removeAndReturnMatches(DSFilter<String> filter) {
            String[] matches = peekMatches(filter);
            for ( String item : matches ) {
                remove(item);
            }
            return matches;
        }

        @Override
        public String[] removeAll() {
            String[] results = peekAll();
            clear();
            return results;
        }

        @Override
        public boolean contains(String item) {
            return set.contains(item);
        }

        @Override
        public int countMatches(DSFilter<String> filter) {
            return peekMatches(filter).length;
        }

        @Override
        public String[] peekMatches(DSFilter<String> filter) {
            List<String> resultList = new ArrayList<>();
            for ( String item : set ) {
                if ( filter.matches(item) ) {
                    resultList.add(item);
                }
            }
            return StringTools.toArray(resultList);
        }

        @Override
        public String[] peekAll() {
            return StringTools.toArray(set);
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
                private final Iterator<String> iterator = set.iterator();

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

        @Override
        public int union(DSSet<String> otherSet) {
            int countBefore = set.size();
            set.addAll(new HashSet<>(Arrays.asList(otherSet.peekAll())));
            return set.size() - countBefore;
        }

        @Override
        public void intersection(DSSet<String> otherSet) {
            set.retainAll(new HashSet<>(Arrays.asList(otherSet.peekAll())));
        }

        @Override
        public void subtract(DSSet<String> otherSet) {
            set.removeAll(new HashSet<>(Arrays.asList(otherSet.peekAll())));
        }
    } // type QuickAndDirtyDSSet
}
