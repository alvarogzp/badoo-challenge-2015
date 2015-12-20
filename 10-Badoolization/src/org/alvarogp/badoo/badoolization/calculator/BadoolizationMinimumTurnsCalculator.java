package org.alvarogp.badoo.badoolization.calculator;

import org.alvarogp.badoo.badoolization.state.BadoolizationCurrentState;
import org.alvarogp.badoo.badoolization.state.BadoolizationState;

public class BadoolizationMinimumTurnsCalculator {
    private final BadoolizationState initialState;
    private final BadoolizationState finalState;
    private final int ralphTurns;
    private BadoolizationCurrentState currentState;
    private int minTurns = Integer.MAX_VALUE;

    public BadoolizationMinimumTurnsCalculator(BadoolizationState initialState, BadoolizationState finalState, int ralphTurns) {
        this.initialState = initialState;
        this.finalState = finalState;
        this.ralphTurns = ralphTurns;
        this.currentState = new BadoolizationCurrentState(initialState, finalState, this);
    }

    public void calculateMinimumTurns() {
        currentState.solve();
    }

    public void reachedTargetInTurn(int turn) {
        if (turn < minTurns) {
            minTurns = turn;
        }
    }

    public boolean shouldContinueToTurn(int turn) {
        return turn <= ralphTurns && turn < minTurns;
    }

    public String getReport() {
        if (ralphTurns < minTurns) {
            return "CHEATER";
        } else {
            return String.valueOf(ralphTurns - minTurns);
        }
    }
}
