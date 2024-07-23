package com.abc.ds.fifo.tests;

import com.abc.ds.fifo.*;

public interface DSUnboundedFifoFactory {
    <T> DSUnboundedFifo<T> create(Class<T> itemType,
                                  int initialCapacity,
                                  int percentToGrowCapacity);

    <T> DSUnboundedFifo<T> create(Class<T> itemType);
}
