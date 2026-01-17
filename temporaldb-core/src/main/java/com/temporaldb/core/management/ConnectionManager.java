package com.temporaldb.core.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Connection pool for managing database connections.
 */
public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private final BlockingQueue<DatabaseConnection> availableConnections;
    private final Set<DatabaseConnection> allConnections = ConcurrentHashMap.newKeySet();
    private final int maxConnections;
    private final AtomicLong createdConnections = new AtomicLong(0);

    public ConnectionManager(int maxConnections) {
        this.maxConnections = maxConnections;
        this.availableConnections = new LinkedBlockingQueue<>(maxConnections);

        // Initialize connections
        for (int i = 0; i < maxConnections; i++) {
            DatabaseConnection conn = new DatabaseConnection(this);
            allConnections.add(conn);
            availableConnections.offer(conn);
            createdConnections.incrementAndGet();
        }

        logger.info("Connection pool initialized with {} connections", maxConnections);
    }

    /**
     * Get a connection from the pool.
     */
    public DatabaseConnection getConnection() throws InterruptedException {
        DatabaseConnection conn = availableConnections.take();
        conn.markActive();
        logger.debug("Connection acquired (available: {})", availableConnections.size());
        return conn;
    }

    /**
     * Return connection to pool.
     */
    public void returnConnection(DatabaseConnection conn) {
        conn.markInactive();
        availableConnections.offer(conn);
        logger.debug("Connection returned (available: {})", availableConnections.size());
    }

    /**
     * Get pool statistics.
     */
    public PoolStatistics getStatistics() {
        return new PoolStatistics(
                maxConnections,
                availableConnections.size(),
                allConnections.size() - availableConnections.size()
        );
    }

    public void close() {
        availableConnections.clear();
        allConnections.clear();
        logger.info("Connection pool closed");
    }
}
