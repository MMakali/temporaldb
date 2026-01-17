package com.temporaldb.core.monitoring;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Aggregated performance metrics.
 */
public class PerformanceMetrics {
    private final AtomicLong totalQueries = new AtomicLong(0);
    private final AtomicLong successfulQueries = new AtomicLong(0);
    private final AtomicLong failedQueries = new AtomicLong(0);
    private final AtomicLong totalQueryTime = new AtomicLong(0);
    private final AtomicLong totalTransactions = new AtomicLong(0);
    private final AtomicLong totalTransactionTime = new AtomicLong(0);
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private final Map<String, Long> queryTypeCounts = new ConcurrentHashMap<>();

    public void recordQuery(String queryType, long durationMs, boolean success) {
        totalQueries.incrementAndGet();
        totalQueryTime.addAndGet(durationMs);

        if (success) {
            successfulQueries.incrementAndGet();
        } else {
            failedQueries.incrementAndGet();
        }

        queryTypeCounts.merge(queryType, 1L, Long::sum);
    }

    public void recordTransaction(String type, long durationMs) {
        totalTransactions.incrementAndGet();
        totalTransactionTime.addAndGet(durationMs);
    }

    public void recordCacheOperation(String type, boolean hit) {
        if (hit) {
            cacheHits.incrementAndGet();
        } else {
            cacheMisses.incrementAndGet();
        }
    }

    public double getAverageQueryTime() {
        long total = totalQueries.get();
        return total > 0 ? (double) totalQueryTime.get() / total : 0;
    }

    public double getSuccessRate() {
        long total = totalQueries.get();
        return total > 0 ? (double) successfulQueries.get() / total * 100 : 0;
    }

    public double getCacheHitRatio() {
        long total = cacheHits.get() + cacheMisses.get();
        return total > 0 ? (double) cacheHits.get() / total : 0;
    }

    @Override
    public String toString() {
        return String.format(
                "PerformanceMetrics{queries=%d, avgTime=%.2fms, successRate=%.2f%%, cacheHitRatio=%.2f%%}",
                totalQueries.get(), getAverageQueryTime(), getSuccessRate(), getCacheHitRatio() * 100
        );
    }
}
