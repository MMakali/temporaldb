package com.temporaldb.core.temporal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Manages temporal versions for the database.
 * Provides version control and time-travel query support.
 */
public class VersionManager {
    private static final Logger logger = LoggerFactory.getLogger(VersionManager.class);

    private final AtomicLong versionIdGenerator = new AtomicLong(0);
    private final Map<Long, TemporalVersion> versions = new ConcurrentHashMap<>();
    private final Map<String, NavigableMap<Long, TemporalVersion>> tableVersions = new ConcurrentHashMap<>();

    /**
     * Create a new version.
     */
    public TemporalVersion createVersion() {
        long versionId = versionIdGenerator.incrementAndGet();
        TemporalVersion version = new TemporalVersion(versionId, System.currentTimeMillis());
        versions.put(versionId, version);
        logger.debug("Created version: {}", versionId);
        return version;
    }

    /**
     * Get version by ID.
     */
    public TemporalVersion getVersion(long versionId) {
        return versions.get(versionId);
    }

    /**
     * Get latest version.
     */
    public TemporalVersion getLatestVersion() {
        if (versions.isEmpty()) return null;
        long maxId = versions.keySet().stream().mapToLong(Long::longValue).max().orElse(-1);
        return versions.get(maxId);
    }

    /**
     * Get version valid at specific timestamp.
     */
    public TemporalVersion getVersionAt(long timestamp) {
        return versions.values().stream()
                .filter(v -> v.isValidAt(timestamp))
                .max(Comparator.comparingLong(TemporalVersion::getVersionId))
                .orElse(null);
    }

    /**
     * Register version for table.
     */
    public void registerTableVersion(String tableName, TemporalVersion version) {
        tableVersions.computeIfAbsent(tableName, k -> new TreeMap<>())
                .put(version.getVersionId(), version);
    }

    /**
     * Get all versions for table.
     */
    public List<TemporalVersion> getTableVersions(String tableName) {
        NavigableMap<Long, TemporalVersion> map = tableVersions.get(tableName);
        return map != null ? new ArrayList<>(map.values()) : Collections.emptyList();
    }

    /**
     * Get version count.
     */
    public long getVersionCount() {
        return versions.size();
    }
}
