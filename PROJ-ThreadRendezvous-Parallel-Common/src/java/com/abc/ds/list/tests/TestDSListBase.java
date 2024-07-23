package com.abc.ds.list.tests;

import java.util.*;

import com.abc.ds.action.*;
import com.abc.ds.filter.*;
import com.abc.ds.iterator.*;
import com.abc.ds.list.*;
import com.abc.ds.sack.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
abstract class TestDSListBase extends TestDSBase {
    protected final DSListFactory factory;
    protected final TestDSHelper<String> testHelper;

    protected TestDSListBase(String subTitle,
                             DSListFactory factory) {

        super("DSList - " + subTitle);
        this.factory = factory;
        testHelper = new TestDSHelper.Builder<String>()
            .setItemType(String.class)
            .setTestAccess(testAccess)
            .setAllowDuplicates(true)
            .setOrderMatters(true)
            .setWrapItemsInQuotes(true)
            .create();
    }

    protected DSList<String> createDS() {
        outln("Creating a new DSList<String> instance...");
        DSList<String> ds = factory.create(String.class);
        outln("   ...created: " + ds.getClass().getCanonicalName());
        return ds;
    }

    protected void add(DSList<String> ds, String... items) {
        for ( String item : items ) {
            outln("add(" + StringTools.quoteWrap(item) + ")");
            ds.add(item);
        }
    }

    protected void addExpectGood(DSList<String> ds, String... items) {
        for ( String item : items ) {
            boolean result = ds.add(item);
            outln("add(" + StringTools.quoteWrap(item) + "), returned",
                result, true);
        }
    }

    protected void addExpectDuplicate(DSList<String> ds, String... items) {
        for ( String item : items ) {
            boolean result = ds.add(item);
            outln("add(" + StringTools.quoteWrap(item) +
                ") [duplicate], returned", result, true);
        }
    }

    protected void checkPeekAll(DSList<String> ds,
                                String... expectedItems) {

        testHelper.check("peekAll()", ds.peekAll(), expectedItems);
    }

    protected void checkPeekAllOnEmpty(DSList<String> ds) {
        checkPeekAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    protected void checkPeekMatches(DSList<String> ds,
                                    DSFilter<String> filter,
                                    String... expectedItems) {

        testHelper.check("peekMatches(filter)", ds.peekMatches(filter), expectedItems);
    }

//    protected void checkRemoveMatches(DSList<String> ds,
//                                      DSFilter<String> filter,
//                                      int expectedRemoveCount) {
//
//        int actualRemoveCount = ds.removeMatches(filter);
//        outln("removeMatches(filter), returned remove count",
//            actualRemoveCount, expectedRemoveCount);
//    }

    protected void checkRemoveAll(DSList<String> ds,
                                  String... expectedItems) {

        testHelper.check("removeAll()", ds.removeAll(), expectedItems);
    }

    protected void checkIterator(DSList<String> ds, String... expectedItems) {
        testHelper.check("createIterator()", ds.createIterator(), expectedItems);
    }

    protected void checkContains(DSList<String> ds,
                                 String item,
                                 boolean expectedResult) {

        outln("contains(" + StringTools.quoteWrap(item) + ")",
            ds.contains(item), expectedResult);
    }

    protected void checkCount(DSList<String> ds, int expectedResult) {
        outln("count()", ds.count(), expectedResult);
    }

    protected void checkIsEmpty(DSList<String> ds, boolean expectedResult) {
        outln("isEmpty()", ds.isEmpty(), expectedResult);
    }

    protected DSList<String> createAltStringSack() {
        return new QuickAndDirtyDSList();
    }


    protected void checkBadIndex(DSList<String> ds,
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

    protected void insertBeforeBadIndex(DSList<String> ds,
                                        final int index,
                                        final String item) {

        checkBadIndex(ds, new MethodAccess() {
            @Override
            public String formattedMethod() {
                return "insertBefore(" + index + ", " + item + ")";
            }

            @Override
            public void execute(DSList<String> ds2) {
                ds2.insertBefore(index, item);
            }
        });
    }

    protected static interface MethodAccess {
        String formattedMethod();
        void execute(DSList<String> ds);
    } // type MethodAccess

    private static class QuickAndDirtyDSList implements DSList<String> {
        private List<String> list;

        public QuickAndDirtyDSList() {
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
        public int addAll(DSSack<String> other) {
            return addAll(other.peekAll());
        }

        @Override
        public void insertBefore(int index, String item)
                throws IndexOutOfBoundsException {

            if ( index == count() ) {
                add(item);
            } else {
                list.add(index, item);
            }
        }

        @Override
        public void insertFirst(String item) {
            insertBefore(0, item);
        }

        @Override
        public int firstIndexOf(String item, int fromIndex) {
            for ( int i = Math.max(0, fromIndex); i < list.size(); i++ ) {
                if ( ObjectTools.isSame(item, list.get(i))) {
                    return i;
                }
            }
            return NOT_FOUND_INDEX;
        }

        @Override
        public int firstIndexOf(String item) {
            return firstIndexOf(item, 0);
        }

        @Override
        public int lastIndexOf(String item, int fromIndex) {
            for ( int i = Math.min(count() - 1, fromIndex); i >= 0; i-- ) {
                if ( ObjectTools.isSame(item, list.get(i))) {
                    return i;
                }
            }
            return NOT_FOUND_INDEX;
        }

        @Override
        public int lastIndexOf(String item) {
            return lastIndexOf(item, count() - 1);
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
        public String removeAtIndex(int index) throws IndexOutOfBoundsException {
            return list.remove(index);
        }

        @Override
        public String removeFirst() throws NoSuchElementException {
            if ( isEmpty() ) throw new NoSuchElementException();
            return removeAtIndex(0);
        }

        @Override
        public String removeLast() throws NoSuchElementException {
            if ( isEmpty() ) throw new NoSuchElementException();
            return removeAtIndex(count() - 1);
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
        public String peekAtIndex(int index) throws IndexOutOfBoundsException {
            return list.get(index);
        }

        @Override
        public String peekFirst() throws NoSuchElementException {
            if ( isEmpty() ) throw new NoSuchElementException();
            return peekAtIndex(0);
        }

        @Override
        public String peekLast() throws NoSuchElementException {
            if ( isEmpty() ) throw new NoSuchElementException();
            return peekAtIndex(count() - 1);
        }

        @Override
        public void replaceAtLindex(int index, String replacementItem)
                throws IndexOutOfBoundsException {

            list.set(index, replacementItem);
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

        @Override
        public DSIterator<String> createReverseIterator() {
            return new DSIterator<String>() {
                private int currIndex = count();

                @Override
                public boolean hasNext() {
                    int nextIndex = currIndex - 1;
                    return 0 <= nextIndex && nextIndex < count();
                }

                @Override
                public String next() throws NoSuchElementException {
                    if ( !hasNext() ) throw new NoSuchElementException();

                    currIndex--;
                    return list.get(currIndex);
                }
            };
        }
    } // type QuickAndDirtyDSList
}
