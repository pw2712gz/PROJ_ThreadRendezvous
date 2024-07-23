package com.programix.testing;

import java.awt.*;
import java.util.*;

import com.programix.thread.*;

public enum TestState implements StateMonitor.Monitorable<TestState> {
    NEVER_STARTED("Never Started", false, new Color(204, 204, 204)),
    RUNNING("Running", false, new Color(207, 231, 245)),
    SUCCEEDED("Succeeded", true, new Color(51, 204, 102)),
    FAILED("Failed", true, new Color(255, 66, 14)),
    //FAILED("Failed", true, new Color(255, 192, 192)),
    CANCELLED("Cancelled", true, new Color(255, 255, 0));

    private static final Map<TestState, TestState[]> allowedTransitions;

    static {
        Map<TestState, TestState[]> map = new HashMap<>();
        map.put(NEVER_STARTED, new TestState[] { RUNNING, CANCELLED });
        map.put(RUNNING, new TestState[] { SUCCEEDED, FAILED, CANCELLED});
        map.put(SUCCEEDED, new TestState[] { });
        map.put(FAILED, new TestState[] { });
        map.put(CANCELLED, new TestState[] { });
        allowedTransitions = new EnumMap<>(map);
    }

    private final String displayName;
    private final boolean complete;
    private final Color preferredBackgroundColor;
    private TestState[] allowedNextStates;

    private TestState(String displayName,
                      boolean complete,
                      Color preferredBackgroundColor) {
        this.displayName = displayName;
        this.complete = complete;
        this.preferredBackgroundColor = preferredBackgroundColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isComplete() {
        return complete;
    }

    public Color getPreferredBackgroundColor() {
        return preferredBackgroundColor;
    }

    @Override
    public synchronized boolean canTransitionTo(TestState potentialNewState) {
        if ( allowedNextStates == null ) {
            allowedNextStates = allowedTransitions.get(this);
            if ( allowedNextStates == null ) {
                allowedNextStates = new TestState[0];
            }
        }
        for ( TestState testState : allowedNextStates ) {
            if ( potentialNewState == testState ) {
                return true;
            }
        }
        return false;
    }
}