package it.polimi.deib.deepse.a3e.middleware.core;

public enum LatencyRequirement  {
    ANY(0), LOW(1), VERY_LOW(2);

    public final int value;

    private LatencyRequirement(int value) {
        this.value = value;
    }
}
