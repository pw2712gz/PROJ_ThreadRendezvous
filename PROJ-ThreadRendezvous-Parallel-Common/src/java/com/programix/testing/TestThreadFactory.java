package com.programix.testing;

public interface TestThreadFactory {
    String createThreadFor(String suggestedThreadName, Runnable work);
    String createThreadFor(Runnable work, String suggestedThreadName);
    void interruptAllLiveThreads();
    void forciblyStopAllLiveThreads();
}
