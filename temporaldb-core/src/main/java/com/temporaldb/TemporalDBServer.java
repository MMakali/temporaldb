package com.temporaldb;

import com.temporaldb.core.memory.OffHeapMemoryManager;
import com.temporaldb.core.security.AuthenticationManager;
import com.temporaldb.core.security.PrivilegeManager;
import com.temporaldb.core.transaction.TransactionManager;
import com.temporaldb.core.audit.AuditLogger;
import com.temporaldb.core.delete.DeleteHandler;
import com.temporaldb.core.temporal.*;
import com.temporaldb.core.query.*;
import com.temporaldb.core.index.*;
import com.temporaldb.core.optimization.*;
import com.temporaldb.core.replication.*;
import com.temporaldb.core.monitoring.*;
import com.temporaldb.core.management.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TemporalDB Server - Fully Integrated Temporal Database.
 * Includes all components: memory, storage, transactions, temporal versioning,
 * query engine, indexing, optimization, replication, backup/recovery, and monitoring.
 */
public class TemporalDBServer {
    private static final Logger logger = LoggerFactory.getLogger(TemporalDBServer.class);

    // Core Components
    private final OffHeapMemoryManager memoryManager;
    private final AuthenticationManager authManager;
    private final PrivilegeManager privilegeManager;
    private final TransactionManager transactionManager;
    private final AuditLogger auditLogger;
    private final DeleteHandler deleteHandler;

    // Temporal Components
    private final VersionManager versionManager;

    // Query Components
    private final QueryParser queryParser;
    private final QueryOptimizer queryOptimizer;
    private final QueryExecutor queryExecutor;

    // Index Components
    private final IndexManager indexManager;

    // Optimization Components
    private final CompressionManager compressionManager;
    private final QueryCache queryCache;
    private final CacheStatistics cacheStatistics;

    // Replication Components
    private final ReplicationManager replicationManager;
    private final BackupManager backupManager;
    private final RecoveryManager recoveryManager;

    // Monitoring Components
    private final MetricsCollector metricsCollector;
    private final HealthMonitor healthMonitor;
    private final DatabaseStatistics dbStatistics;
    private final AdminAPI adminAPI;

    // Management Components
    private final SchemaManager schemaManager;
    private final DataValidator dataValidator;
    private final ConstraintChecker constraintChecker;
    private final ConnectionManager connectionManager;

    /**
     * -- GETTER --
     *  Check if server is running.
     */
    @Getter
    private volatile boolean running = false;

    /**
     * Create a TemporalDB Server with default configuration.
     */
    public TemporalDBServer() {
        this(10L * 1024 * 1024 * 1024, 10);  // 10GB memory, 10 connections
    }

    /**
     * Create a TemporalDB Server with custom configuration.
     */
    public TemporalDBServer(long maxMemory, int maxConnections) {
        logger.info("Initializing TemporalDB Server v2.0.0");

        // Initialize core components
        this.memoryManager = new OffHeapMemoryManager(maxMemory);
        this.authManager = new AuthenticationManager();
        this.privilegeManager = new PrivilegeManager();
        this.transactionManager = new TransactionManager();
        this.auditLogger = new AuditLogger();
        this.deleteHandler = new DeleteHandler();

        // Initialize temporal components
        this.versionManager = new VersionManager();

        // Initialize query components
        this.queryParser = new QueryParser();
        this.queryOptimizer = new QueryOptimizer();
        this.queryExecutor = new QueryExecutor();

        // Initialize index components
        this.indexManager = new IndexManager();

        // Initialize optimization components
        this.compressionManager = new CompressionManager();
        this.queryCache = new QueryCache(1000);  // 1000 entry cache
        this.cacheStatistics = new CacheStatistics(queryCache);

        // Initialize replication components
        this.replicationManager = new ReplicationManager("primary-node");
        this.backupManager = new BackupManager();
        this.recoveryManager = new RecoveryManager();

        // Initialize monitoring components
        this.metricsCollector = new MetricsCollector();
        this.healthMonitor = new HealthMonitor();
        this.dbStatistics = new DatabaseStatistics();
        this.adminAPI = new AdminAPI();

        // Initialize management components
        this.schemaManager = new SchemaManager();
        this.dataValidator = new DataValidator();
        this.constraintChecker = new ConstraintChecker();
        this.connectionManager = new ConnectionManager(maxConnections);

        logger.info("TemporalDB Server components initialized");
        logger.info("  Memory: {} GB", maxMemory / (1024L * 1024 * 1024));
        logger.info("  Connections: {}", maxConnections);
        logger.info("  Query Cache: 1000 entries");
    }

