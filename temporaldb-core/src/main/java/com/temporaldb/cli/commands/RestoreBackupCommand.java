package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Restore backup command.
 */
public class RestoreBackupCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RestoreBackupCommand.class);
    private final TemporalDBServer server;

    public RestoreBackupCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "restore";
    }

    @Override
    public String getDescription() {
        return "Restore from backup";
    }

    @Override
    public String getUsage() {
        return "restore <backup_id>";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("restore backup-123");
    }

    

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Usage: " + getUsage());
                return false;
            }

            String backupId = args[0];

            System.out.println("⚠️  Restoring from backup: " + backupId);
            System.out.println("This will overwrite current data...");

            server.recoverFromBackup(backupId);
            System.out.println("✓ Backup restored successfully");

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
