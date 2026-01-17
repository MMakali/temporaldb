package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Create index command.
 */
public class CreateIndexCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CreateIndexCommand.class);
    private final TemporalDBServer server;

    public CreateIndexCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "index";
    }

    @Override
    public String getDescription() {
        return "Create index on column";
    }

    @Override
    public String getUsage() {
        return "index <name> on <table>(<column>) [BTREE|HASH]";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "index idx_age on users(age) BTREE",
                "index idx_id on users(id) HASH"
        );
    }

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length < 4) {
                System.out.println("Usage: " + getUsage());
                return false;
            }

            String indexName = args[0];
            String tableName = args[2].split("\\(")[0];
            String columnName = args[2].split("[()]")[1];
            String type = args.length > 3 ? args[3] : "BTREE";

            server.createIndex(indexName, tableName, columnName, type);
            System.out.println("✓ Index '" + indexName + "' created on " + tableName + "(" + columnName + ")");
            System.out.println("  Type: " + type);

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
