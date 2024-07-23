package com.programix.testing;

public class BasicScoringInfo implements ScoringInfo {
    private final int fullPointValue;
    private final int expectedPassCount;

    public BasicScoringInfo(int fullPointValue, int expectedPassCount) {
        this.fullPointValue = fullPointValue;
        this.expectedPassCount = expectedPassCount;
    }

    @Override
    public int getFullPointValue() {
        return fullPointValue;
    }

    @Override
    public int getExpectedPassCount() {
        return expectedPassCount;
    }
}
