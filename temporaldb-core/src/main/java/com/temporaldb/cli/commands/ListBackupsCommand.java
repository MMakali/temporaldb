package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.replication.BackupPoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * List backups command.
 */
public class ListBackupsCommand implements Command {
    private final TemporalDBServer server;

    public ListBackupsCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "backups";
    }

    @Override
    public String getDescription() {
        return "List all backups";
    }

    @Override
    public String getUsage() {
        return "backups";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("backups");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            List<BackupPoint> backups = server.getBackupManager().listBackups();

            if (backups.isEmpty()) {
                System.out.println("No backups found");
                return true;
            }

            System.out.println("\nBackups:\n");
            System.out.printf("%-20s %-25s\n", "Name", "Created");
            System.out.println(String.join("", Collections.nCopies(45, "-")));

            for (BackupPoint backup : backups) {
                System.out.printf("%-20s %-25s\n",
                        backup.getName(),
                        new java.util.Date(backup.getTimestamp()));
            }
            System.out.println();

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
