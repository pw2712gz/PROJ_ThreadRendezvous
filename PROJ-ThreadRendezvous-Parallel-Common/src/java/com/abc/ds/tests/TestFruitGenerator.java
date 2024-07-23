package com.abc.ds.tests;

import java.util.*;

import com.programix.thread.*;
import com.programix.util.*;

public class TestFruitGenerator {
    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' fruits which has no duplicates
     * in the first 20, and always starts with:
     * <pre>
     * banana, zucchini, apple, uglifruit, youngberry,
     * vanilla, jujube, strawberry, lemon, honeydew, ...
     * </pre>
     */
    public static final long RANDOM_SEED_20 = 0xfeed0cabL; // feed-O-cab

    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' fruits which has no duplicates
     * in the first 15, and always starts with:
     * <pre>
     * lemon, tangerine, watermelon, honeydew, pear,
     * uglifruit, elderberry, mango, nectarine, fig, ...
     * </pre>
     */
    public static final long RANDOM_SEED_15 = 0xba11bbbbL; // 'ball BBBB'

    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' fruits which has no duplicates
     * in the first 12, and always starts with:
     * <pre>
     * vanilla, raspberry, zucchini, watermelon, kiwi,
     * cherry, banana, jujube, date, fig, ...
     * </pre>
     */
    public static final long RANDOM_SEED_12 = 0x5afec0deL; // 'safe code'

    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' fruits which has no duplicates
     * in the first 10, and always starts with:
     * <pre>
     * date, watermelon, quince, banana, strawberry,
     * mango, youngberry, zucchini, cherry, nectarine, ...
     * </pre>
     */
    public static final long RANDOM_SEED_10 = 0xca11ca11L; // 'call call'

    /**
     * Used as seed for {@link Random} during construction to create a
     * reproducible sequence of 'random' fruits which has no duplicates
     * in the first 5, and always starts with:
     * <pre>
     * mango, date, grape, strawberry, cherry,
     * mango, strawberry, mango, strawberry, vanilla, ...
     * </pre>
     */
    public static final long RANDOM_SEED_5  = 0xf00d5e1fL; // 'food self'


    private int currOffset;
    private Random nda_random; // lazy init
    private Long nda_randomSeed; // lazy init unless specified at construction

    public TestFruitGenerator() {
        reset();
    }

