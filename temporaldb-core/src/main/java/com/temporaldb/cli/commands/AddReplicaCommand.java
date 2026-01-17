package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Add replica command.
 */
public class AddReplicaCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AddReplicaCommand.class);
    private final TemporalDBServer server;

    public AddReplicaCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "replica";
    }

    @Override
    public String getDescription() {
        return "Add replica node";
    }

    @Override
    public String getUsage() {
        return "replica <id> <host> <port>";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "replica replica1 192.168.1.100 5432",
                "replica replica2 192.168.1.101 5432"
        );
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length < 3) {
                System.out.println("Usage: " + getUsage());
                return false;
            }

            String replicaId = args[0];
            String host = args[1];
            int port = Integer.parseInt(args[2]);

            server.addReplicaNode(replicaId, host, port);
            System.out.println("✓ Replica '" + replicaId + "' added");
            System.out.println("  Host: " + host + ":" + port);

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
