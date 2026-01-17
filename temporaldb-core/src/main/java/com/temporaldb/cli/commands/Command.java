package com.temporaldb.cli.commands;

import java.util.List;

/**
 * Base interface for all CLI commands.
 */
public interface Command {
    /**
     * Get command name.
     */
    String getName();

    /**
     * Get command description.
     */
    String getDescription();

    /**
     * Get command usage.
     */
    String getUsage();

    /**
     * Get command examples.
     */
    List<String> getExamples();


    /**
     * Execute the command.
     */
    boolean execute(String[] args);
}

