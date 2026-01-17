package com.temporaldb.core.temporal;

/**
 * Represents a value with temporal metadata.
 */
public class VersionedValue<T> {
    private final T value;
    private final long versionId;
    private final long transactionTime;
    private final long validTimeStart;
    private volatile long validTimeEnd;

    public VersionedValue(T value, long versionId, long transactionTime, long validTimeStart) {
        this.value = value;
        this.versionId = versionId;
        this.transactionTime = transactionTime;
        this.validTimeStart = validTimeStart;
        this.validTimeEnd = Long.MAX_VALUE;
    }

    public T getValue() {
        return value;
    }

    public long getVersionId() {
        return versionId;
    }

    public long getTransactionTime() {
        return transactionTime;
    }

    public long getValidTimeStart() {
        return validTimeStart;
    }

    public long getValidTimeEnd() {
        return validTimeEnd;
    }

    public void setValidTimeEnd(long validTimeEnd) {
        this.validTimeEnd = validTimeEnd;
    }

    public boolean isValidAt(long timestamp) {
        return timestamp >= validTimeStart && timestamp <= validTimeEnd;
    }
}
