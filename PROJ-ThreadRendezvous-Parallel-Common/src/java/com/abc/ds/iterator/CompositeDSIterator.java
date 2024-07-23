package com.abc.ds.iterator;

import java.util.*;

import com.abc.ds.*;

/**
 * Create a composite iterator for all of the specified iterators.
 * Each of the iterators is used in turn and hasNext() only returns
 * false when the last iterator returns false.
 * You can use {@link Builder} to dynamically create.
 */
public class CompositeDSIterator<T> implements DSIterator<T> {
    private final DSIterator<T>[] underlyingIterators;
    private int pointer;
    private boolean nextAvailable;

    @SuppressWarnings("unchecked")
    public CompositeDSIterator(DSIterator<T>[] pUnderlyingIterators) {
        if ( pUnderlyingIterators == null ) {
            pUnderlyingIterators =
                DSTools.createArrayFromType(DSIterator.class, 0);
        }

        this.underlyingIterators = pUnderlyingIterators;
        pointer = 0;
        advanceNextAvailable();
    }

    private void advanceNextAvailable() {
        nextAvailable = false;
        for ( ; pointer < underlyingIterators.length; pointer++ ) {
            if ( underlyingIterators[pointer].hasNext() ) {
                nextAvailable = true;
                return;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return nextAvailable;
    }

    @Override
    public T next() throws NoSuchElementException {
        if ( !hasNext() ) {
            throw new NoSuchElementException();
        }
        T item = underlyingIterators[pointer].next();
        advanceNextAvailable();
        return item;
    }

    public static class Builder<T> {
        private DSIterator<T>[] iterators;
        private int count;

        public Builder() {
            iterators = createIteratorArray(10);
            count = 0;
        }

        @SuppressWarnings("unchecked")
        private DSIterator<T>[] createIteratorArray(int length) {
            return DSTools.createArrayFromType(DSIterator.class, length);
        }

        public CompositeDSIterator<T> create() {
            if ( count == 0 ) {
                return new CompositeDSIterator<>(null);
            }

            DSIterator<T>[] exact = createIteratorArray(count);
            System.arraycopy(iterators, 0, exact, 0, count);
            return new CompositeDSIterator<>(exact);
        }

        public void append(DSIterator<T> iterator) {
            if ( iterator == null ) {
                return; // ignore
            }

            if ( count == iterators.length ) {
                DSIterator<T>[] tmp = createIteratorArray(iterators.length * 2);
                System.arraycopy(iterators, 0, tmp, 0, count);
                iterators = tmp;
            }
            iterators[count] = iterator;
            count++;
        }
    } // type Builder
}
