package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.management.ColumnDefinition;
import com.temporaldb.core.management.TableSchema;

import java.util.Arrays;
import java.util.List;

/**
 * Show columns command.
 */
public class ShowColumnsCommand implements Command {
    private final TemporalDBServer server;

    public ShowColumnsCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "columns";
    }

    @Override
    public String getDescription() {
        return "Show table columns";
    }

    @Override
    public String getUsage() {
        return "columns <table>";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("columns users", "columns orders");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Usage: " + getUsage());
                return false;
            }

            String tableName = args[0];
            TableSchema schema = server.getTableSchema(tableName);

            if (schema == null) {
                System.out.println("✗ Table not found: " + tableName);
                return false;
            }

            System.out.println("\nColumns in " + tableName + ":\n");
            for (ColumnDefinition col : schema.getColumns()) {
                System.out.printf("  • %-20s %s\n", col.getName(), col.getType());
            }
            System.out.println();
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
