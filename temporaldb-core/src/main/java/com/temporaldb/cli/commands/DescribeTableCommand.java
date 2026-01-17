package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.management.ColumnDefinition;
import com.temporaldb.core.management.TableSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Describe table command.
 */
public class DescribeTableCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(DescribeTableCommand.class);
    private final TemporalDBServer server;

    public DescribeTableCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "describe";
    }

    @Override
    public String getDescription() {
        return "Show table schema";
    }

    @Override
    public String getUsage() {
        return "describe <table>";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "describe users",
                "describe orders"
        );
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

            System.out.println("\nTable: " + tableName);
            System.out.println(String.join("", Collections.nCopies(60, "-")));
            System.out.printf("%-20s %-15s %-10s\n", "Column", "Type", "Nullable");
            System.out.println(String.join("", Collections.nCopies(60, "-")));

            for (ColumnDefinition col : schema.getColumns()) {
                System.out.printf("%-20s %-15s %-10s\n",
                        col.getName(), col.getType(), col.isNullable() ? "YES" : "NO");
            }

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
