package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.replication.BackupPoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * List snapshots command.
 */
public class ListSnapshotsCommand implements Command {
    private final TemporalDBServer server;

    public ListSnapshotsCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "snapshots";
    }

    @Override
    public String getDescription() {
        return "List all temporal snapshots";
    }

    @Override
    public String getUsage() {
        return "snapshots";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("snapshots");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            List<BackupPoint> backups = server.getBackupManager().listBackups();

            if (backups.isEmpty()) {
                System.out.println("No snapshots found");
                return true;
            }

            System.out.println("\nTemporal Snapshots:\n");
            System.out.printf("%-15s %-25s %-20s\n", "Name", "Created", "Status");
            System.out.println(String.join("", Collections.nCopies(60, "-")));

            for (BackupPoint backup : backups) {
                System.out.printf("%-15s %-25s %-20s\n",
                        backup.getName(),
                        new java.util.Date(backup.getTimestamp()),
                        backup.isVerified() ? "VERIFIED" : "PENDING");
            }
            System.out.println();

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
