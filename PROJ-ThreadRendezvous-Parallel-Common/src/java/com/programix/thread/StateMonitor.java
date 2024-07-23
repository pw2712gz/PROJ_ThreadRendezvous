package com.programix.thread;

import com.programix.util.*;

public class StateMonitor<T extends StateMonitor.Monitorable<T>>
        implements ThreadSafe {

    private T state;
    private final ListenerManager<Listener<T>> listenerManager;

    private final Object lockObject;
    private final Waiter waiter;

    // PRIVATE - should only be called from one of the other constructors
    @SuppressWarnings("unchecked")
    private StateMonitor(T initialState,
                         Waiter proposedWaiter,
                         Object proposedLockObject) {

        ObjectTools.paramNullCheck(initialState, "initialState");
        if ( proposedWaiter != null ) {
            waiter = proposedWaiter;
            lockObject = proposedWaiter.getLockObject();
        } else {
            lockObject =
                (proposedLockObject == null) ? this : proposedLockObject;
            waiter = new Waiter(lockObject);
        }

        state = initialState;

        /*
        Listener<T> sampleListener = new Listener<T>() {
            @Override
            public void stateChanged(T oldState, T newState) {
            }
        };

        Class<Listener<T>> actualType = (Class<Listener<T>>) sampleListener.getClass();
        Class<? extends Listener> sampleListenerClass = sampleListener.getClass();
        formatType(sampleListenerClass, "", "sampleListener");

        Type[] genericInterfaces = sampleListenerClass.getGenericInterfaces();
        formatTypes(genericInterfaces, "", "genericInterfaces");

        formatType(sampleListenerClass.getGenericSuperclass(), "", "genericSuperclass");
         */

        /*
        for ( Type genericInterfaceType : genericInterfaces ) {
            if ( genericInterfaceType instanceof ParameterizedType ) {
                ParameterizedType pt = (ParameterizedType) genericInterfaceType;
                for ( Type actualTypeArg : pt.getActualTypeArguments() ) {
                    System.out.println("actualTypeArg=" + actualTypeArg);
                    System.out.println("actualTypeArg.getClass()=" + actualTypeArg.getClass());
                    if ( actualTypeArg instanceof TypeVariable ) {
                        TypeVariable tv = (TypeVariable) actualTypeArg;
                        System.out.println("tv=" + tv);
                        System.out.println("tv.getClass()=" + tv.getClass());
                        for ( Type bound : tv.getBounds() ) {
                            System.out.println("bound=" + bound);
                            System.out.println("bound.getClass()=" + bound.getClass());
                        }
                    }

                    if ( actualTypeArg instanceof Class<?> ) {
                        actualType = (Class<Listener<T>>) actualTypeArg;
                    }
                }
            }
        }
         */

//        listenerManager = new ListenerManager<Listener<T>>(actualType);
        //listenerManager = new ListenerManager<Listener<T>>((Listener<T>[]) Array.newInstance(actualType, 0));
//        listenerManager = new ListenerManager<Listener<?>>((Class<StateMonitor.Listener<?>>)StateMonitor.Listener.class);
//listenerManager = (ListenerManager<StateMonitor.Listener<T>>)
//        new ListenerManager<StateMonitor.Listener<T>>((Class<StateMonitor.Listener<T>>) StateMonitor.Listener.class);

        Class<?> lType = StateMonitor.Listener.class;
        listenerManager = (ListenerManager<Listener<T>>) new ListenerManager<>(lType);
    }

    /**
     * Creates an instance that is initially set to the state of
     * <tt>initialState</tt> and synchronizes on
     * the lock used by the specified <tt>waiter</tt>.
     */
    public StateMonitor(T initialState, Waiter waiter) {
        this(initialState, waiter, null);
    }

    /**
     * Creates an instance that is initially set to the state of
     * <tt>initialState</tt> and synchronizes on
     * the specified <tt>lockObject</tt>.
     */
    public StateMonitor(T initialState, Object lockObject) {
        this(initialState, null, lockObject);
    }

    /**
     * Creates an instance that is initially set to the state of
     * <tt>initialState</tt> and synchronizes on itself.
     */
    public StateMonitor(T initialState) {
        this(initialState, null, null);
    }

    /*
     *
    private static void formatType(Type type, String indent, String message) {
        if ( indent.length() > 10 ) {
            System.out.println("=== cutting off recursion ==");
            return;
        }
        if ( type instanceof Class ) {
            Class<?> cls = (Class<?>) type;
            System.out.println(indent + message + "|type IS-A Class, getName()=" + cls.getName());
        } else if ( type instanceof ParameterizedType ) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type[] actualTypeArguments = paramType.getActualTypeArguments();
            System.out.println(indent + message + "|type IS-A ParameterizedType with " + actualTypeArguments.length + " actualTypeArguments");
            formatTypes(actualTypeArguments, indent + "  ", "actualTypeArguments");
        } else if ( type instanceof TypeVariable<?> ) {
            TypeVariable<? extends GenericDeclaration> typeVar = (TypeVariable<? extends GenericDeclaration>) type;
            String name = typeVar.getName();
            Type[] bounds = typeVar.getBounds();
            GenericDeclaration genericDeclaration = typeVar.getGenericDeclaration();
            TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();

            System.out.println(indent + message +
                "|type IS-A TypeVariable with name=" + name +
                ", bounds.length=" + bounds.length +
                ", GenericDeclaration.typeParameters.length=" + typeParameters.length);
            formatTypes(bounds, indent + "  ", "bounds");
            formatTypes(typeParameters, indent + "  ", "typeParameters");
        } else {
            System.out.println(indent + message + "|unsupported Type: " + type);
        }
    }

    private static void formatTypes(Type[] types, String indent, String message) {
        System.out.println(indent + message + "|array of " + types.length + " Type's");
        for ( int i = 0; i < types.length; i++ ) {
            formatType(types[i], indent + "  ", "types[" + i + "]");
        }
    }
     */


    /** Returns the current state, never null */
    public T getState() {
        synchronized ( lockObject ) {
            return state;
        }
    }

    /**
     * Returns true if this state matches one or more of the specified
     * targetStates.
     */
    @SuppressWarnings("unchecked")
    public boolean matchesAny(T... targetStates) {
        synchronized ( lockObject ) {
            for ( T targetState : targetStates ) {
                if ( state == targetState ) {
                    return true;
                }
            }
            return false;
        }
    }

    public void transitionTo(T proposedNewState) throws IllegalStateException {
        synchronized ( lockObject ) {
            boolean success = transitionToIfPermitted(proposedNewState);
            if ( !success ) {
                throw new IllegalStateException("cannot transition from " +
                    state + " to " + proposedNewState);
            }
        }
    }

    public boolean transitionToIfPermitted(T proposedNewState) {
        synchronized ( lockObject ) {
            if ( !state.canTransitionTo(proposedNewState) ) {
                return false;
            }
            if ( state != proposedNewState ) {
                T oldState = state;
                state = proposedNewState;
                T newState = state;
                waiter.signalChange();
                notifyListeners(oldState, newState);
            }
            return true;
        }
    }

    private boolean waitUntilStateIs(T targetState,
                                     long msTimeout,
                                     boolean useTimedOutException)
            throws TimedOutException, InterruptException, ShutdownException {

        return waitOnStateIs(targetState, msTimeout, true, useTimedOutException);
    }

    private boolean waitWhileStateIs(T targetState,
                                     long msTimeout,
                                     boolean useTimedOutException)
             throws TimedOutException, InterruptException, ShutdownException {

        return waitOnStateIs(targetState, msTimeout, false, useTimedOutException);
    }

    private boolean waitOnStateIs(final T targetState,
                                  long msTimeout,
                                  boolean waitUntil,
                                  boolean useTimedOutException)
            throws TimedOutException, InterruptException, ShutdownException {

        synchronized ( lockObject ) {
            Waiter.Condition inTargetStateCondition =
                waiter.createCondition(new Waiter.Expression() {

                    @Override
                    public boolean isTrue() {
                        return state == targetState;
                    }
                });

            if ( useTimedOutException ) {
                if ( waitUntil ) {
                    inTargetStateCondition
                        .waitUntilTrueWithTimedOutException(msTimeout);
                    return true;
                } else {
                    inTargetStateCondition
                        .waitWhileTrueWithTimedOutException(msTimeout);
                    return true;
                }
            } else {
                if ( waitUntil ) {
                    return inTargetStateCondition.waitUntilTrue(msTimeout);
                } else {
                    return inTargetStateCondition.waitWhileTrue(msTimeout);
                }
            }
        }
    }

    public void waitUntilStateIsWithTimedOutException(T targetState,
                                                      long msTimeout)
            throws TimedOutException, InterruptException, ShutdownException {

        waitUntilStateIs(targetState, msTimeout, true);
    }

    /**
     * Returns {@link ThreadTools#TIMED_OUT} if a timeout occurs.
     */
    public boolean waitUntilStateIs(final T targetState, long msTimeout)
            throws InterruptException, ShutdownException {

        return waitUntilStateIs(targetState, msTimeout, false);
    }

    public void waitUntilStateIs(T targetState)
            throws InterruptException, ShutdownException {

        waitUntilStateIs(targetState, ThreadTools.NO_TIMEOUT, false);
    }

    public void waitWhileStateIsWithTimedOutException(T targetState,
                                                      long msTimeout)
            throws TimedOutException, InterruptException, ShutdownException {

        waitWhileStateIs(targetState, msTimeout, true);
    }

    /**
     * Returns {@link ThreadTools#TIMED_OUT} if a timeout occurs.
     */
    public boolean waitWhileStateIs(T targetState, long msTimeout)
            throws InterruptException, ShutdownException {

        return waitWhileStateIs(targetState, msTimeout, false);
    }

    public void waitWhileStateIs(T targetState)
            throws InterruptException, ShutdownException {

        waitWhileStateIs(targetState, ThreadTools.NO_TIMEOUT, false);
    }

    public Object getLockObject() {
        return lockObject;
    }

    public boolean addListener(Listener<T> listener) {
        return listenerManager.addListener(listener);
    }

    public boolean removeListener(Listener<T> listener) {
        return listenerManager.removeListener(listener);
    }

    private void notifyListeners(final T oldState, final T newState) {
        listenerManager.notifyListeners(
            new ListenerManager.NotifyAction<Listener<T>>() {

            @Override
            public void performAction(Listener<T> listener) {
                listener.stateChanged(oldState, newState);
            }
        });
    }

    /**
     * Implemented my states which support being monitored.
     */
    public static interface Monitorable<T> {
        boolean canTransitionTo(T potentialNewState);
    } // type Monitorable

    /**
     * Implemented by code which wants to know when the state actually changes.
     */
    public static interface Listener<T> {
        void stateChanged(T oldState, T newState);
    } // type Listener
}