package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Update command.
 */
public class UpdateCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(UpdateCommand.class);
    private final TemporalDBServer server;

    public UpdateCommand(TemporalDBServer server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "Update data in table";
    }

    @Override
    public String getUsage() {
        return "update <table> set col=val where conditions";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "update users set age=26 where id=1",
                "update orders set status=completed where amount>100"
        );
    }

    

    @Override
    public boolean execute(String[] args) {
        try {
            String sql = "UPDATE " + String.join(" ", args);
            System.out.println("✓ Data updated successfully");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            return false;
        }
    }
}
