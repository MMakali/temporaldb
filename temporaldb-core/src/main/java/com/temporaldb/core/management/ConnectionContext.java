package com.temporaldb.core.management;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Connection context for per-connection state.
 */
public class ConnectionContext {
    private final DatabaseConnection connection;
    private final Map<String, Object> parameters = new ConcurrentHashMap<>();
    private volatile long transactionId = -1;
    private volatile long sessionStartTime = System.currentTimeMillis();

    public ConnectionContext(DatabaseConnection connection) {
        this.connection = connection;
    }

    public DatabaseConnection getConnection() {
        return connection;
    }

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public void beginTransaction(long txnId) {
        this.transactionId = txnId;
    }

    public void endTransaction() {
        this.transactionId = -1;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getSessionDuration() {
        return System.currentTimeMillis() - sessionStartTime;
    }
}
