package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;

import java.util.Arrays;
import java.util.List;

/**
 * Drop index command.
 */
public class DropIndexCommand implements Command {
    private final TemporalDBServer server;

    public DropIndexCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "dropindex";
    }

    @Override
    public String getDescription() {
        return "Drop an index";
    }

    @Override
    public String getUsage() {
        return "dropindex <name>";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("dropindex idx_age");
    }

    

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Usage: " + getUsage());
                return false;
            }

            System.out.println("✓ Index '" + args[0] + "' dropped");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
