package com.temporaldb.core.index;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages all database indexes.
 */
public class IndexManager {
    private static final Logger logger = LoggerFactory.getLogger(IndexManager.class);

    private final Map<String, DatabaseIndex> indexes = new ConcurrentHashMap<>();
    private final Map<String, IndexStatistics> statistics = new ConcurrentHashMap<>();

    /**
     * Create a B-Tree index.
     */
    public DatabaseIndex createBTreeIndex(String name, String tableName, String columnName) {
        BTreeIndex index = new BTreeIndex(name, tableName, columnName);
        indexes.put(name, index);
        statistics.put(name, new IndexStatistics(name));
        logger.info("Created B-Tree index: {}", name);
        return index;
    }

    /**
     * Create a Hash index.
     */
    public DatabaseIndex createHashIndex(String name, String tableName, String columnName) {
        HashIndex index = new HashIndex(name, tableName, columnName);
        indexes.put(name, index);
        statistics.put(name, new IndexStatistics(name));
        logger.info("Created Hash index: {}", name);
        return index;
    }

    /**
     * Get index by name.
     */
    public DatabaseIndex getIndex(String name) {
        return indexes.get(name);
    }

    /**
     * Find best index for column.
     */
    public DatabaseIndex findBestIndex(String tableName, String columnName) {
        return indexes.values().stream()
                .filter(idx -> idx.getTableName().equals(tableName) && idx.getColumnName().equals(columnName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Update index statistics.
     */
    public void recordIndexUsage(String indexName) {
        IndexStatistics stats = statistics.get(indexName);
        if (stats != null) {
            stats.incrementAccessCount();
        }
    }

    /**
     * Get index statistics.
     */
    public IndexStatistics getStatistics(String indexName) {
        return statistics.get(indexName);
    }
}

