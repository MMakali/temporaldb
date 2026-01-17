package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.monitoring.DatabaseStatistics;

import java.util.Arrays;
import java.util.List;

/**
 * Show statistics command.
 */
public class ShowStatsCommand implements Command {
    private final TemporalDBServer server;

    public ShowStatsCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "Show database statistics";
    }

    @Override
    public String getUsage() {
        return "stats [table]";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("stats", "stats users");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            DatabaseStatistics stats = server.getDatabaseStatistics();

            System.out.println("\nDatabase Statistics:\n");
            System.out.println("  Total Rows: " + stats.getTotalRows());
            System.out.println("  Total Queries: " + stats.getTotalQueries());
            System.out.println("  Total Transactions: " + stats.getTotalTransactions());
            System.out.println();

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
