package com.temporaldb.core.temporal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a specific version of the database.
 * Tracks transaction time and valid time for bitemporal support.
 */
public class TemporalVersion {
    private static final Logger logger = LoggerFactory.getLogger(TemporalVersion.class);

    private final long versionId;
    private final long transactionTime;  // When was this version created
    private volatile long validTimeStart;  // When is this version valid from
    private volatile long validTimeEnd;    // When is this version valid to
    private final Map<String, Object> metadata = new ConcurrentHashMap<>();

    public TemporalVersion(long versionId, long transactionTime) {
        this.versionId = versionId;
        this.transactionTime = transactionTime;
        this.validTimeStart = transactionTime;
        this.validTimeEnd = Long.MAX_VALUE;  // Open-ended by default
    }

    public long getVersionId() { return versionId; }
    public long getTransactionTime() { return transactionTime; }
    public long getValidTimeStart() { return validTimeStart; }
    public long getValidTimeEnd() { return validTimeEnd; }

    public void setValidTimeEnd(long validTimeEnd) {
        this.validTimeEnd = validTimeEnd;
    }

    public void putMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    public boolean isValidAt(long timestamp) {
        return timestamp >= validTimeStart && timestamp <= validTimeEnd;
    }

    @Override
    public String toString() {
        return String.format("TemporalVersion{id=%d, txTime=%d, validTime=[%d,%d]}",
                versionId, transactionTime, validTimeStart, validTimeEnd);
    }
}

