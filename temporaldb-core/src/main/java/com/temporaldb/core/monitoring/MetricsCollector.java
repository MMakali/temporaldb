package com.temporaldb.core.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collects performance metrics across the system.
 */
public class MetricsCollector {
    private static final Logger logger = LoggerFactory.getLogger(MetricsCollector.class);

    private final PerformanceMetrics metrics = new PerformanceMetrics();

    /**
     * Record query execution.
     */
    public void recordQueryExecution(String queryType, long durationMs, boolean success) {
        metrics.recordQuery(queryType, durationMs, success);
    }

    /**
     * Record transaction.
     */
    public void recordTransaction(String type, long durationMs) {
        metrics.recordTransaction(type, durationMs);
    }

    /**
     * Record cache operation.
     */
    public void recordCacheOperation(String type, boolean hit) {
        metrics.recordCacheOperation(type, hit);
    }

    /**
     * Get current metrics.
     */
    public PerformanceMetrics getMetrics() {
        return metrics;
    }
}

