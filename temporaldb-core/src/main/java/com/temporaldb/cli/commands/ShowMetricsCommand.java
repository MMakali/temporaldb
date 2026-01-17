package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.monitoring.PerformanceMetrics;

import java.util.Arrays;
import java.util.List;

/**
 * Show metrics command.
 */
public class ShowMetricsCommand implements Command {
    private final TemporalDBServer server;

    public ShowMetricsCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "metrics";
    }

    @Override
    public String getDescription() {
        return "Show performance metrics";
    }

    @Override
    public String getUsage() {
        return "metrics";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("metrics");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            PerformanceMetrics metrics = server.getPerformanceMetrics();

            System.out.println("\nPerformance Metrics:\n");
            System.out.println("  Average Query Time: " + metrics.getAverageQueryTime() + " ms");
            System.out.println("  Success Rate: " + metrics.getSuccessRate() + "%");
            System.out.println("  Cache Hit Ratio: " + (metrics.getCacheHitRatio() * 100) + "%");
            System.out.println();

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
