package com.abc.ds.keyvalue.tests;

import com.abc.ds.keyvalue.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/**
 * Used to produce key-value pairs with the fruit as a key and a string-length
 * of the fruit name formatted as a string as the value. For example:
 *   key="apple"
 *   value="5"
 */
public class TestFruitPairGenerator {
    private final TestFruitGenerator fruitGenerator;

    public TestFruitPairGenerator(TestFruitGenerator fruitGenerator) {
        ObjectTools.paramNullCheck(fruitGenerator, "fruitGenerator");
        this.fruitGenerator = fruitGenerator;
    }

    public TestFruitPairGenerator(long randomSeed) {
        this(new TestFruitGenerator(randomSeed));
    }

    public TestFruitPairGenerator() {
        this(new TestFruitGenerator());
    }

    /**
     * See {@link TestFruitGenerator#reset()}
     */
    public synchronized void reset() {
        fruitGenerator.reset();
    }

    /**
     * Returns the <em>next</em> count fruits in alphabetical order.
     * See {@link TestFruitGenerator#next(int)}
     */
    public DSKeyValuePair<String, String>[] next(int count) {
        return FruitPair.createArray(fruitGenerator.next(count));
    }

    /**
     * Returns count String's randomly chosen from the available fruit
     * and the returned result may include duplicates.
     * See {@link TestFruitGenerator#nextRandom(int)}
     */
    public DSKeyValuePair<String, String>[] nextRandom(int count) {
        return FruitPair.createArray(fruitGenerator.nextRandom(count));
    }

    /**
     * Returns the number of fruits requested in sequence.
     * See {@link TestFruitGenerator#getFruits(int)}.
     */
    public static DSKeyValuePair<String, String>[] getFruits(int count) {
        return FruitPair.createArray(TestFruitGenerator.getFruits(count));
    }

    /**
     * Returns the number of fruits requested in sequence starting at the
     * specified offset.
     * See {@link TestFruitGenerator#getFruits(int, int)}.
     */
    public static DSKeyValuePair<String, String>[] getFruits(int offset, int count) {
        return FruitPair.createArray(TestFruitGenerator.getFruits(offset, count));
    }

    private static class FruitPair extends AbstractDSKeyValuePair<String, String> {
        private final String key;
        private final String value;

        public FruitPair(String fruit) {
            key = fruit;
            value = String.format("%d", key.length());
        }

        public static FruitPair[] createArray(String[] fruits) {
            FruitPair[] pairs = new FruitPair[fruits.length];
            for ( int i = 0; i < pairs.length; i++ ) {
                pairs[i] = new FruitPair(fruits[i]);
            }
            return pairs;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    } // type FruitPair
}
