package com.abc.ds.tests;

import java.math.*;
import java.util.*;

import com.abc.ds.*;
import com.abc.ds.iterator.*;
import com.programix.thread.*;
import com.programix.util.*;

public class TestDSHelper<T> {
    private final Class<T> itemType;
    private final TestAccess ta;
    private final boolean allowDuplicates;
    private final boolean orderMatters;
    private final boolean wrapItemsInQuotes;
    public final T[] itemZeroLenArray;

    // private, create instances using Builder
    private TestDSHelper(Class<T> itemType,
                        TestAccess testAccess,
                        boolean allowDuplicates,
                        boolean orderMatters,
                        boolean wrapItemsInQuotes) {

        this.itemType = itemType;
        this.ta = testAccess;
        this.allowDuplicates = allowDuplicates;
        this.orderMatters = orderMatters;
        this.wrapItemsInQuotes = wrapItemsInQuotes;
        itemZeroLenArray = DSTools.createArrayFromType(itemType, 0);
    }

    public Class<T> getItemType() {
        return itemType;
    }

    public boolean isAllowDuplicates() {
        return allowDuplicates;
    }

    public boolean isOrderMatters() {
        return orderMatters;
    }

    public void check(String prefix,
                      T[] resultItems,
                      T[] expectedItems) {

        if ( ObjectTools.isEmpty(expectedItems) ) {
            if ( resultItems == null ) {
                ta.outln(prefix + " results null, expected a zero-length array", false);
            } else if ( resultItems.length > 0 ) {
                ta.outln(prefix + " results an array with length=" +
                    resultItems.length + ", expected a zero-length array", false);
            } else {
                ta.outln(prefix + " results a zero-length array", true);
            }
        } else if ( resultItems == null ) {  // expectedItems != null
            ta.outln(prefix + " results null but " +
                "expected array is not null and has length of " +
                expectedItems.length, false);
        } else if ( ObjectTools.isSameArray(resultItems, expectedItems) ) {
            String suffix = resultItems.length > 100
                ? "[too many to list here]"
                : "[" + formatCommaDelimited(resultItems) + "]";
            ta.outln(prefix + " returned " + resultItems.length +
                " items which match what is expected " + suffix, true);
        } else if ( orderMatters ) {
            // neither is null, they don't match exactly

            ta.outln(prefix + " result an array whose ordered items don't match what was expected", false);
            String lengthText = "    resultItems.length=" + resultItems.length + ", expectedItems.length=" + expectedItems.length;
            if ( resultItems.length == expectedItems.length ) {
                ta.outln(lengthText);
            } else {
                ta.outlnErrorText(lengthText);
            }

            int lenOfLonger = Math.max(resultItems.length, expectedItems.length);
            boolean printMatchesToo = expectedItems.length <= 100;
            ta.outln(String.format(
                "    %5.5s %-20.20s %-20.20s", "Index", "Result", "Expected"));
            ta.outln(String.format(
                "    %5.5s %-20.20s %-20.20s", "-----", "-----", "---------"));
            for ( int i = 0; i < lenOfLonger; i++ ) {
                boolean hasResult = i < resultItems.length;
                boolean hasExpected = i < expectedItems.length;
                boolean matches =
                    hasResult &&
                    hasExpected &&
                    ObjectTools.isSame(resultItems[i], expectedItems[i]);

                boolean printLine = !matches || printMatchesToo;
                if ( printLine ) {
                    String text = String.format("    %5d %-20.20s %-20.20s",
                        i,
                        hasResult ? StringTools.quoteWrap(resultItems[i]) : "",
                        hasExpected ? StringTools.quoteWrap(expectedItems[i]) : ""
                        );

                    if ( matches ) {
                        ta.outln(text);
                    } else {
                        ta.outlnErrorText(text);
                    }
                }
            }
        } else {
            // neither is null, they don't match exactly, and order doesn't matter

            ItemStore expectedStore = new ItemStore();
            expectedStore.addAll(expectedItems);

            ItemStore resultStore = new ItemStore();
            resultStore.addAll(resultItems);

            ItemStore unexpectedStore = resultStore.createCopy();
            unexpectedStore.subtract(expectedStore);
            boolean anyUnexpected = !unexpectedStore.isEmpty();

            ItemStore expectedButNotFoundStore = expectedStore.createCopy();
            expectedButNotFoundStore.subtract(resultStore);
            boolean anyExpectedButNotFound = !expectedButNotFoundStore.isEmpty();

            boolean allGood = !anyUnexpected && !anyExpectedButNotFound;

            if ( allGood ) {
                String suffix = resultItems.length > 100
                    ? "[too many to list here]"
                    : "[" + formatCommaDelimited(resultItems) + "]";
                ta.outln(prefix + " results " + resultItems.length +
                    " items which match what is expected" + suffix, true);
            } else {
                ta.outln(prefix + " results don't match what was expected", false);
                String lengthText = "    result item count=" + resultStore.getCount() +
                    ", expected item count=" + expectedStore.getCount();
                if ( resultItems.length == expectedItems.length ) {
                    ta.outln(lengthText);
                } else {
                    ta.outlnErrorText(lengthText);
                }

                ItemStore remainingExpectedStore = expectedStore.createCopy();
                for ( T result : resultStore.getAllItems() ) {
                    boolean found = remainingExpectedStore.removeCountOfItem(result, 1) > 0;
                    String text = "    " + StringTools.quoteWrap(result) + " " +
                        (found ? "expected and found" : "unexpected");
                    if ( found ) {
                        ta.outln(text);
                    } else {
                        ta.outlnErrorText(text);
                    }
                }

                for ( T expected : expectedButNotFoundStore.getAllItems() ) {
                    ta.outlnErrorText("    " + StringTools.quoteWrap(expected) +
                        " expected, but not found");
                }
            }
        }
    }

