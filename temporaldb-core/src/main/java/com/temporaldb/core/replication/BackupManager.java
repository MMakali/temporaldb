package com.temporaldb.core.replication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages database backups.
 */
public class BackupManager {
    private static final Logger logger = LoggerFactory.getLogger(BackupManager.class);

    private final List<BackupPoint> backupPoints = new CopyOnWriteArrayList<>();
    private final BackupLog backupLog = new BackupLog();

    /**
     * Create a backup point.
     */
    public BackupPoint createBackup(String name, String description) {
        BackupPoint backup = new BackupPoint(
                UUID.randomUUID().toString(),
                name,
                description,
                System.currentTimeMillis()
        );

        backupPoints.add(backup);
        backupLog.log(backup);
        logger.info("Created backup: {} - {}", name, description);
        return backup;
    }

    /**
     * Get backup by ID.
     */
    public BackupPoint getBackup(String backupId) {
        return backupPoints.stream()
                .filter(b -> b.getId().equals(backupId))
                .findFirst()
                .orElse(null);
    }

    /**
     * List all backups.
     */
    public List<BackupPoint> listBackups() {
        return new ArrayList<>(backupPoints);
    }

    /**
     * Delete backup.
     */
    public boolean deleteBackup(String backupId) {
        return backupPoints.removeIf(b -> b.getId().equals(backupId));
    }
}
