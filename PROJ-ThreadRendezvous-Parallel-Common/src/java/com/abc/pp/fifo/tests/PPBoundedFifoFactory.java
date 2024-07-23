package com.abc.pp.fifo.tests;

import com.abc.pp.fifo.*;

public interface PPBoundedFifoFactory {
    <T> PPBoundedFifo<T> create(Class<T> itemType,
                                  int capacity);
}
