package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.management.TableSchema;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * List tables command.
 */
public class ListTablesCommand implements Command {
    private final TemporalDBServer server;

    public ListTablesCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "tables";
    }

    @Override
    public String getDescription() {
        return "List all tables";
    }

    @Override
    public String getUsage() {
        return "tables";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("tables");
    }


    @Override
    public boolean execute(String[] args) {
        try {
            Collection<TableSchema> tables = server.getSchemaManager().getAllTables();

            if (tables.isEmpty()) {
                System.out.println("No tables found");
                return true;
            }

            System.out.println("\nTables:\n");
            for (TableSchema table : tables) {
                System.out.printf("  • %s (%d columns)\n", table.getTableName(), table.getColumns().size());
            }
            System.out.println();
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