    /**
     * Start the server.
     */
    public void start() {
        logger.info("Starting TemporalDB Server v2.0.0");

        try {
            // Create default admin user
            authManager.createUser("admin", "admin123", "admin@temporaldb.com");
            privilegeManager.grantRole(authManager.getUser("admin"), "ADMIN");

            running = true;

            // Log startup information
            logger.info("✓ TemporalDB Server started successfully");
            logger.info("✓ Default admin user created (username: admin, password: admin123)");
            logger.info("✓ Temporal version manager initialized");
            logger.info("✓ Query engine initialized");
            logger.info("✓ Index manager initialized");
            logger.info("✓ Query cache initialized (1000 entries)");
            logger.info("✓ Replication manager initialized");
            logger.info("✓ Backup manager initialized");
            logger.info("✓ Monitoring system initialized");
            logger.info("✓ Schema manager initialized");
            logger.info("✓ Connection pool initialized");
            logger.info("✓ Memory available: {} GB",
                    memoryManager.getAvailableMemory() / (1024.0 * 1024.0 * 1024.0));

        } catch (Exception e) {
            logger.error("Failed to start TemporalDB Server", e);
            stop();
            throw new RuntimeException("Server startup failed", e);
        }
    }

    /**
     * Stop the server.
     */
    public void stop() {
        logger.info("Stopping TemporalDB Server");
        running = false;

        try {
            if (connectionManager != null) {
                connectionManager.close();
            }
            if (memoryManager != null) {
                memoryManager.close();
            }
            logger.info("✓ TemporalDB Server stopped");
        } catch (Exception e) {
            logger.error("Error stopping TemporalDB Server", e);
        }
    }

    // ==================== Query Methods ====================

    /**
     * Execute a SELECT query.
     */
    public List<Map<String, Object>> executeSelect(String sql) {
        if (!running) throw new IllegalStateException("Server is not running");

        try {
            SelectQuery query = queryParser.parseSelect(sql);
            QueryPlan plan = new QueryPlan(query);
            QueryPlan optimized = queryOptimizer.optimize(plan);
            return queryExecutor.execute(optimized);
        } catch (Exception e) {
            logger.error("Query execution failed: {}", sql, e);
            auditLogger.log("SELECT", "system", "", 0, null, null, false, e.getMessage());
            throw new RuntimeException("Query execution failed", e);
        }
    }

    /**
     * Execute a temporal (point-in-time) query.
     */
    public List<Map<String, Object>> executeTemporalSelect(String sql, long timestamp) {
        if (!running) throw new IllegalStateException("Server is not running");

        SelectQuery query = queryParser.parseSelect(sql);
        query.setTemporal(true, timestamp);

        // Get version valid at timestamp
        TemporalVersion version = versionManager.getVersionAt(timestamp);
        if (version == null) {
            logger.warn("No version found for timestamp: {}", timestamp);
            return Collections.emptyList();
        }

        QueryPlan plan = new QueryPlan(query);
        QueryPlan optimized = queryOptimizer.optimize(plan);
        return queryExecutor.execute(optimized);
    }

    // ==================== Temporal Methods ====================

    /**
     * Get current temporal version.
     */
    public TemporalVersion getCurrentVersion() {
        return versionManager.getLatestVersion();
    }