    public TestFruitGenerator(long randomSeed) {
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
     * Returns the <em>next</em> count fruits in alphabetical order.
     * If count is greater than 26, then duplicates will be returned.
     * @param count any int value is permitted (less than 1 returns a zero-len array)
     */
    public synchronized String[] next(int count) {
        if ( count < 1 ) {
            return StringTools.ZERO_LEN_ARRAY;
        }

        String[] result = new String[count];
        int resultPtr = 0;
        do {
            int copyCount =
                Math.min(result.length - resultPtr, FRUITS.length - currOffset);
            System.arraycopy(FRUITS, currOffset, result, resultPtr, copyCount);
            resultPtr += copyCount;
            currOffset = (currOffset + copyCount) % FRUITS.length;
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
     * Returns count String's randomly chosen from the available fruit
     * and the returned result may include duplicates.
     * If {@link #reset()} is called, then the same sequence of 'random'
     * fruits is returned.
     * @param count any int value is permitted (less than 1 returns a zero-len array)
     */
    public synchronized String[] nextRandom(int count) {
        if ( count < 1 ) {
            return StringTools.ZERO_LEN_ARRAY;
        }

        Random random = checkOrCreateRandom();

        String[] result = new String[count];
        for ( int i = 0; i < result.length; i++ ) {
            result[i] = FRUITS[random.nextInt(FRUITS.length)];
        }
        return result;
    }


    /**
     * Returns the number of fruits requested in sequence.
     * There are 26 fruits, so they wrap around and repeat as needed.
     * @param count any int value is permitted (less than 1 returns a zero-len array)
     */
    public static String[] getFruits(int count) {
        return getFruits(0, count);
    }

    /**
     * Returns the number of fruits requested in sequence starting at the specified offset.
     * There are 26 fruits, so they wrap around and repeat as needed.
     * @param offset any int value is permitted (less than zero is silently increased to zero)
     * @param count any int value is permitted (less than 1 returns a zero-len array)
     */
    public static String[] getFruits(int offset, int count) {
        if ( count < 1 ) {
            return StringTools.ZERO_LEN_ARRAY;
        }

        String[] result = new String[count];
        int resultPtr = 0;
        int currOffset = offset % FRUITS.length;
        do {
            int copyCount =
                Math.min(result.length - resultPtr, FRUITS.length - currOffset);
            System.arraycopy(FRUITS, currOffset, result, resultPtr, copyCount);
            resultPtr += copyCount;
            currOffset = (currOffset + copyCount) % FRUITS.length;
        } while ( resultPtr < result.length );

        return result;
    }

    @SuppressWarnings("unused")
    private static void randomFruits() {
        System.out.println("seed searching -----------------------------");
        Set<String> stringMatchSet = new HashSet<>(10000);
        boolean checkStringMatchSet = true;
        System.out.println("*** checkStringMatchSet=" + checkStringMatchSet);
        stringMatchSet.add("feed");
        stringMatchSet.add("f00d");
        stringMatchSet.add("face");
        stringMatchSet.add("ba11");
        stringMatchSet.add("ba5e");
        stringMatchSet.add("c001");
        stringMatchSet.add("babe");
        stringMatchSet.add("beef");
        stringMatchSet.add("ca11");
        stringMatchSet.add("aced");
        stringMatchSet.add("bead");
        stringMatchSet.add("cafe");
        stringMatchSet.add("dada");
        stringMatchSet.add("dead");
        stringMatchSet.add("deaf");
        stringMatchSet.add("deed");
        stringMatchSet.add("fade");
        stringMatchSet.add("b0z0");
        stringMatchSet.add("ab1e");
        stringMatchSet.add("5e1f");
        stringMatchSet.add("5e11");
        stringMatchSet.add("5a1e");
        stringMatchSet.add("c0de");
        stringMatchSet.add("c0da");
        stringMatchSet.add("ce11");
        stringMatchSet.add("c001");
        stringMatchSet.add("deed");
        stringMatchSet.add("dea1");
        stringMatchSet.add("d011");
        stringMatchSet.add("ea5e");
        stringMatchSet.add("1eaf");
        stringMatchSet.add("1055");
        stringMatchSet.add("5afe");
        stringMatchSet.add("5eed");
        stringMatchSet.add("50da");
        stringMatchSet.add("50fa");
        stringMatchSet.add("501d");

        Random seedRandom = new Random(0x11111111);
        Set<String> set = new HashSet<>(10000);

        NanoTimer timer = NanoTimer.createStarted();
        while ( true ) {
            //long seed = seedRandom.nextLong();

            long seed = (seedRandom.nextInt() & 0x00000000ffffffffL);
            if ( checkStringMatchSet ) {
                String seedStr = String.format("%08x", seed);
                if ( !(
                        stringMatchSet.contains(seedStr.substring(0, 4)) &&
                        stringMatchSet.contains(seedStr.substring(4, 8))

                      )) {
                    continue;
                }
            }

            TestFruitGenerator fg = new TestFruitGenerator(seed);
            int minUnique = 10;
            String[] fruits = fg.nextRandom(26);
            set.clear();
            int uniqueDepth = 0;
            boolean foundFirstDup = false;
            for ( String fruit : fruits ) {
                if ( foundFirstDup ) {
                    set.add(fruit);
                } else {
                    if ( set.add(fruit) ) {
                        uniqueDepth++;
                    } else {
                        foundFirstDup = true;
                    }
                }
            }

            set.addAll(Arrays.asList(fruits));
            if ( uniqueDepth >= minUnique ) {
                timer.stop();

                List<String> fruitList = new ArrayList<>(Arrays.asList(fruits));
                int idxIlama = fruitList.indexOf("ilama");
                int idxXigua = fruitList.indexOf("xigua");

                System.out.printf(
                    "%7.3f sec, seed=%08x, %2d unique, %2d idxIlama, %2d idxXigua, %s%n",
                    timer.getElapsedSeconds(),
                    seed,
                    uniqueDepth,
                    idxIlama,
                    idxXigua,
                    fruitList);
                System.out.println("==================");
                ThreadTools.nap(1000);
                timer.resetAndStart();
            }
        }
    }

    @SuppressWarnings("unused")
    private static void randomFruitsCombo() {
            System.out.println("seed searching -----------------------------");
            Set<String> stringMatchSet = new LinkedHashSet<>(10000);
            boolean checkStringMatchSet = true;
            System.out.println("*** checkStringMatchSet=" + checkStringMatchSet);
            stringMatchSet.add("feed");
            stringMatchSet.add("f00d");
            stringMatchSet.add("face");
            stringMatchSet.add("ba11");
            stringMatchSet.add("ba5e");
            stringMatchSet.add("c001");
            stringMatchSet.add("babe");
            stringMatchSet.add("beef");
            stringMatchSet.add("ca11");
            stringMatchSet.add("aced");
            stringMatchSet.add("bead");
            stringMatchSet.add("cafe");
            stringMatchSet.add("dada");
            stringMatchSet.add("dead");
            stringMatchSet.add("deaf");
            stringMatchSet.add("deed");
            stringMatchSet.add("fade");
            stringMatchSet.add("b020");
            stringMatchSet.add("ab1e");
            stringMatchSet.add("5e1f");
            stringMatchSet.add("5e11");
            stringMatchSet.add("5a1e");
            stringMatchSet.add("c0de");
            stringMatchSet.add("c0da");
            stringMatchSet.add("ce11");
            stringMatchSet.add("c001");
            stringMatchSet.add("deed");
            stringMatchSet.add("dea1");
            stringMatchSet.add("d011");
            stringMatchSet.add("ea5e");
            stringMatchSet.add("1eaf");
            stringMatchSet.add("1055");
            stringMatchSet.add("5afe");
            stringMatchSet.add("5eed");
            stringMatchSet.add("50da");
            stringMatchSet.add("50fa");
            stringMatchSet.add("501d");
    //        stringMatchSet.add("1234");
    //        stringMatchSet.add("5678");
    //        stringMatchSet.add("abcd");
    //        stringMatchSet.add("0000");
    //        stringMatchSet.add("1111");
    //        stringMatchSet.add("2222");
    //        stringMatchSet.add("3333");
    //        stringMatchSet.add("4444");
    //        stringMatchSet.add("5555");
    //        stringMatchSet.add("6666");
    //        stringMatchSet.add("7777");
    //        stringMatchSet.add("8888");
    //        stringMatchSet.add("9999");
    //        stringMatchSet.add("aaaa");
    //        stringMatchSet.add("bbbb");
    //        stringMatchSet.add("cccc");
    //        stringMatchSet.add("dddd");
    //        stringMatchSet.add("eeee");
    //        stringMatchSet.add("ffff");
    //        stringMatchSet.add("1122");
    //        stringMatchSet.add("2233");
    //        stringMatchSet.add("3344");
    //        stringMatchSet.add("4455");
    //        stringMatchSet.add("5566");
    //        stringMatchSet.add("6677");
    //        stringMatchSet.add("7788");
    //        stringMatchSet.add("8899");
    //        stringMatchSet.add("99aa");
    //        stringMatchSet.add("aabb");
    //        stringMatchSet.add("bbcc");
    //        stringMatchSet.add("ccdd");
    //        stringMatchSet.add("ddee");
    //        stringMatchSet.add("eeff");
    //        stringMatchSet.add("ff00");
    //        stringMatchSet.add("4321");
    //        stringMatchSet.add("3210");
    //        stringMatchSet.add("9876");
    //        stringMatchSet.add("5432");
    //        stringMatchSet.add("dcba");

            String[] words = StringTools.toArray(stringMatchSet);
            System.out.println("words.length=" + words.length);

            String[] secondWords = new String[10000];
            for ( int i = 0; i < secondWords.length; i++ ) {
                secondWords[i] = String.format("%04x", i);
            }

            System.out.println("checking words for parsability...");
            for ( String word : words ) {
                Integer.parseInt(word, 16);
            }
            System.out.println("... all good");

            System.out.println("----");
            Set<String> set = new HashSet<>(10000);
            NanoTimer timer = NanoTimer.createStarted();
            for ( String firstWord : words ) {
                //for ( String secondWord : secondWords ) {
                for ( String secondWord : words ) {
                    String comboStr = firstWord + secondWord;
                    int nextInt = (int) Long.parseLong(comboStr.toUpperCase(), 16);
                    long seed = (nextInt & 0x00000000ffffffffL);
    //                if ( checkStringMatchSet ) {
    //                    String seedStr = String.format("%08x", seed);
    //                    if ( !(
    //                            stringMatchSet.contains(seedStr.substring(0, 4)) &&
    //                            stringMatchSet.contains(seedStr.substring(4, 8))
    //
    //                          )) {
    //                        continue;
    //                    }
    //                }

                    TestFruitGenerator fg = new TestFruitGenerator(seed);
                    int minUnique = 5;
                    String[] fruits = fg.nextRandom(26);
                    set.clear();
                    int uniqueDepth = 0;
                    boolean foundFirstDup = false;
                    for ( String fruit : fruits ) {
                        if ( foundFirstDup ) {
                            set.add(fruit);
                        } else {
                            if ( set.add(fruit) ) {
                                uniqueDepth++;
                            } else {
                                foundFirstDup = true;
                            }
                        }
                    }

                    set.addAll(Arrays.asList(fruits));
                    if ( uniqueDepth >= minUnique ) {

                        List<String> fruitList = new ArrayList<>(Arrays.asList(fruits));
                        int idxIlama = fruitList.indexOf("ilama");
                        int idxXigua = fruitList.indexOf("xigua");
                        if ( (0 <= idxIlama && idxIlama <= 30) ||
                             (0 <= idxXigua && idxXigua <= 30) ) {
                            continue;
                        }

                        timer.stop();
                        System.out.printf(
                            "%7.3f sec, seed=%08x, %2d uniqueDepth, %2d unique, %2d idxIlama, %2d idxXigua, %s%n",
                            timer.getElapsedSeconds(),
                            seed,
                            uniqueDepth,
                            set.size(),
                            idxIlama,
                            idxXigua,
                            fruitList);
                        System.out.println("==================");
                        //ThreadTools.nap(1000);
                        timer.resetAndStart();
                    }
                }
            }
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
            TestFruitGenerator fg = new TestFruitGenerator(seed);
            String[] fruits = fg.nextRandom(26);

            Set<String> set = new LinkedHashSet<>();
            int uniqueDepth = 0;
            boolean foundFirstDup = false;
            for ( String fruit : fruits ) {
                if ( foundFirstDup ) {
                    set.add(fruit);
                } else {
                    if ( set.add(fruit) ) {
                        uniqueDepth++;
                    } else {
                        foundFirstDup = true;
                    }
                }
            }

            set.addAll(Arrays.asList(fruits));
            List<String> fruitList = new ArrayList<>(Arrays.asList(fruits));
            int idxIlama = fruitList.indexOf("ilama");
            int idxXigua = fruitList.indexOf("xigua");

            System.out.printf(
                "seed=%08x, %2d uniqueDepth, %2d unique, %2d idxIlama, %2d idxXigua, %s%n",
                seed,
                uniqueDepth,
                set.size(),
                idxIlama,
                idxXigua,
                fruitList);
            System.out.println("==================");
        }
    }

    public static void main(String[] args) {
        getFruits(0, 5);
        getFruits(10, 7);
        getFruits(36, 7);
        getFruits(24, 2);
        getFruits(25, 1);
        getFruits(24, 5);
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(getFruits(15, -3)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(getFruits(5, 100)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(getFruits(0, 52)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(getFruits(103, 10)));

        System.out.println("fruit generator -----------------------------");
        TestFruitGenerator fruitGenerator = new TestFruitGenerator();
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.next(5)));

        System.out.println("fruit generator -- random without seed -----------------------------");
        fruitGenerator = new TestFruitGenerator();
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(5)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(15)));
        fruitGenerator.reset();
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(3)));
        System.out.println("Arrays.asList(fruits)=" + Arrays.asList(fruitGenerator.nextRandom(4)));

        System.out.println("fruit generator -- random WITH seed -----------------------------");
        fruitGenerator = new TestFruitGenerator(0xba5eba11);
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

    private static final String[] FRUITS = new String[] {
        "apple",
        "banana",
        "cherry",
        "date",
        "elderberry",
        "fig",
        "grape",
        "honeydew",
        "ilama",
        "jujube",
        "kiwi",
        "lemon",
        "mango",
        "nectarine",
        "orange",
        "pear",
        "quince",
        "raspberry",
        "strawberry",
        "tangerine",
        "uglifruit",
        "vanilla",
        "watermelon",
        "xigua",
        "youngberry",
        "zucchini"
    };
}
