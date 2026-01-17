package com.temporaldb.core.replication;

/**
 * Represents a recovery event.
 */
public class RecoveryEvent {
    private final String backupId;
    private final long timestamp;
    private String status;
    private String error;

    public RecoveryEvent(String backupId, long timestamp, String status) {
        this.backupId = backupId;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getBackupId() {
        return backupId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
