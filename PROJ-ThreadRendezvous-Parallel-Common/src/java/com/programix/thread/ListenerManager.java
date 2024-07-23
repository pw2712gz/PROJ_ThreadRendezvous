package com.programix.thread;

import java.lang.reflect.*;
import java.util.*;

import com.programix.util.*;

public class ListenerManager<L> {
    private L[] listeners;
    private final L[] listenerZeroLenArray;
    private final Lock addRemoveLock;
    private final Lock notificationLock;

    public ListenerManager(L[] pListenerZeroLenArray) {
        ObjectTools.paramNullCheck(pListenerZeroLenArray, "pListenerZeroLenArray");
        if ( pListenerZeroLenArray.length != 0 ) {
            throw new IllegalArgumentException(
                "pListenerZeroLenArray must have a length of 0, not " +
                pListenerZeroLenArray.length);
        }
        listenerZeroLenArray = pListenerZeroLenArray;
        listeners = listenerZeroLenArray;
        addRemoveLock = new Lock(new Object());
        notificationLock = new Lock(new Object());
    }

    public ListenerManager(Class<L> listenerType) {
        this(createArrayFromType(listenerType, 0));
    }

    /*
    public ListenerManager() {
        Class<L> listenerType = calcListenerType();
        listenerZeroLenArray = createArrayFromType(listenerType, 0);
        listeners = listenerZeroLenArray;
        addRemoveLock = new Lock(new Object());
        notificationLock = new Lock(new Object());
    }
     */

    @SuppressWarnings("unchecked")
    public Class<L> calcListenerType() {
        ParameterizedType parameterizedType =
            (ParameterizedType) (getClass().getGenericSuperclass());
        return (Class<L>) parameterizedType.getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] createArrayFromType(Class<T> type, int count) {
        ObjectTools.paramNullCheck(type, "type");
        return (T[]) Array.newInstance(type, count);
    }

    public int getListenerCount() {
        return getAllListeners().length;
    }

    public boolean addListener(L listener) {
        if ( listener == null ) return false;

        addRemoveLock.acquireLockForThreadIfNeeded();
        try {
            Set<L> set = new LinkedHashSet<>(Arrays.asList(listeners));
            boolean addedIt = set.add(listener);
            listeners = set.toArray(listenerZeroLenArray);
            return addedIt;
        } finally {
            addRemoveLock.releaseLockForThreadIfDone();
        }
    }

    public boolean removeListener(L listener) {
        if ( listener == null ) return false;

        addRemoveLock.acquireLockForThreadIfNeeded();
        try {
            Set<L> set = new LinkedHashSet<>(Arrays.asList(listeners));
            boolean removedIt = set.remove(listener);
            listeners = set.toArray(listenerZeroLenArray);
            return removedIt;
        } finally {
            addRemoveLock.releaseLockForThreadIfDone();
        }
    }

    private L[] getAllListeners() {
        addRemoveLock.acquireLockForThreadIfNeeded();
        try {
            return listeners;
        } finally {
            addRemoveLock.releaseLockForThreadIfDone();
        }
    }


    public void notifyListeners(NotifyAction<L> action) {
        ObjectTools.paramNullCheck(action, "action");

        notificationLock.acquireLockForThreadIfNeeded();
        try {
            L[] localListeners = getAllListeners();
            for ( L listener : localListeners ) {
                action.performAction(listener);
            }
        } finally {
            notificationLock.releaseLockForThreadIfDone();
        }
    }

    public static interface NotifyAction<L> {
        void performAction(L listener);
    } // type NotifyAction

    private static class Lock implements ThreadSafe {
        private Thread lockHolder;
        private final Counter depth;
        private final Object syncLockObject;

        public Lock(Object pSyncLockObject) {
            lockHolder = null;

            syncLockObject =
                pSyncLockObject != null ? pSyncLockObject : new Object();
            depth = new Counter(syncLockObject);
        }

        public void acquireLockForThreadIfNeeded(long msTimeout)
                throws InterruptException, TimedOutException, ShutdownException {

            synchronized ( syncLockObject ) {
                Thread callingThread = Thread.currentThread();

                if ( depth.isNotZero() && lockHolder != callingThread ) {
                    depth.waitUntilZero(msTimeout);
                }

                lockHolder = callingThread;
                depth.increment();
            }
        }

        public void acquireLockForThreadIfNeeded()
            throws InterruptException, ShutdownException {
            acquireLockForThreadIfNeeded(ThreadTools.NO_TIMEOUT);
        }

        public void releaseLockForThreadIfDone() throws IllegalStateException {
            synchronized ( syncLockObject ) {
                if ( depth.isShutdown() ) return; // quietly return

                Thread callingThread = Thread.currentThread();

                if ( lockHolder != callingThread ) {
                    throw new IllegalStateException(
                        "lock cannot be released as the calling thread " +
                        StringTools.quoteWrap(callingThread.getName()) +
                        " doesn't currently hold the lock");
                }

                depth.decrement();
                if ( depth.isZero() ) {
                    lockHolder = null;
                }
            }
        }
    } // type Lock
}
