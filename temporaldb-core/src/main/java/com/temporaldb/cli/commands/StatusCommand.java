package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;

import java.util.Arrays;
import java.util.List;

/**
 * Status command.
 */
public class StatusCommand implements Command {
    private final TemporalDBServer server;

    public StatusCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getDescription() {
        return "Show server status";
    }

    @Override
    public String getUsage() {
        return "status";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("status");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            System.out.println("\nServer Status:");
            System.out.println("  Status: " + (server.isRunning() ? "RUNNING" : "STOPPED"));
            System.out.println("  Version: 2.0.0");
            System.out.println("  Java: " + System.getProperty("java.version"));
            System.out.println("  Processors: " + Runtime.getRuntime().availableProcessors());
            System.out.println();
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
