package com.programix.testing;

import java.util.*;

/** deliberate package scope */
class BasicTestThreadFactory implements TestThreadFactory {
    private static final WorkWrapper[] WORK_WRAPPER_ZERO_LEN_ARRAY = new WorkWrapper[0];

    private WorkWrapperStore workWrapperStore;
    private Map<String, Integer> nameCountMap;

    public BasicTestThreadFactory() {
        workWrapperStore = new WorkWrapperStore();
        nameCountMap = new HashMap<>();
    }

    @Override
    public String createThreadFor(String suggestedThreadName, Runnable work) {
        WorkWrapper ww = new WorkWrapper(work, suggestedThreadName);
        return ww.getActualThreadName();
    }

    @Override
    public String createThreadFor(Runnable work, String suggestedThreadName) {
        return createThreadFor(suggestedThreadName, work);
    }

    @Override
    public void interruptAllLiveThreads() {
        for (WorkWrapper ww : workWrapperStore.getAllWork()) {
            ww.interrupt();
        }
    }

    @Override
    public void forciblyStopAllLiveThreads() {
        for (WorkWrapper ww : workWrapperStore.getAllWork()) {
            ww.forceStop();
        }
    }

    private synchronized String generateUniqueThreadName(String suggestedThreadName) {
        Integer oldCount = nameCountMap.get(suggestedThreadName);
        int count;
        if (oldCount == null) {
            count = 1;
        } else {
            count = oldCount + 1;
        }
        nameCountMap.put(suggestedThreadName, count);
        return String.format("%s-%02d", suggestedThreadName, count);
    }

    private class WorkWrapper {
        private final Runnable rawWork;
        private final Thread internalThread;
        private volatile boolean interruptRequested;
        private volatile boolean forceStopRequested;

        public WorkWrapper(Runnable rawWork, String suggestedThreadName) {
            this.rawWork = rawWork;
            interruptRequested = false;
            forceStopRequested = false;

            workWrapperStore.addWork(this);
            internalThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        runWork();
                    }
                },
                generateUniqueThreadName(suggestedThreadName));
            internalThread.start();
        }

        private void runWork() {
            try {
                rawWork.run();
            } finally {
                workWrapperStore.removeWork(this);
            }
        }

        public String getActualThreadName() {
            return internalThread.getName();
        }

        public void interrupt() {
            interruptRequested = true;
            internalThread.interrupt();
        }

        @SuppressWarnings("deprecation")
        public void forceStop() {
            forceStopRequested = true;
            internalThread.stop();
        }
    } // type WorkWrapper

    private static class WorkWrapperStore {
        private Set<WorkWrapper> liveWorkSet;

        public WorkWrapperStore() {
            liveWorkSet = new HashSet<>();
        }

        public synchronized void addWork(WorkWrapper ww) {
            liveWorkSet.add(ww);
        }

        public synchronized boolean removeWork(WorkWrapper ww) {
            return liveWorkSet.remove(ww);
        }

        public synchronized WorkWrapper[] getAllWork() {
            return liveWorkSet.toArray(WORK_WRAPPER_ZERO_LEN_ARRAY);
        }
    } // type WorkWrapperStore
}
