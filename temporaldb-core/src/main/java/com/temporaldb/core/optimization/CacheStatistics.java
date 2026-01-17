package com.temporaldb.core.optimization;

/**
 * Tracks query cache statistics.
 */
public class CacheStatistics {
    private final QueryCache cache;
    private long peakSize = 0;
    private long totalEvictions = 0;

    public CacheStatistics(QueryCache cache) {
        this.cache = cache;
    }

    public void recordPeakSize(int size) {
        peakSize = Math.max(peakSize, size);
    }

    public void recordEviction() {
        totalEvictions++;
    }

    public double getHitRatio() {
        return cache.getHitRatio();
    }

    public long getPeakSize() {
        return peakSize;
    }

    public long getTotalEvictions() {
        return totalEvictions;
    }

    @Override
    public String toString() {
        return String.format(
                "CacheStatistics{size=%d, hits=%d, misses=%d, hitRatio=%.2f%%, peak=%d, evictions=%d}",
                cache.getSize(), cache.getHitCount(), cache.getMissCount(),
                getHitRatio() * 100, peakSize, totalEvictions
        );
    }
}
