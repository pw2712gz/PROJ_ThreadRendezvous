package com.programix.testing;

import java.util.*;

import com.programix.thread.*;

public class TestChunkContainer implements TestChunk {
    private final TestChunk[] childChunks;
    private final String displayName;
    private final ScoringInfo scoringInfo;

    private final StateMonitor<TestState> testStateMonitor;
    private final ListenerManager<TestChunk.Listener> listenerManager;

    private final Set<TestChunk> neverStartedSet;
    private final Set<TestChunk> runningSet;
    private final Set<TestChunk> cancelledSet;
    private final Set<TestChunk> failedSet;
    private final Set<TestChunk> succeededSet;

    private final BooleanState kickedOff;
    private final Object lockObject = new Object();

    public TestChunkContainer(TestChunk[] pChildChunks,
                              String displayName) {

        this.childChunks = pChildChunks.clone();
        this.displayName = displayName;

        kickedOff = new BooleanState(lockObject);

        testStateMonitor =
            new StateMonitor<>(TestState.NEVER_STARTED, lockObject);
        testStateMonitor.addListener(new StateMonitor.Listener<TestState>() {
            @Override
            public void stateChanged(final TestState oldState,
                                     final TestState newState) {
                final ListenerEventMeta lem = new ListenerEventMeta();
                listenerManager.notifyListeners(new ListenerManager.NotifyAction<TestChunk.Listener>() {
                    @Override
                    public void performAction(TestChunk.Listener listener) {
                        listener.testStateChanged(lem, oldState, newState);
                    }
                });
            }
        });

        listenerManager =
            new ListenerManager<>(TestChunk.Listener.class);

        neverStartedSet = new LinkedHashSet<>();
        runningSet = new LinkedHashSet<>();
        cancelledSet = new LinkedHashSet<>();
        failedSet = new LinkedHashSet<>();
        succeededSet = new LinkedHashSet<>();

        int totalFullPointValue = 0;
        int totalExpectedPassCount = 0;

        for ( final TestChunk childChunk : childChunks ) {
            childChunk.addListener(new ChildTestChunkListener(childChunk));
            neverStartedSet.add(childChunk);

            ScoringInfo si = childChunk.getScoringInfo();
            if (si != null) {
                totalFullPointValue += si.getFullPointValue();
                totalExpectedPassCount += si.getExpectedPassCount();
            }
        }
        scoringInfo = new BasicScoringInfo(totalFullPointValue, totalExpectedPassCount);
    }

    @Override
    public ScoringInfo getScoringInfo() {
        return scoringInfo;
    }

    @Override
    public int getActiveThreadCount() {
        int total = 0;
        for (TestChunk testChunk : childChunks) {
            total += testChunk.getActiveThreadCount();
        }
        return total;
    }

