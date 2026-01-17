package com.temporaldb.core.delete;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles soft and hard delete operations.
 */
public class DeleteHandler {
    private static final Logger logger = LoggerFactory.getLogger(DeleteHandler.class);

    private final Map<String, DeleteRecord> deleteRecords = new ConcurrentHashMap<>();

    /**
     * Soft delete a row.
     * @param tableName Table name
     * @param rowId Row ID
     * @param userId User performing delete
     * @param reason Reason for deletion
     */
    public void softDelete(String tableName, long rowId, String userId, String reason) {
        String key = tableName + ":" + rowId;
        DeleteRecord record = new DeleteRecord(tableName, rowId, userId, reason, false);
        deleteRecords.put(key, record);
        logger.info("Soft-deleted {}:{} by {}", tableName, rowId, userId);
    }

    /**
     * Hard delete a row.
     * @param tableName Table name
     * @param rowId Row ID
     * @param userId User performing delete
     */
    public void hardDelete(String tableName, long rowId, String userId) {
        String key = tableName + ":" + rowId;
        DeleteRecord record = new DeleteRecord(tableName, rowId, userId, "Hard delete", true);
        deleteRecords.put(key, record);
        logger.info("Hard-deleted {}:{} by {}", tableName, rowId, userId);
    }

    /**
     * Restore a soft-deleted row.
     * @param tableName Table name
     * @param rowId Row ID
     * @param userId User performing restore
     */
    public void restore(String tableName, long rowId, String userId) {
        String key = tableName + ":" + rowId;
        deleteRecords.remove(key);
        logger.info("Restored {}:{} by {}", tableName, rowId, userId);
    }

    /**
     * Check if row is deleted.
     * @param tableName Table name
     * @param rowId Row ID
     * @return true if deleted
     */
    public boolean isDeleted(String tableName, long rowId) {
        String key = tableName + ":" + rowId;
        DeleteRecord record = deleteRecords.get(key);
        return record != null && !record.isHardDeleted();
    }

    /**
     * Get delete record.
     * @param tableName Table name
     * @param rowId Row ID
     * @return DeleteRecord or null
     */
    public DeleteRecord getDeleteRecord(String tableName, long rowId) {
        String key = tableName + ":" + rowId;
        return deleteRecords.get(key);
    }

    /**
     * Delete record information.
     */
    public static class DeleteRecord {
        private final String tableName;
        private final long rowId;
        private final String userId;
        private final String reason;
        private final boolean hardDeleted;
        private final long timestamp;

        public DeleteRecord(String tableName, long rowId, String userId,
                            String reason, boolean hardDeleted) {
            this.tableName = tableName;
            this.rowId = rowId;
            this.userId = userId;
            this.reason = reason;
            this.hardDeleted = hardDeleted;
            this.timestamp = System.currentTimeMillis();
        }

        public String getTableName() { return tableName; }
        public long getRowId() { return rowId; }
        public String getUserId() { return userId; }
        public String getReason() { return reason; }
        public boolean isHardDeleted() { return hardDeleted; }
        public long getTimestamp() { return timestamp; }
    }
}