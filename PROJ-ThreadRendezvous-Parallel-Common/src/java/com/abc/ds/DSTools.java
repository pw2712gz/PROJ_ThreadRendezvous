package com.abc.ds;

import java.lang.reflect.*;
import java.util.*;

import com.abc.ds.iterator.*;
import com.abc.ds.sack.*;
import com.programix.util.*;

public class DSTools {
    // no instances
    private DSTools() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] createArrayFromType(Class<T> type, int count) {
        ObjectTools.paramNullCheck(type, "type");
        return (T[]) Array.newInstance(type, count);
    }

    /**
     * Used to coerce types around some of the limits of Generics in Java.
     * USE WITH CAUTION.
     * <p>Example:
     * <br/>Since you can't do:
     * <pre>
     * Class&lt;DSKeyValuePair&lt;Integer, String&gt;&gt; type =
     *     DSKeyValuePair&lt;Integer, String&gt;.class;
     * </pre>
     * you can instead do the "ugly but it works":
     * <pre>
     * Class&lt;DSKeyValuePair&lt;Integer, String&gt;&gt; type =
     *     (Class&lt;DSKeyValuePair&lt;Integer, String&gt;&gt;)
     *         DSTools.coerceClassType(DSKeyValuePair.class);
     * </pre>
     * @param type
     */
    public static <T> Class<?> coerceClassType(Class<T> type) {
        T[] array = createArrayFromType(type, 0);
        Class<?> componentType = array.getClass().getComponentType();
        return componentType;
    }

    /**
     * A wrapping adapter to make a {@link DSIterable} accessible as
     * a {@link Iterable}. This allows code such as:
     * <pre>
     * DSSack&lt;String&gt; sack = //...
     * for ( String s : DSTools.asJavaIterable(sack) ) {
     *     System.out.println("s=" + s);
     * }
     * </pre>
     */
    public static <T> Iterable<T> asJavaIterable(DSIterable<T> dsIterable) {
        ObjectTools.paramNullCheck(dsIterable, "dsIterable");
        return new JavaIterable<>(dsIterable);
    }

    /** Returns true if the reference is null or points to an empty sack. */
    public static boolean isEmpty(DSSack<?> sack) {
        return sack == null || sack.isEmpty();
    }

    /**
     * Uses the iterator to collect all the items and returns them in an
     * array, never null.
     */
    public static <T> T[] iteratorToArray(Class<T> itemType,
                                          DSIterator<T> iterator) {

        List<T> list = new ArrayList<>();
        while ( iterator.hasNext() ) {
            list.add(iterator.next());
        }
        T[] results = createArrayFromType(itemType, list.size());
        list.toArray(results);
        return results;
    }

    private static class JavaIterable<T> implements Iterable<T> {
        private final DSIterable<T> dsIterable;

        public JavaIterable(DSIterable<T> dsIterable) {
            this.dsIterable = dsIterable;
        }

        @Override
        public Iterator<T> iterator() {
            return new JavaIterator();
        }

        private class JavaIterator implements Iterator<T> {
            private final DSIterator<T> dsIterator;

            public JavaIterator() {
                dsIterator = dsIterable.createIterator();
            }

            @Override
            public boolean hasNext() {
                return dsIterator.hasNext();
            }

            @Override
            public T next() {
                return dsIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        } // type JavaIterator
    } // type JavaIterable
}
