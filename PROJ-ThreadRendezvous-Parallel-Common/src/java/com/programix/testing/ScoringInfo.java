package com.programix.testing;

public interface ScoringInfo {
    public static final ScoringInfo ZERO_POINT_INSTANCE = new ScoringInfo() {
        @Override
        public int getFullPointValue() {
            return 0;
        }

        @Override
        public int getExpectedPassCount() {
            return Integer.MAX_VALUE;
        }
    };

    int getFullPointValue();
    int getExpectedPassCount();
}
