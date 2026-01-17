package com.temporaldb.core.audit;

import lombok.Data;

/**
 * Represents an audit log entry.
 */
@Data
public class AuditLog {
    private final long id;
    private final String operation;          // INSERT, UPDATE, DELETE, HARD_DELETE
    private final String username;
    private final String tableName;
    private final long rowId;
    private final String beforeState;        // JSON
    private final String afterState;         // JSON
    private final long timestamp;
    private final boolean success;
    private final String errorMessage;

    public AuditLog(long id, String operation, String username, String tableName,
                    long rowId, String beforeState, String afterState,
                    boolean success, String errorMessage) {
        this.id = id;
        this.operation = operation;
        this.username = username;
        this.tableName = tableName;
        this.rowId = rowId;
        this.beforeState = beforeState;
        this.afterState = afterState;
        this.timestamp = System.currentTimeMillis();
        this.success = success;
        this.errorMessage = errorMessage;
    }
}