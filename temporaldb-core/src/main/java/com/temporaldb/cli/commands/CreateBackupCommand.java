package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Create backup command.
 */
public class CreateBackupCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CreateBackupCommand.class);
    private final TemporalDBServer server;

    public CreateBackupCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "backup";
    }

    @Override
    public String getDescription() {
        return "Create database backup";
    }

    @Override
    public String getUsage() {
        return "backup <name> [description]";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "backup daily_backup",
                "backup weekly_backup \"Weekly full backup\""
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
            String backupId = server.createBackup(name);

            System.out.println("✓ Backup '" + name + "' created");
            System.out.println("  ID: " + backupId);
            System.out.println("  Time: " + new java.util.Date());

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
