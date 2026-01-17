package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Create snapshot command.
 */
public class CreateSnapshotCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CreateSnapshotCommand.class);
    private final TemporalDBServer server;

    public CreateSnapshotCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "snapshot";
    }

    @Override
    public String getDescription() {
        return "Create temporal snapshot";
    }

    @Override
    public String getUsage() {
        return "snapshot <name> [description]";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "snapshot Q1_2023",
                "snapshot Q1_2023 \"First quarter backup\""
        );
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Usage: " + getUsage());
                return false;
            }

            String name = args[0];
            String description = args.length > 1 ? args[1] : "";

            String snapshotId = server.createTemporalSnapshot(name);
            System.out.println("✓ Snapshot '" + name + "' created");
            System.out.println("  ID: " + snapshotId);
            System.out.println("  Time: " + new java.util.Date());

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
