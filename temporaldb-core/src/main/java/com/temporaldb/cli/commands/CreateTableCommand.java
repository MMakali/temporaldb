package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.core.management.ColumnDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create table command.
 */
public class CreateTableCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CreateTableCommand.class);
    private final TemporalDBServer server;

    public CreateTableCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() { return "create"; }

    @Override
    public String getDescription() { return "Create a new table"; }

    @Override
    public String getUsage() { return "create table <name> (col1 type1, col2 type2, ...)"; }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "create table users (id LONG, name STRING, age INT)",
                "create table orders (id LONG, user_id LONG, amount DOUBLE)"
        );
    }


    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length < 3 || !args[0].equalsIgnoreCase("table")) {
                System.out.println("Usage: " + getUsage());
                return false;
            }

            String tableName = args[1];

            // Parse column definitions
            List<ColumnDefinition> columns = new ArrayList<>();
            for (int i = 2; i < args.length; i++) {
                String[] parts = args[i].split(" ");
                if (parts.length >= 2) {
                    columns.add(new ColumnDefinition(parts[0], parts[1]));
                }
            }

            server.createTable(tableName, columns);
            System.out.println("✓ Table '" + tableName + "' created with " + columns.size() + " columns");
            logger.info("Created table: {}", tableName);
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}

