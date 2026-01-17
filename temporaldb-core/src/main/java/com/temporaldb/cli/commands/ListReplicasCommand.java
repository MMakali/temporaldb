package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.replication.ReplicaNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * List replicas command.
 */
public class ListReplicasCommand implements Command {
    private final TemporalDBServer server;

    public ListReplicasCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "replicas";
    }

    @Override
    public String getDescription() {
        return "List all replica nodes";
    }

    @Override
    public String getUsage() {
        return "replicas";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("replicas");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            List<ReplicaNode> replicas = server.getReplicationManager().getReplicas();

            if (replicas.isEmpty()) {
                System.out.println("No replicas configured");
                return true;
            }

            System.out.println("\nReplica Nodes:\n");
            System.out.printf("%-15s %-20s %-10s\n", "ID", "Host:Port", "Status");
            System.out.println(String.join("", Collections.nCopies(45, "-")));

            for (ReplicaNode replica : replicas) {
                System.out.printf("%-15s %-20s %-10s\n",
                        replica.getReplicaId(),
                        replica.getHost() + ":" + replica.getPort(),
                        replica.isHealthy() ? "HEALTHY" : "UNHEALTHY");
            }
            System.out.println();

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
