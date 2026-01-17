package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.monitoring.SystemHealth;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Show health command.
 */
public class ShowHealthCommand implements Command {
    private final TemporalDBServer server;

    public ShowHealthCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "health";
    }

    @Override
    public String getDescription() {
        return "Show system health";
    }

    @Override
    public String getUsage() {
        return "health";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("health");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            SystemHealth health = server.getSystemHealth();

            System.out.println("\nSystem Health:\n");
            System.out.println("  Status: " + (health.isHealthy() ? "✓ HEALTHY" : "✗ ISSUES"));
            System.out.println("  Uptime: " + (health.getUptime() / 1000) + " seconds");
            System.out.println("  CPU Usage: " + health.getCpuUsage() + "%");
            System.out.println("  Memory Usage: " + health.getMemoryUsage() + "%");

            if (!health.getIssues().isEmpty()) {
                System.out.println("\n  Issues:");
                for (Map.Entry<String, String> issue : health.getIssues().entrySet()) {
                    System.out.println("    - " + issue.getKey() + ": " + issue.getValue());
                }
            }
            System.out.println();

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
