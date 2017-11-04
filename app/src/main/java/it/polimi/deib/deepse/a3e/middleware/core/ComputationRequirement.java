package it.polimi.deib.deepse.a3e.middleware.core;

/**
 * Created by giovanniquattrocchi on 04/11/17.
 */

public enum ComputationRequirement {
    ANY(0), FAST(1), VERY_FAST(2);

    public final int value;

    private ComputationRequirement(int value) {
        this.value = value;
    }
}
