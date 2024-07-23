package com.abc.ds.tests;

import java.util.*;

public class TestIntGenerator {
    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' int's which has no duplicates
     * in the first 20, and always starts with:
     * <pre>
     * 120, 360, 110, 310, 350, 320, 200, 290, 220, 180
     * </pre>
     */
    public static final long RANDOM_SEED_20 = 0xfeed0cabL; // feed-O-cab

    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' int's which has no duplicates
     * in the first 15, and always starts with:
     * <pre>
     * 220, 300, 330, 180, 260, 310, 150, 230, 240, 160
     * </pre>
     */
    public static final long RANDOM_SEED_15 = 0xba11bbbbL; // 'ball BBBB'

    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' int's which has no duplicates
     * in the first 12, and always starts with:
     * <pre>
     * 320, 280, 360, 330, 210, 130, 120, 200, 140, 160
     * </pre>
     */
    public static final long RANDOM_SEED_12 = 0x5afec0deL; // 'safe code'

    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' int's which has no duplicates
     * in the first 10, and always starts with:
     * <pre>
     * 140, 330, 270, 120, 290, 230, 350, 360, 130, 240
     * </pre>
     */
    public static final long RANDOM_SEED_10 = 0xca11ca11L; // 'call call'

    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' int's which has no duplicates
     * in the first 5, and always starts with:
     * <pre>
     * 230, 140, 170, 290, 130, 230, 290, 230, 290, 320
     * </pre>
     */
    public static final long RANDOM_SEED_5  = 0xf00d5e1fL; // 'food self'

    private static final int[] INT_ZERO_LEN_ARRAY = new int[0];

    private int currOffset;
    private Random nda_random; // lazy init
    private Long nda_randomSeed; // lazy init unless specified at construction

    public TestIntGenerator() {
        reset();
    }

    public TestIntGenerator(long randomSeed) {
        nda_randomSeed = randomSeed;
        reset();
    }

    public Object getLockObject() {
        return this;
    }

    public synchronized void reset() {
        currOffset = 0;
        clearRandom();
    }

    /**
     * Returns the <em>next</em> count int's in increasing order.
     * If count is greater than 26, then duplicates will be returned.
     * @param count any int value is permitted (less than 1 returns a zero-len array)
     */
    public synchronized int[] next(int count) {
        if ( count < 1 ) {
            return INT_ZERO_LEN_ARRAY;
        }

        int[] result = new int[count];
        int resultPtr = 0;
        do {
            int copyCount =
                Math.min(result.length - resultPtr, NUMBERS.length - currOffset);
            System.arraycopy(NUMBERS, currOffset, result, resultPtr, copyCount);
            resultPtr += copyCount;
            currOffset = (currOffset + copyCount) % NUMBERS.length;
        } while ( resultPtr < result.length );

        return result;
    }

    private synchronized void clearRandom() {
        nda_random = null;
    }

    private synchronized Random checkOrCreateRandom() {
        if ( nda_random == null ) {
            if ( nda_randomSeed == null ) {
                // not specified at construction, generate a random seed
                nda_randomSeed = new Random().nextLong();
            }
            nda_random = new Random(nda_randomSeed);
        }
        return nda_random;
    }

    /**
     * Returns count int's randomly chosen from the available 26 int's
     * and the returned result may include duplicates.
     * If {@link #reset()} is called, then the same sequence of 'random'
     * int's is returned.
     * @param count any int value is permitted (less than 1 returns a zero-len array)
     */
    public synchronized int[] nextRandom(int count) {
        if ( count < 1 ) {
            return INT_ZERO_LEN_ARRAY;
        }

        Random random = checkOrCreateRandom();

        int[] result = new int[count];
        for ( int i = 0; i < result.length; i++ ) {
            result[i] = NUMBERS[random.nextInt(NUMBERS.length)];
        }
        return result;
    }


    /**
     * Returns the number of int's requested in sequence.
     * There are 26 int's, so they wrap around and repeat as needed.
     * @param count any int value is permitted (less than 1 returns a zero-len array)
     */
    public static int[] getInts(int count) {
        return getInts(0, count);
    }

    /**
     * Returns the number of int's requested in sequence starting at the specified offset.
     * There are 26 int's, so they wrap around and repeat as needed.
     * @param offset any int value is permitted (less than zero is silently increased to zero)
     * @param count any int value is permitted (less than 1 returns a zero-len array)
     */
    public static int[] getInts(int offset, int count) {
        if ( count < 1 ) {
            return INT_ZERO_LEN_ARRAY;
        }

        int[] result = new int[count];
        int resultPtr = 0;
        int currOffset = offset % NUMBERS.length;
        do {
            int copyCount =
                Math.min(result.length - resultPtr, NUMBERS.length - currOffset);
            System.arraycopy(NUMBERS, currOffset, result, resultPtr, copyCount);
            resultPtr += copyCount;
            currOffset = (currOffset + copyCount) % NUMBERS.length;
        } while ( resultPtr < result.length );

        return result;
    }

    private static void randomDemo() {
        System.out.println("---------");
        long[] seeds = new long[] {
            RANDOM_SEED_20,
            RANDOM_SEED_15,
            RANDOM_SEED_12,
            RANDOM_SEED_10,
            RANDOM_SEED_5,
        };

        for ( long seed : seeds ) {
            TestIntGenerator fg = new TestIntGenerator(seed);
            int[] values = fg.nextRandom(26);
            List<Integer> valuesList = new ArrayList<>();
            for ( Integer value : values ) {
                valuesList.add(value);
            }

            Set<Integer> set = new LinkedHashSet<>();
            int uniqueDepth = 0;
            boolean foundFirstDup = false;
            for ( int value : values ) {
                if ( foundFirstDup ) {
                    set.add(value);
                } else {
                    if ( set.add(value) ) {
                        uniqueDepth++;
                    } else {
                        foundFirstDup = true;
                    }
                }
            }

            set.addAll(valuesList);

            System.out.printf(
                "seed=%08x, %2d uniqueDepth, %2d unique, %s%n",
                seed,
                uniqueDepth,
                set.size(),
                valuesList);
            System.out.println("==================");
        }
    }

    public static void main(String[] args) {
        getInts(0, 5);
        getInts(10, 7);
        getInts(36, 7);
        getInts(24, 2);
        getInts(25, 1);
        getInts(24, 5);
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(getInts(15, -3)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(getInts(5, 100)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(getInts(0, 52)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(getInts(103, 10)));

        System.out.println("fruit generator -----------------------------");
        TestIntGenerator fruitGenerator = new TestIntGenerator();
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));

        System.out.println("fruit generator -- random without seed -----------------------------");
        fruitGenerator = new TestIntGenerator();
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(15)));
        fruitGenerator.reset();
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(3)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(4)));

        System.out.println("fruit generator -- random WITH seed -----------------------------");
        fruitGenerator = new TestIntGenerator(0xba5eba11);
        //fruitGenerator = new TestFruitGenerator(0x33333333);
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(10)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(15)));
        fruitGenerator.reset();
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(3)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(4)));

//        randomFruits();
//        randomFruitsCombo();
        randomDemo();

    }

    private static final int[] NUMBERS = new int[] {
        110, 120, 130, 140, 150, 160, 170, 180, 190, 200,
        210, 220, 230, 240, 250, 260, 270, 280, 290, 300,
        310, 320, 330, 340, 350, 360
    };
}
