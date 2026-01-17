package com.temporaldb.core.replication;

import java.util.HashMap;
import java.util.Map;

/**
 * Event representing a replication operation.
 */
public class ReplicationEvent {
    private final long timestamp;
    private final String operation;
    private final Map<String, Object> data;
    private final String sourceNode;
    private boolean applied = false;

    public ReplicationEvent(long timestamp, String operation, Map<String, Object> data, String sourceNode) {
        this.timestamp = timestamp;
        this.operation = operation;
        this.data = new HashMap<>(data);
        this.sourceNode = sourceNode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getOperation() {
        return operation;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getSourceNode() {
        return sourceNode;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }
}
