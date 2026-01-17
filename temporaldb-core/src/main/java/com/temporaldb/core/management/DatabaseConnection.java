package com.temporaldb.core.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a single database connection.
 */
public class DatabaseConnection {
    private final String connectionId = UUID.randomUUID().toString();
    private final ConnectionManager manager;
    private volatile boolean active = false;
    private volatile long createdAt = System.currentTimeMillis();
    private volatile long lastUsedAt = createdAt;
    private volatile boolean closed = false;

    public DatabaseConnection(ConnectionManager manager) {
        this.manager = manager;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public boolean isActive() {
        return active;
    }

    void markActive() {
        this.active = true;
    }

    void markInactive() {
        this.active = false;
        lastUsedAt = System.currentTimeMillis();
    }

    public boolean isClosed() {
        return closed;
    }

    /**
     * Execute query.
     */
    public List<Map<String, Object>> executeQuery(String sql) {
        if (closed) {
            throw new IllegalStateException("Connection is closed");
        }

        // Query execution logic would go here
        return new ArrayList<>();
    }

    /**
     * Close connection.
     */
    public void close() {
        if (!closed) {
            closed = true;
            manager.returnConnection(this);
        }
    }
}
