package com.temporaldb.cli.commands;

import java.util.Arrays;
import java.util.List;

/**
 * Clear command.
 */
public class ClearCommand implements Command {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Clear the screen";
    }

    @Override
    public String getUsage() {
        return "clear";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList("clear");
    }


    @Override
    public boolean execute(String[] args) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        return true;
    }
}
