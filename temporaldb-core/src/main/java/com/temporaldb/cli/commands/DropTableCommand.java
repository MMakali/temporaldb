package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Drop table command.
 */
public class DropTableCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(DropTableCommand.class);
    private final TemporalDBServer server;

    public DropTableCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "drop";
    }

    @Override
    public String getDescription() {
        return "Drop a table";
    }

    @Override
    public String getUsage() {
        return "drop table <name>";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "drop table users",
                "drop table orders"
        );
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length < 2 || !args[0].equalsIgnoreCase("table")) {
                System.out.println("Usage: " + getUsage());
                return false;
            }

            String tableName = args[1];
            server.getSchemaManager().dropTable(tableName);
            System.out.println("✓ Table '" + tableName + "' dropped");
            logger.info("Dropped table: {}", tableName);
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
