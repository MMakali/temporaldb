package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.temporal.TemporalVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

// ==================== TEMPORAL COMMANDS ====================

/**
 * Show version command - Display temporal versions.
 */
public class ShowVersionCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ShowVersionCommand.class);
    private final TemporalDBServer server;

    public ShowVersionCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() { return "version"; }

    @Override
    public String getDescription() { return "Show current database version"; }

    @Override
    public String getUsage() { return "version"; }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("version", "version list");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            TemporalVersion current = server.getCurrentVersion();

            if (current == null) {
                System.out.println("No versions available");
                return true;
            }

            System.out.println("\nCurrent Version:");
            System.out.println("  ID: " + current.getVersionId());
            System.out.println("  Transaction Time: " + new java.util.Date(current.getTransactionTime()));
            System.out.println("  Valid From: " + new java.util.Date(current.getValidTimeStart()));
            System.out.println("  Valid To: " + (current.getValidTimeEnd() == Long.MAX_VALUE ?
                    "OPEN" : new java.util.Date(current.getValidTimeEnd())));
            System.out.println();

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}