    /**
     * Create a backup snapshot.
     */
    public String createTemporalSnapshot(String name) {
        TemporalVersion version = versionManager.createVersion();
        BackupPoint backup = backupManager.createBackup(name, "Temporal snapshot");
        logger.info("Created temporal snapshot: {} (version: {})", name, version.getVersionId());
        return backup.getId();
    }

    // ==================== Index Methods ====================

    /**
     * Create index on column.
     */
    public void createIndex(String indexName, String tableName, String columnName, String type) {
        if ("BTREE".equalsIgnoreCase(type)) {
            indexManager.createBTreeIndex(indexName, tableName, columnName);
        } else if ("HASH".equalsIgnoreCase(type)) {
            indexManager.createHashIndex(indexName, tableName, columnName);
        } else {
            throw new IllegalArgumentException("Unknown index type: " + type);
        }
    }

    // ==================== Replication Methods ====================

    /**
     * Add replica node.
     */
    public void addReplicaNode(String replicaId, String host, int port) {
        replicationManager.addReplica(replicaId, host, port);
    }

    /**
     * Get replication lag.
     */
    public long getReplicationLag(String replicaId) {
        return replicationManager.getReplicationLag(replicaId);
    }

    // ==================== Backup Methods ====================

    /**
     * Create backup.
     */
    public String createBackup(String name) {
        BackupPoint backup = backupManager.createBackup(name, "Manual backup");
        return backup.getId();
    }

    /**
     * Recover from backup.
     */
    public void recoverFromBackup(String backupId) {
        BackupPoint backup = backupManager.getBackup(backupId);
        if (backup != null) {
            recoveryManager.recover(backup);
        }
    }

    // ==================== Monitoring Methods ====================

    /**
     * Get system health.
     */
    public SystemHealth getSystemHealth() {
        return healthMonitor.performHealthCheck();
    }

    /**
     * Get performance metrics.
     */
    public PerformanceMetrics getPerformanceMetrics() {
        return metricsCollector.getMetrics();
    }

    /**
     * Get database statistics.
     */
    public DatabaseStatistics getDatabaseStatistics() {
        return dbStatistics;
    }

    // ==================== Schema Methods ====================

    /**
     * Create table.
     */
    public void createTable(String tableName, List<ColumnDefinition> columns) {
        schemaManager.createTable(tableName, columns);
    }

    /**
     * Get table schema.
     */
    public TableSchema getTableSchema(String tableName) {
        return schemaManager.getTableSchema(tableName);
    }

    // ==================== Component Getters ====================

    public OffHeapMemoryManager getMemoryManager() { return memoryManager; }
    public AuthenticationManager getAuthManager() { return authManager; }
    public PrivilegeManager getPrivilegeManager() { return privilegeManager; }
    public TransactionManager getTransactionManager() { return transactionManager; }
    public AuditLogger getAuditLogger() { return auditLogger; }
    public DeleteHandler getDeleteHandler() { return deleteHandler; }
    public VersionManager getVersionManager() { return versionManager; }
    public QueryParser getQueryParser() { return queryParser; }
    public IndexManager getIndexManager() { return indexManager; }
    public QueryCache getQueryCache() { return queryCache; }
    public ReplicationManager getReplicationManager() { return replicationManager; }
    public BackupManager getBackupManager() { return backupManager; }
    public SchemaManager getSchemaManager() { return schemaManager; }
    public ConnectionManager getConnectionManager() { return connectionManager; }

    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        logger.info("================================");
        logger.info("TemporalDB v2.0.0");
        logger.info("================================");
        logger.info("Java version: {}", System.getProperty("java.version"));
        logger.info("Available processors: {}", Runtime.getRuntime().availableProcessors());

        TemporalDBServer server = new TemporalDBServer();
        server.start();

        // Add shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown signal received");
            server.stop();
        }));

        // Keep server running
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.info("Server interrupted");
        }
    }
}