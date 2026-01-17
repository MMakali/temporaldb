package com.temporaldb.core.replication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Coordinates synchronization with replica nodes.
 */
public class SyncCoordinator {
    private static final Logger logger = LoggerFactory.getLogger(SyncCoordinator.class);

    /**
     * Sync with replica node.
     */
    public void syncWithReplica(ReplicaNode replica, ReplicationEvent event) {
        if (!replica.isHealthy()) {
            logger.warn("Skipping sync with unhealthy replica: {}", replica.getReplicaId());
            return;
        }

        try {
            // Simulate network sync
            long startTime = System.currentTimeMillis();
            simulateNetworkSync(replica, event);
            long duration = System.currentTimeMillis() - startTime;

            replica.setLastSyncTime(System.currentTimeMillis());
            replica.setReplicationLag(duration);
            event.setApplied(true);

            logger.debug("Synced with replica {} in {}ms", replica.getReplicaId(), duration);
        } catch (Exception e) {
            logger.error("Sync failed with replica {}", replica.getReplicaId(), e);
            replica.setHealthy(false);
        }
    }

    private void simulateNetworkSync(ReplicaNode replica, ReplicationEvent event) {
        // Simulate network latency
        try {
            Thread.sleep(10);  // 10ms network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
