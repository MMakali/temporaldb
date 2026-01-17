package com.temporaldb.core.replication;

/**
 * Represents a backup point.
 */
public class BackupPoint {
    private final String id;
    private final String name;
    private final String description;
    private final long timestamp;
    private volatile boolean verified = false;

    public BackupPoint(String id, String name, String description, long timestamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
