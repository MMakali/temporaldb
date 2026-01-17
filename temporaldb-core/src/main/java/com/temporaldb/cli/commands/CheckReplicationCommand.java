package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.replication.ReplicaNode;

import java.util.Arrays;
import java.util.List;

/**
 * Check replication command.
 */
public class CheckReplicationCommand implements Command {
    private final TemporalDBServer server;

    public CheckReplicationCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "replag";
    }

    @Override
    public String getDescription() {
        return "Check replication lag";
    }

    @Override
    public String getUsage() {
        return "replag [replica_id]";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("replag", "replag replica1");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            List<ReplicaNode> replicas = server.getReplicationManager().getReplicas();

            System.out.println("\n️Replication Lag:\n");

            for (ReplicaNode replica : replicas) {
                long lag = server.getReplicationManager().getReplicationLag(replica.getReplicaId());
                System.out.printf("  %s: %d ms\n", replica.getReplicaId(), lag);
            }
            System.out.println();

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
