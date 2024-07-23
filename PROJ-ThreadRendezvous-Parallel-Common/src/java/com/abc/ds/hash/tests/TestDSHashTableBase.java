package com.abc.ds.hash.tests;

import java.util.*;

import com.abc.ds.*;
import com.abc.ds.filter.*;
import com.abc.ds.hash.*;
import com.abc.ds.keyvalue.*;
import com.abc.ds.keyvalue.tests.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
abstract class TestDSHashTableBase extends TestDSBase {
    @SuppressWarnings("unchecked")
    public static final Class<DSKeyValuePair<String, String>> STRING_KV_TYPE =
        (Class<DSKeyValuePair<String, String>>) DSTools.coerceClassType(DSKeyValuePair.class);

    public static final DSKeyValuePair<String, String>[] STRING_KV_ZERO_LEN_ARRAY =
        DSKeyValueTools.createArray(0, String.class, String.class);

    public static final DSKeyValuePair<String, String> PAIR_OCEAN =
        new BasicDSKeyValuePair<String, String>("ocean", "water");
    public static final DSKeyValuePair<String, String> PAIR_GLACIER =
        new BasicDSKeyValuePair<String, String>("glacier", "ice");
    public static final DSKeyValuePair<String, String> PAIR_VOLCANO =
        new BasicDSKeyValuePair<String, String>("volcano", "magma");
    public static final DSKeyValuePair<String, String> PAIR_LAKE =
        new BasicDSKeyValuePair<String, String>("lake", "water");
    public static final DSKeyValuePair<String, String> PAIR_BEACH =
        new BasicDSKeyValuePair<String, String>("beach", "sand");
    public static final DSKeyValuePair<String, String> PAIR_SCHOOL =
        new BasicDSKeyValuePair<String, String>("school", "children");


    protected final DSHashTableFactory factory;
    protected final TestDSHelper<DSKeyValuePair<String, String>> testHelper;
    protected final TestDSHelper<String> stringTestHelper;

    protected TestDSHashTableBase(String subTitle, DSHashTableFactory factory) {
        super("DSHashTable - " + subTitle);
        this.factory = factory;
        testHelper = new TestDSHelper.Builder<DSKeyValuePair<String, String>>()
            .setItemType(STRING_KV_TYPE)
            .setTestAccess(testAccess)
            .setAllowDuplicates(false)
            .setOrderMatters(false)
            .setWrapItemsInQuotes(true)
            .create();
        stringTestHelper = new TestDSHelper.Builder<String>()
            .setItemType(String.class)
            .setTestAccess(testAccess)
            .setAllowDuplicates(false)
            .setOrderMatters(false)
            .setWrapItemsInQuotes(true)
            .create();
    }

    protected DSHashTable<String, String> createDS(int bucketCount) {
        outln("Creating a new DSHashTable<String, String> instance" +
            " with bucketCount=" + bucketCount + "...");
        DSHashTable<String, String> ht = factory.create(
            String.class, String.class, bucketCount, 0.0);
        outln("   ...created: " + ht.getClass().getCanonicalName());
        return ht;
    }

    protected DSHashTable<String, String> createDS() {
        return createDS(11);
    }
    protected void insert(DSHashTable<String, String> ht, String key, String value) {
        outln("insert(" + StringTools.quoteWrap(key) + ", " +
            StringTools.quoteWrap(value) + ")");
        ht.insert(key, value);
    }

    @SuppressWarnings("unchecked")
    protected void insert(DSHashTable<String, String> ht,
                          DSKeyValuePair<String, String>... pairs) {

        for ( DSKeyValuePair<String, String> pair : pairs ) {
            insert(ht, pair.getKey(), pair.getValue());
        }
    }

    protected DSKeyValuePair<String, String>[] getFruitPairs(int offset,
                                                             int count) {

        return TestFruitPairGenerator.getFruits(offset, count);
    }

    protected DSKeyValuePair<String, String>[] getFruitPairs(int count) {
        return getFruitPairs(0, count);
    }

    /**
     * Removes duplicate keys retaining the LAST key-value pair (since the
     * value might be different for duplicate keys).
     * New array returned, passed array is not modified.
     */
    @SuppressWarnings("unchecked")
    protected DSKeyValuePair<String, String>[] removeDuplicateKeys(
            DSKeyValuePair<String, String>[] origPairs) {

        Map<String, DSKeyValuePair<String, String>> map =
            new LinkedHashMap<>();
        for ( DSKeyValuePair<String, String> origPair : origPairs ) {
            map.put(origPair.getKey(), origPair);
        }
        return map.values().toArray(
            DSTools.createArrayFromType(DSKeyValuePair.class, map.size()));
    }

