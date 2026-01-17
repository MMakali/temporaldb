package com.temporaldb.core.monitoring;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Manages database-wide statistics.
 */
public class DatabaseStatistics {
    private final AtomicLong totalRows = new AtomicLong(0);
    private final AtomicLong totalQueries = new AtomicLong(0);
    private final AtomicLong totalTransactions = new AtomicLong(0);
    private final Map<String, TableStatistics> tableStats = new ConcurrentHashMap<>();
    private final long createdAt = System.currentTimeMillis();

    /**
     * Record row insertion.
     */
    public void recordInsert(String tableName) {
        totalRows.incrementAndGet();
        tableStats.computeIfAbsent(tableName, k -> new TableStatistics(tableName))
                .incrementRowCount();
    }

    /**
     * Record row deletion.
     */
    public void recordDelete(String tableName) {
        totalRows.decrementAndGet();
        tableStats.computeIfAbsent(tableName, k -> new TableStatistics(tableName))
                .decrementRowCount();
    }

    /**
     * Record query execution.
     */
    public void recordQuery() {
        totalQueries.incrementAndGet();
    }

    /**
     * Record transaction completion.
     */
    public void recordTransaction() {
        totalTransactions.incrementAndGet();
    }

    /**
     * Get table statistics.
     */
    public TableStatistics getTableStatistics(String tableName) {
        return tableStats.get(tableName);
    }

    public long getTotalRows() {
        return totalRows.get();
    }

    public long getTotalQueries() {
        return totalQueries.get();
    }

    public long getTotalTransactions() {
        return totalTransactions.get();
    }

    /**
     * Table-specific statistics.
     */
    public static class TableStatistics {
        private final String tableName;
        private final AtomicLong rowCount = new AtomicLong(0);
        private final AtomicLong insertCount = new AtomicLong(0);
        private final AtomicLong updateCount = new AtomicLong(0);
        private final AtomicLong deleteCount = new AtomicLong(0);

        public TableStatistics(String tableName) {
            this.tableName = tableName;
        }

        public void incrementRowCount() {
            rowCount.incrementAndGet();
            insertCount.incrementAndGet();
        }

        public void decrementRowCount() {
            rowCount.decrementAndGet();
            deleteCount.incrementAndGet();
        }

        public long getRowCount() {
            return rowCount.get();
        }

        public long getInsertCount() {
            return insertCount.get();
        }

        public long getDeleteCount() {
            return deleteCount.get();
        }
    }
}