    private void childTestStateChanged(TestChunk childTestChunk,
                                       ListenerEventMeta meta,
                                       TestState oldState,
                                       TestState newState) {

        synchronized ( lockObject ) {
            switch ( oldState ) {
                case NEVER_STARTED: neverStartedSet.remove(childTestChunk); break;
                case RUNNING: runningSet.remove(childTestChunk); break;
                default: throw new IllegalStateException(
                    "oldState=" + oldState + " should 'never' happen");
            }

            switch ( newState ) {
                case RUNNING: runningSet.add(childTestChunk); break;
                case SUCCEEDED: succeededSet.add(childTestChunk); break;
                case FAILED: failedSet.add(childTestChunk); break;
                case CANCELLED: cancelledSet.add(childTestChunk); break;
                default: throw new IllegalStateException(
                    "newState=" + newState + " should 'never' happen");
            }

            TestState newOverallState = null;
            if ( neverStartedSet.isEmpty() ) {
                if ( !runningSet.isEmpty() ) {
                    newOverallState = TestState.RUNNING;
                } else if ( succeededSet.size() == childChunks.length ) {
                    newOverallState = TestState.SUCCEEDED;
                } else if ( !cancelledSet.isEmpty() ) {
                    newOverallState = TestState.CANCELLED;
                } else {
                    newOverallState = TestState.FAILED;
                }
            } else {
                // some never started, might be NEVER_STARTED or RUNNING
                newOverallState =
                    neverStartedSet.size() == childChunks.length ?
                        TestState.NEVER_STARTED : TestState.RUNNING;
            }

            testStateMonitor.transitionToIfPermitted(newOverallState);
        }
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public TestState getTestState() {
        return testStateMonitor.getState();
    }

    @Override
    public boolean kickoffTestsAsync() {
        synchronized ( lockObject ) {
            if ( kickedOff.isTrue() ) {
                return false;
            }
            kickedOff.setState(true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for ( TestChunk testChunk : childChunks ) {
                        testChunk.kickoffTestsAsync();
                        ThreadTools.nap(691);
                    }
                }

            }).start();
            return true;
        }
    }

    @Override
    public void cancelAllTests() {
        for ( TestChunk testChunk : childChunks ) {
            testChunk.cancelAllTests();
        }
    }

    @Override
    public void addListener(TestChunk.Listener listener) {
        listenerManager.addListener(listener);
    }

    @Override
    public void removeListener(TestChunk.Listener listener) {
        listenerManager.removeListener(listener);
    }

    private class ChildTestChunkListener implements TestChunk.Listener {
        private final TestChunk childTestChunk;

        public ChildTestChunkListener(TestChunk childTestChunk) {
            this.childTestChunk = childTestChunk;
        }

        @Override
        public void testStateChanged(ListenerEventMeta meta,
                                     TestState oldState,
                                     TestState newState) {

            childTestStateChanged(childTestChunk, meta, oldState, newState);
        }

        @Override
        public void incrementPassedCount(final ListenerEventMeta meta) {
            listenerManager.notifyListeners(
                new ListenerManager.NotifyAction<TestChunk.Listener>() {
                @Override
                public void performAction(TestChunk.Listener listener) {
                    listener.incrementPassedCount(meta);
                }
            });
        }

        @Override
        public void incrementFailedCount(final ListenerEventMeta meta) {
            listenerManager.notifyListeners(
                new ListenerManager.NotifyAction<TestChunk.Listener>() {
                @Override
                public void performAction(TestChunk.Listener listener) {
                    listener.incrementFailedCount(meta);
                }
            });
        }

        @Override
        public void outln(final ListenerEventMeta meta, final String line) {
            listenerManager.notifyListeners(
                new ListenerManager.NotifyAction<TestChunk.Listener>() {
                    @Override
                    public void performAction(TestChunk.Listener listener) {
                        listener.outln(meta, line);
                    }
                });
        }

        @Override
        public void outlnErrorText(final ListenerEventMeta meta,
                                   final String line) {
            listenerManager.notifyListeners(
                new ListenerManager.NotifyAction<TestChunk.Listener>() {
                @Override
                public void performAction(TestChunk.Listener listener) {
                    listener.outlnErrorText(meta, line);
                }
            });
        }

        @Override
        public void outStackTrace(final ListenerEventMeta meta,
                                  final Throwable t) {
            listenerManager.notifyListeners(
                new ListenerManager.NotifyAction<TestChunk.Listener>() {
                    @Override
                    public void performAction(TestChunk.Listener listener) {
                        listener.outStackTrace(meta, t);
                    }
                });
        }

        @Override
        public void outErrorStackTrace(final ListenerEventMeta meta,
                                       final Throwable t) {
            listenerManager.notifyListeners(
                new ListenerManager.NotifyAction<TestChunk.Listener>() {
                    @Override
                    public void performAction(TestChunk.Listener listener) {
                        listener.outErrorStackTrace(meta, t);
                    }
                });
        }
    } // type ChildTestChunkListener
}