    public void check(String prefix,
                      DSIterator<T> iterator,
                      T[] expectedItems) {

        T[] resultItems = DSTools.iteratorToArray(itemType, iterator);
        check(prefix, resultItems, expectedItems);
    }


    /**
     * Removes duplicates maintaining the order of original appearance.
     * New array returned, passed array is not modified.
     */
    public T[] removeDuplicates(T[] items) {
        if ( ObjectTools.isEmpty(items) ) return itemZeroLenArray;
        Set<T> set = new LinkedHashSet<>(Arrays.asList(items));
        return set.toArray(itemZeroLenArray);
    }

    /**
     * Returns a new array containing the shuffled items from the specified
     * array.
     * If random is null, the same random seed (0x00000000feedface) is
     * used each time to guarantee consistent results from run to run.
     * New array returned, passed array is not modified.
     */
    public T[] shuffle(T[] items, Random random) {
        if ( ObjectTools.isEmpty(items) ) return itemZeroLenArray;
        random = random == null ? new Random(0x00000000feedface) : random;
        List<T> list = new ArrayList<>(Arrays.asList(items));
        Collections.shuffle(list, random);
        return list.toArray(itemZeroLenArray);
    }

    /**
     * Returns a new array containing the shuffled items from the specified
     * array.
     * The same random seed (0x00000000feedface) is
     * used each time to guarantee consistent results from run to run.
     * New array returned, passed array is not modified.
     */
    public T[] shuffle(T[] items) {
        return shuffle(items, null);
    }

    /**
     * Returns a new array after potentially removing duplicates and/or
     * potentially shuffling.
     * If duplicates are not allowed, any duplicates are removed.
     * If order does not matter, the items are shuffled in a way which
     * is the same each time this method is called.
     */
    public T[] potentiallyRemoveDuplicatesAndShuffle(T[] items) {
        if ( ObjectTools.isEmpty(items) ) return itemZeroLenArray;

        if ( allowDuplicates ) {
            if ( orderMatters ) {
                return items.clone();
            } else {
                return shuffle(items);
            }
        } else {
            if ( orderMatters ) {
                return removeDuplicates(items);
            } else {
                return shuffle(removeDuplicates(items));
            }
        }
    }

//
//    public ItemStore createItemStore() {
//        return new ItemStore();
//    }

