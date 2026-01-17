package com.temporaldb.core.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Optimizes queries using indexes.
 */
public class IndexQueryOptimizer {
    private static final Logger logger = LoggerFactory.getLogger(IndexQueryOptimizer.class);

    private final IndexManager indexManager;

    public IndexQueryOptimizer(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    /**
     * Find best index for query column.
     */
    public DatabaseIndex selectIndexForColumn(String tableName, String columnName) {
        DatabaseIndex index = indexManager.findBestIndex(tableName, columnName);
        if (index != null) {
            logger.debug("Selected index {} for query on {}.{}",
                    index.getName(), tableName, columnName);
            indexManager.recordIndexUsage(index.getName());
        }
        return index;
    }

    /**
     * Get all available indexes for table.
     */
    public List<DatabaseIndex> getAvailableIndexes(String tableName) {
        // This would need access to all indexes - simplified here
        return new ArrayList<>();
    }
}
