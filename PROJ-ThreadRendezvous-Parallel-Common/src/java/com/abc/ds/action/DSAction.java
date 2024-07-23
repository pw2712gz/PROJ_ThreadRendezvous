package com.abc.ds.action;

/**
 * Used to define an action to perform on items of type T.
 */
public interface DSAction<T> {
    void perform(T item);
}
