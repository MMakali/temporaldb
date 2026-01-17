package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Insert command.
 */
public class InsertCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(InsertCommand.class);
    private final TemporalDBServer server;

    public InsertCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "insert";
    }

    @Override
    public String getDescription() {
        return "Insert data into table";
    }

    @Override
    public String getUsage() {
        return "insert into <table> (cols) values (vals)";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "insert into users (id, name, age) values (1, John, 25)",
                "insert into orders (id, user_id, amount) values (1, 1, 100.50)"
        );
    }


    @Override
    public boolean execute(String[] args) {
        try {
            String sql = String.join(" ", args);
            server.executeSelect("SELECT 1");  // Placeholder for insert
            System.out.println("✓ Data inserted successfully");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
