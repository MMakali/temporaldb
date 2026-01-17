package com.temporaldb.core.audit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs all database operations for audit trail.
 */
public class AuditLogger {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);

    private final AtomicLong auditIdGenerator = new AtomicLong(0);
    private final Map<Long, AuditLog> auditLogs = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log an operation.
     * @param operation Operation type
     * @param username User performing operation
     * @param tableName Table name
     * @param rowId Row ID
     * @param beforeState Before state as JSON
     * @param afterState After state as JSON
     * @param success Whether operation succeeded
     */
    public void log(String operation, String username, String tableName, long rowId,
                    String beforeState, String afterState, boolean success) {
        log(operation, username, tableName, rowId, beforeState, afterState, success, null);
    }

    /**
     * Log an operation with error.
     */
    public void log(String operation, String username, String tableName, long rowId,
                    String beforeState, String afterState, boolean success, String errorMessage) {
        long auditId = auditIdGenerator.incrementAndGet();
        AuditLog auditLog = new AuditLog(
                auditId, operation, username, tableName, rowId,
                beforeState, afterState, success, errorMessage
        );
        auditLogs.put(auditId, auditLog);

        logger.info("Audit: [{}] {} on {}.{} by {} - {}",
                auditId, operation, tableName, rowId, username,
                success ? "SUCCESS" : "FAILED");
    }

    /**
     * Get audit log entry.
     * @param id Audit ID
     * @return AuditLog or null
     */
    public AuditLog getAuditLog(long id) {
        return auditLogs.get(id);
    }

    /**
     * Query audit logs.
     * @param username Filter by username (null = all)
     * @param operation Filter by operation (null = all)
     * @return List of matching audit logs
     */
    public List<AuditLog> queryAuditLogs(String username, String operation) {
        return auditLogs.values().stream()
                .filter(log -> username == null || log.getUsername().equals(username))
                .filter(log -> operation == null || log.getOperation().equals(operation))
                .sorted(Comparator.comparingLong(AuditLog::getId).reversed())
                .toList();
    }

    public long getAuditLogCount() {
        return auditLogs.size();
    }
}