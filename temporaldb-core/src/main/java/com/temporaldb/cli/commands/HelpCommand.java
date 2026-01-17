package com.temporaldb.cli.commands;

import java.util.Arrays;
import java.util.List;

/**
 * Help command.
 */
public class HelpCommand implements Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show help information";
    }

    @Override
    public String getUsage() {
        return "help [command]";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "help",
                "help select",
                "help insert"
        );
    }


    @Override
    public boolean execute(String[] args) {
        System.out.println("Use 'help <command>' for specific command help");
        return true;
    }
}
