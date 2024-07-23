package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;

public interface DSLinkedListFactory {
    <T> DSLinkedList<T> create(Class<T> itemType);
}