    private String formatCommaDelimited(Object[] items) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for ( Object item : items ) {
            if ( first ) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(wrapItemsInQuotes ? StringTools.quoteWrap(item) : String.valueOf(item));
        }
        return sb.toString();
    }

    private class ItemStore {
        private final Map<T, Counter> map;
        private int count;

        public ItemStore() {
            map = new LinkedHashMap<>();
            count = 0;
        }

        public int getCount() {
            return count;
        }

        public boolean isEmpty() {
            return count == 0;
        }

        @SuppressWarnings("unused")
        public void clear() {
            map.clear();
            count = 0;
        }

        public ItemStore createCopy() {
            ItemStore copy = new ItemStore();
            for ( Map.Entry<T, Counter> entry : map.entrySet() ) {
                copy.map.put(entry.getKey(), new Counter(entry.getValue().getCount()));
            }
            copy.count = count;
            return copy;
        }

        @SuppressWarnings("unused")
        public boolean contains(T item) {
            return map.containsKey(item);
        }

        public T[] getAllItems() {
            List<T> itemList = new ArrayList<>();
            for ( Map.Entry<T, Counter> entry : map.entrySet() ) {
                T item = entry.getKey();
                Counter counter = entry.getValue();
                for ( int i = 0; i < counter.getCount(); i++ ) {
                    itemList.add(item);
                }
            }
            return itemList.toArray(
                DSTools.createArrayFromType(itemType, itemList.size()));
        }

        public void addAll(T[] items) {
            for ( T item : items ) {
                Counter counter = map.get(item);
                if ( counter == null ) {
                    counter = new Counter(1);
                    map.put(item, counter);
                    count++;
                } else if ( allowDuplicates ) {
                    counter.increment();
                    count++;
                }
            }
        }

        public int removeCountOfItem(T item, int countToRemove) {
            Counter counter = map.get(item);
            if ( counter == null ) {
                // other item not in this set, so nothing to do
                return 0;
            } else if ( counter.getCount() <= countToRemove ) {
                // subtracting everything, so remove from map
                map.remove(item);
                count -= counter.getCount();
                return counter.getCount();
            } else {
                for ( int i = 0; i < countToRemove; i++ ) {
                    counter.decrement();
                }
                count -= countToRemove;
                return countToRemove;
            }
        }

        public void subtract(ItemStore otherStore) {
            for ( Map.Entry<T, Counter> otherEntry : otherStore.map.entrySet() ) {
                T otherKey = otherEntry.getKey();
                Counter otherCounter = otherEntry.getValue();
                removeCountOfItem(otherKey, otherCounter.getCount());
            }
        }

    } // type ItemStore

    public static class Builder<T> {
        private Class<T> itemType;
        private TestAccess testAccess;
        private boolean allowDuplicates = true;
        private boolean orderMatters = true;
        private boolean wrapItemsInQuotes = true;

        public Builder() {
        }

        public TestDSHelper<T> create() {
            if ( itemType == null ) {
                throw new IllegalArgumentException("itemType must be specified");
            }
            if ( testAccess == null ) {
                throw new IllegalArgumentException("testAccess must be specified");
            }
            return new TestDSHelper<>(
                itemType, testAccess, allowDuplicates, orderMatters, wrapItemsInQuotes);
        }

        /**
         * Required, specifies the item type.
         */
        public Builder<T> setItemType(Class<T> itemType) {
            this.itemType = itemType;
            return this;
        }

        /**
         * Required, specified the {@link TestAccess} to use.
         */
        public Builder<T> setTestAccess(TestAccess testAccess) {
            this.testAccess = testAccess;
            return this;
        }

        /**
         * Optional, whether duplicates are allowed or not, defaults to true.
         */
        public Builder<T> setAllowDuplicates(boolean allowDuplicates) {
            this.allowDuplicates = allowDuplicates;
            return this;
        }

        /**
         * Optional, whether the order of items matters, defaults to true.
         */
        public Builder<T> setOrderMatters(boolean orderMatters) {
            this.orderMatters = orderMatters;
            return this;
        }

        /**
         * Optional, whether formatted items should be wrapped in quotes, defaults to true.
         */
        public Builder<T> setWrapItemsInQuotes(boolean wrapItemsInQuotes) {
            this.wrapItemsInQuotes = wrapItemsInQuotes;
            return this;
        }
    } // type Builder

    public static interface TestAccess {
        void outln(String msg, boolean success);
        void outln(String msg);
        void outlnErrorText(String msg);

        void outln(String msg, boolean result, boolean expectedResult);
        void outln(String msg, String result, String expectedResult);
        void outln(String msg, Object result, Object expectedResult);
        void outln(String msg, BigDecimal result, BigDecimal expectedResult);
        void outln(String msg, int result, int expectedResult);
        void outln(String msg, int result, int expectedResult, int tolerance);
        void outln(String msg, long result, long expectedResult);
        void outln(String msg, long result, long expectedResult, long tolerance);
        void outln(String msg, double result, double expectedResult, double tolerance, int decimalPlaces);
        void outln(String msg, Object[] result, Object[] expectedResult);
        void outln(String msg, byte[] result, byte[] expectedResult);
        void outln(String msg, int[] result, int[] expectedResult);
    } // type TestAccess

    /*
    public static void main(String[] args) {
        TestAccess fakeTa = new TestAccess() {
            @Override
            public void outln(String msg, boolean success) {
                if ( success ) {
                    System.out.println("PASS: " + msg);
                } else {
                    System.out.println("FAIL: " + msg);
                }
            }

            @Override
            public void outln(String msg) {
                System.out.println(msg);
            }

            @Override
            public void outlnErrorText(String msg) {
                System.out.println(msg);
            }
        };

        TestDSHelper<String> helper = new TestDSHelper.Builder<String>()
            .setItemType(String.class)
            .setTestAccess(fakeTa)
            .setAllowDuplicates(true)
            .setOrderMatters(false)
            .create();

//        helper.check("peekAll()", arr("b", "a", "b", "c", "b"), arr("a", "b", "c"));
//        helper.check("peekAll()", arr("b", "a", "b", "c"), arr("a", "b", "c", "d"));
//        helper.check("peekAll()", arr("b", "a", "b", "c", "e"), arr("a", "b", "c", "d"));

        System.out.println("===================================================");
        TestDSHelper<String>.ItemStore storeA = helper.createItemStore();
        TestDSHelper<String>.ItemStore storeB = helper.createItemStore();

        storeA.addAll(arr("a", "d", "b", "c", "b", "c", "d", "c"));

        System.out.println("storeA.getCount()=" + storeA.getCount());
        if ( storeA.getAllItems() == null ) {
            System.out.println("storeA.getAllItems()=null");
        } else {
            System.out.println("storeA.getAllItems().length=" + storeA.getAllItems().length);
            for ( int i = 0; i < storeA.getAllItems().length; i++ ) {
                System.out.println("  storeA.getAllItems()[" + i + "]=" + storeA.getAllItems()[i]);
            }
        }

        storeA.removeCountOfItem("c", 2);

        System.out.println("storeA.getCount()=" + storeA.getCount());
        if ( storeA.getAllItems() == null ) {
            System.out.println("storeA.getAllItems()=null");
        } else {
            System.out.println("storeA.getAllItems().length=" + storeA.getAllItems().length);
            for ( int i = 0; i < storeA.getAllItems().length; i++ ) {
                System.out.println("  storeA.getAllItems()[" + i + "]=" + storeA.getAllItems()[i]);
            }
        }

        storeB.addAll(arr("d", "b", "a", "x", "d"));
        if ( storeB.getAllItems() == null ) {
            System.out.println("storeB.getAllItems()=null");
        } else {
            System.out.println("storeB.getAllItems().length=" + storeB.getAllItems().length);
            for ( int i = 0; i < storeB.getAllItems().length; i++ ) {
                System.out.println("  storeB.getAllItems()[" + i + "]=" + storeB.getAllItems()[i]);
            }
        }

        System.out.println("subtracting storeB from storeA -------------------------------------------");
        storeA.subtract(storeB);
        if ( storeA.getAllItems() == null ) {
            System.out.println("storeA.getAllItems()=null");
        } else {
            System.out.println("storeA.getAllItems().length=" + storeA.getAllItems().length);
            for ( int i = 0; i < storeA.getAllItems().length; i++ ) {
                System.out.println("  storeA.getAllItems()[" + i + "]=" + storeA.getAllItems()[i]);
            }
        }
    }

    private static String[] arr(String...strings) {
        return strings;
    }
     */
}
