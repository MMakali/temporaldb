package com.temporaldb.core.replication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Resolves conflicts in replicated data.
 */
public class ConflictResolver {
    private static final Logger logger = LoggerFactory.getLogger(ConflictResolver.class);

    /**
     * Resolve conflict between two versions.
     */
    public Map<String, Object> resolve(Map<String, Object> local, Map<String, Object> remote) {
        // Simple strategy: use most recent timestamp
        Long localTs = (Long) local.get("_timestamp");
        Long remoteTs = (Long) remote.get("_timestamp");

        if (localTs == null || remoteTs == null) {
            return local;  // Default to local
        }

        if (remoteTs > localTs) {
            logger.info("Conflict resolution: using remote version (timestamp: {})", remoteTs);
            return remote;
        } else {
            logger.info("Conflict resolution: using local version (timestamp: {})", localTs);
            return local;
        }
    }
}
