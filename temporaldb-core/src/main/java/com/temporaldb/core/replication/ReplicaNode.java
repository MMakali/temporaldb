package com.temporaldb.core.replication;

/**
 * Represents a replica node.
 */
public class ReplicaNode {
    private final String replicaId;
    private final String host;
    private final int port;
    private final ReplicationManager manager;
    private volatile boolean healthy = true;
    private volatile long lastSyncTime;
    private volatile long replicationLag = 0;

    public ReplicaNode(String replicaId, String host, int port, ReplicationManager manager) {
        this.replicaId = replicaId;
        this.host = host;
        this.port = port;
        this.manager = manager;
        this.lastSyncTime = System.currentTimeMillis();
    }

    public String getReplicaId() {
        return replicaId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public long getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(long time) {
        this.lastSyncTime = time;
    }

    public long getReplicationLag() {
        return replicationLag;
    }

    public void setReplicationLag(long lag) {
        this.replicationLag = lag;
    }
}