    /**
     * Returns a new array contains the shuffled items from the specified
     * array.
     * If random is null, the same random seed (0x00000000feedface) is
     * used each time to guarantee consistent results from run to run.
     * New array returned, passed array is not modified.
     */
    @SuppressWarnings("unchecked")
    protected DSKeyValuePair<String, String>[] shuffle(
            DSKeyValuePair<String, String>[] origPairs,
            Random random) {

        random = random == null ? new Random(0x00000000feedface) : random;

        List<DSKeyValuePair<String, String>> list = new
            ArrayList<>(Arrays.asList(origPairs));
        Collections.shuffle(list, random);
        return list.toArray(
            DSTools.createArrayFromType(DSKeyValuePair.class, list.size()));
    }

    protected String[] pairsToStrings(DSKeyValuePair<String, String>[] pairs) {
        return StringKeyValue.toStrings(StringKeyValue.createArray(pairs));
    }

    @SuppressWarnings("unchecked")
    protected void checkPeekAll(DSHashTable<String, String> ds,
                                DSKeyValuePair<String, String>... expectedItems) {

        stringTestHelper.check("peekAll()",
            pairsToStrings(ds.peekAll()),
            pairsToStrings(expectedItems));
    }

    protected void checkPeekAllOnEmpty(DSHashTable<String, String> ds) {
        checkPeekAll(ds, STRING_KV_ZERO_LEN_ARRAY);
    }

    @SuppressWarnings("unchecked")
    protected void checkPeekKeyMatches(DSHashTable<String, String> ds,
                                       DSFilter<String> keyFilter,
                                       DSKeyValuePair<String, String>... expectedItems) {

        stringTestHelper.check("peekKeyMatches(filter)",
            pairsToStrings(ds.peekKeyMatches(keyFilter)),
            pairsToStrings(expectedItems));
    }

    protected void checkCount(DSHashTable<String, String> ds,
                              int expectedResult) {
        outln("count()", ds.count(), expectedResult);
    }

    protected void checkIsEmpty(DSHashTable<String, String> ds,
                                boolean expectedResult) {
        outln("isEmpty()", ds.isEmpty(), expectedResult);
    }


//    protected void checkArrayMatch(DSKeyValuePair<String, String>[] results,
//                                   DSKeyValuePair<String, String>[] expecteds) {
//
//        String[] resultStrings = StringKeyValue.toStrings(
//            StringKeyValue.createArray(results));
//        String[] expectedStrings = StringKeyValue.toStrings(
//            StringKeyValue.createArray(expecteds));
//
//        checkArrayMatch(resultStrings, expectedStrings);
//    }

    @SuppressWarnings("unchecked")
    protected void checkNoNullSlots(DSKeyValuePair<String, String>... pairs) {
        if ( ObjectTools.isNoSlotEmpty(pairs) ) return;

        if ( pairs == null ) {
            throw new NullPointerException(
                "DSKeyValuePair[] reference is null, expected an array");
        }

        for ( int i = 0; i < pairs.length; i++ ) {
            if ( pairs[i] == null ) {
                throw new NullPointerException(
                    "index " + i + " of DSKeyValuePair[] is null");
            }
        }
    }


//    protected boolean isSame(DSKeyValuePair<String, String> a,
//                             DSKeyValuePair<String, String> b) {
//
//        if ( a == b ) {
//            // both pointing to same object, OR both are null
//            return true;
//        } else if ( a == null || b == null ) {
//            // one is null and the other is NOT null
//            return false;
//        } else {
//            return
//                ObjectTools.isSame(a.getKey(), b.getKey()) &&
//                ObjectTools.isSame(a.getValue(), b.getValue());
//        }
//    }

//    protected boolean isSameKey(DSKeyValuePair<String, String> a,
//                                DSKeyValuePair<String, String> b) {
//
//        if ( a == b ) {
//            // both pointing to same object, OR both are null
//            return true;
//        } else if ( a == null || b == null ) {
//            // one is null and the other is NOT null
//            return false;
//        } else {
//            return ObjectTools.isSame(a.getKey(), b.getKey());
//        }
//    }

    protected static final class StringKeyValue
            extends AbstractDSKeyValuePair<String, String> {

        public static final StringKeyValue[] ZERO_LEN_ARRAY = new StringKeyValue[0];

        private final String key;
        private final String value;

        public StringKeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public StringKeyValue(DSKeyValuePair<String, String> pair) {
            this(pair.getKey(), pair.getValue());
        }

        @SuppressWarnings("unchecked")
        public static StringKeyValue[] createArray(
                DSKeyValuePair<String, String>... pairs) {

            if ( ObjectTools.isEmpty(pairs) ) return ZERO_LEN_ARRAY;

            StringKeyValue[] results = new StringKeyValue[pairs.length];
            for ( int i = 0; i < results.length; i++ ) {
                results[i] = pairs[i] == null ? null : new StringKeyValue(pairs[i]);
            }
            return results;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        public static String[] toStrings(StringKeyValue... pairs) {
            if ( ObjectTools.isEmpty(pairs) ) return StringTools.ZERO_LEN_ARRAY;

            String[] results = new String[pairs.length];
            for ( int i = 0; i < results.length; i++ ) {
                results[i] = pairs[i] == null ? null : pairs[i].toString();
            }
            return results;
        }
    } // type StringKeyValue
}
