package com.temporaldb.core.transaction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages concurrent transactions with MVCC
 */
public class TransactionManager {
    private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);

    private final AtomicLong versionCounter = new AtomicLong(0);
    private final Map<Long, Transaction> activeTransactions = new ConcurrentHashMap<>();

    /**
     * Start a new transaction.
     * @param userId User starting the transaction
     * @return New transaction
     */
    public Transaction begin(String userId) {
        long snapshotVersion = versionCounter.get();
        Transaction txn = new Transaction(snapshotVersion, userId);
        activeTransactions.put(txn.getTxnId(), txn);
        logger.info("Transaction {} started for user {}", txn.getTxnId(), userId);
        return txn;
    }

    /**
     * Commit a transaction.
     * @param txn Transaction to commit
     */
    public void commit(Transaction txn) {
        txn.commit();
        activeTransactions.remove(txn.getTxnId());
        versionCounter.incrementAndGet();
        logger.info("Transaction {} committed", txn.getTxnId());
    }

    /**
     * Rollback a transaction.
     * @param txn Transaction to rollback
     */
    public void rollback(Transaction txn) {
        txn.rollback();
        activeTransactions.remove(txn.getTxnId());
        logger.info("Transaction {} rolled back", txn.getTxnId());
    }

    /**
     * Get current snapshot version (for MVCC).
     * @return Current version
     */
    public long getCurrentVersion() {
        return versionCounter.get();
    }

    /**
     * Get all active transactions.
     * @return Map of transactions
     */
    public Map<Long, Transaction> getActiveTransactions() {
        return new HashMap<>(activeTransactions);
    }
}