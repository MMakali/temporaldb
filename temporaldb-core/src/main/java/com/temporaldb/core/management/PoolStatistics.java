package com.temporaldb.core.management;

/**
 * Connection pool statistics.
 */
public class PoolStatistics {
    private final int maxConnections;
    private final int availableConnections;
    private final int activeConnections;

    public PoolStatistics(int max, int available, int active) {
        this.maxConnections = max;
        this.availableConnections = available;
        this.activeConnections = active;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public int getAvailableConnections() {
        return availableConnections;
    }

    public int getActiveConnections() {
        return activeConnections;
    }

    public double getUtilization() {
        return (double) activeConnections / maxConnections * 100;
    }

    @Override
    public String toString() {
        return String.format(
                "PoolStatistics{max=%d, available=%d, active=%d, utilization=%.2f%%}",
                maxConnections, availableConnections, activeConnections, getUtilization()
        );
    }
}
