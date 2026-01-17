package com.temporaldb.core.index;

/**
 * Tracks index usage statistics.
 */
public class IndexStatistics {
    private final String indexName;
    private long accessCount = 0;
    private long hitCount = 0;
    private long missCount = 0;
    private long insertCount = 0;
    private long deleteCount = 0;
    private long createdAt = System.currentTimeMillis();

    public IndexStatistics(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexName() {
        return indexName;
    }

    public synchronized void incrementAccessCount() {
        accessCount++;
    }

    public synchronized void recordHit() {
        hitCount++;
    }

    public synchronized void recordMiss() {
        missCount++;
    }

    public synchronized void recordInsert() {
        insertCount++;
    }

    public synchronized void recordDelete() {
        deleteCount++;
    }

    public double getHitRatio() {
        long total = hitCount + missCount;
        return total > 0 ? (double) hitCount / total : 0;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public long getHitCount() {
        return hitCount;
    }

    public long getMissCount() {
        return missCount;
    }

    public long getInsertCount() {
        return insertCount;
    }

    public long getDeleteCount() {
        return deleteCount;
    }

    @Override
    public String toString() {
        return String.format(
                "IndexStatistics{name='%s', accesses=%d, hits=%d, misses=%d, hitRatio=%.2f%%}",
                indexName, accessCount, hitCount, missCount, getHitRatio() * 100
        );
    }
}
