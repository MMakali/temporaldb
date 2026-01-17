package com.temporaldb.core.replication;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages data replication across nodes.
 */
public class ReplicationManager {
    private static final Logger logger = LoggerFactory.getLogger(ReplicationManager.class);

    private final String nodeId;
    private final List<ReplicaNode> replicas = new CopyOnWriteArrayList<>();
    private final ReplicationLog replicationLog = new ReplicationLog();
    private final SyncCoordinator coordinator = new SyncCoordinator();
    private final ConflictResolver conflictResolver = new ConflictResolver();

    public ReplicationManager(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * Add a replica node.
     */
    public void addReplica(String replicaId, String host, int port) {
        ReplicaNode replica = new ReplicaNode(replicaId, host, port, this);
        replicas.add(replica);
        logger.info("Added replica: {} at {}:{}", replicaId, host, port);
    }

    /**
     * Replicate write operation to all replicas.
     */
    public void replicateWrite(String operation, Map<String, Object> data) {
        ReplicationEvent event = new ReplicationEvent(
                System.currentTimeMillis(),
                operation,
                data,
                nodeId
        );

        replicationLog.log(event);

        for (ReplicaNode replica : replicas) {
            coordinator.syncWithReplica(replica, event);
        }

        logger.debug("Replicated write operation to {} replicas", replicas.size());
    }

    /**
     * Get replication lag.
     */
    public long getReplicationLag(String replicaId) {
        ReplicaNode replica = findReplica(replicaId);
        if (replica != null) {
            return System.currentTimeMillis() - replica.getLastSyncTime();
        }
        return -1;
    }

    /**
     * Get all replicas status.
     */
    public List<ReplicaNode> getReplicas() {
        return new ArrayList<>(replicas);
    }

    private ReplicaNode findReplica(String replicaId) {
        return replicas.stream()
                .filter(r -> r.getReplicaId().equals(replicaId))
                .findFirst()
                .orElse(null);
    }
}

