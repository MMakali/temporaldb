package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Select command.
 */
public class SelectCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(SelectCommand.class);
    private final TemporalDBServer server;

    public SelectCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public String getDescription() {
        return "Query data from table";
    }

    @Override
    public String getUsage() {
        return "select <columns> from <table> [where conditions]";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "select * from users",
                "select id, name from users where age > 18",
                "select * from users as of timestamp"
        );
    }

    

    @Override
    public boolean execute(String[] args) {
        try {
            String sql = "SELECT " + String.join(" ", args);
            List<Map<String, Object>> results = server.executeSelect(sql);

            if (results.isEmpty()) {
                System.out.println("No results found");
                return true;
            }

            // Print header
            Set<String> columns = results.get(0).keySet();
            for (String col : columns) {
                System.out.printf("%-20s ", col);
            }
            System.out.println();
            System.out.println(String.join("", Collections.nCopies(20 * columns.size(), "-")));

            // Print rows
            for (Map<String, Object> row : results) {
                for (String col : columns) {
                    System.out.printf("%-20s ", row.get(col));
                }
                System.out.println();
            }

            System.out.println("\n" + results.size() + " rows returned");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            logger.error("Query error", e);
            return false;
        }
    }
}
