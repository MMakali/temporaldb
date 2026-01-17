package com.temporaldb.cli.commands;

import java.util.Arrays;
import java.util.List;

/**
 * Exit command.
 */
public class ExitCommand implements Command {
    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "Exit the CLI";
    }

    @Override
    public String getUsage() {
        return "exit";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("exit", "quit");
    }


    @Override
    public boolean execute(String[] args) {
        System.out.println("Goodbye!");
        return true;
    }
}
