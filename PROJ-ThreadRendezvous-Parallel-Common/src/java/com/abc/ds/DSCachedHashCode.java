package com.abc.ds;

/**
 * Marker interface to indicate that the {@link Object#hashCode()} method
 * has been overridden and the hash code is cached for super-efficient
 * subsequent calls to {@link #hashCode()}. Some internals check to see if
 * this interface has been implemented and if so, hashCode will be called
 * first before checking for equality (since the hash code is declared to
 * be quick and cheap to call after the first call).
 */
public interface DSCachedHashCode {
    @Override
    int hashCode();
}
