package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Time travel query command.
 */
public class TimeTravelQuery implements Command {
    private static final Logger logger = LoggerFactory.getLogger(TimeTravelQuery.class);
    private final TemporalDBServer server;

    public TimeTravelQuery(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "asof";
    }

    @Override
    public String getDescription() {
        return "Query data as of specific timestamp";
    }

    @Override
    public String getUsage() {
        return "asof <timestamp> <query>";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "asof 1609459200000 select * from users",
                "asof 1640995200000 select id, name from users"
        );
    }

    

    @Override
    public boolean execute(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Usage: " + getUsage());
                return false;
            }

            long timestamp = Long.parseLong(args[0]);
            String query = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            System.out.println("⏰ Querying as of: " + new java.util.Date(timestamp));

            List<Map<String, Object>> results = server.executeTemporalSelect(query, timestamp);

            if (results.isEmpty()) {
                System.out.println("No results found");
                return true;
            }

            // Print results
            System.out.println("\n" + results.size() + " rows found:\n");
            for (Map<String, Object> row : results) {
                System.out.println(row);
            }

            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
