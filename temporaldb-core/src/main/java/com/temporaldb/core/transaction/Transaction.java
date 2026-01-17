package com.temporaldb.core.transaction;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a database transaction with ACID properties.
 */
@Getter
public class Transaction {
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);
    private static final AtomicLong txnIdGenerator = new AtomicLong(0);

    private final long txnId;
    private final long snapshotVersion;
    private final String userId;
    private final long startTime;
    private final List<Operation> operations = new ArrayList<>();
    private volatile TransactionState state = TransactionState.ACTIVE;

    public Transaction(long snapshotVersion, String userId) {
        this.txnId = txnIdGenerator.incrementAndGet();
        this.snapshotVersion = snapshotVersion;
        this.userId = userId;
        this.startTime = System.currentTimeMillis();
    }

    public void addOperation(Operation operation) {
        if (state != TransactionState.ACTIVE) {
            throw new IllegalStateException("Cannot add operation to " + state + " transaction");
        }
        operations.add(operation);
    }

    public void commit() {
        state = TransactionState.COMMITTED;
        logger.info("Transaction {} committed with {} operations", txnId, operations.size());
    }

    public void rollback() {
        state = TransactionState.ROLLED_BACK;
        logger.info("Transaction {} rolled back", txnId);
    }

    public enum TransactionState {
        ACTIVE, COMMITTED, ROLLED_BACK
    }

    /**
     * Represents a single operation within a transaction.
     */
    @Getter
    public static class Operation {
        public enum Type { INSERT, UPDATE, DELETE }

        private final Type type;
        private final String tableName;
        private final long rowId;
        private final Map<String, Object> values;

        public Operation(Type type, String tableName, long rowId, Map<String, Object> values) {
            this.type = type;
            this.tableName = tableName;
            this.rowId = rowId;
            this.values = values;
        }

    }
}