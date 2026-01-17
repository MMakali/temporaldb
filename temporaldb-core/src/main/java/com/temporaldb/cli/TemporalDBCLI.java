package com.temporaldb.cli;

import com.temporaldb.TemporalDBServer;
import com.temporaldb.cli.commands.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.io.*;

/**
 * Main entry point for TemporalDB CLI.
 * Provides interactive command-line interface for database operations.
 */
public class TemporalDBCLI {
    private static final Logger logger = LoggerFactory.getLogger(TemporalDBCLI.class);

    private final TemporalDBServer server;
    private final CommandRegistry commandRegistry;
    private final BufferedReader reader;
    private volatile boolean running = false;
    /**
     * -- SETTER --
     *  Set current user.
     * -- GETTER --
     *  Get current user.

     */
    @Getter
    @Setter
    private String currentUser = null;

    /**
     * Create CLI instance.
     */
    public TemporalDBCLI(TemporalDBServer server) {
        this.server = server;
        this.commandRegistry = new CommandRegistry(server);
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Start the CLI.
     */
    public void start() {
        running = true;
        logger.info("TemporalDB CLI Started");

        printWelcome();

        try {
            commandLoop();
        } catch (Exception e) {
            logger.error("CLI error", e);
            System.err.println("Error: " + e.getMessage());
        } finally {
            stop();
        }
    }

    /**
     * Main command loop.
     */
    private void commandLoop() throws IOException {
        while (running) {
            try {
                System.out.print(getPrompt());
                System.out.flush();

                String line = reader.readLine();
                if (line == null) {
                    break;  // EOF
                }

                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                    running = false;
                    break;
                }

                if (line.startsWith("help")) {
                    handleHelp(line);
                    continue;
                }

                // Parse and execute command
                executeCommand(line);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                logger.debug("Command execution error", e);
            }
        }
    }

    /**
     * Execute a command.
     */
    private void executeCommand(String commandLine) {
        try {
            String[] parts = parseCommand(commandLine);
            if (parts.length == 0) return;

            String commandName = parts[0].toLowerCase();
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);

            Command command = commandRegistry.getCommand(commandName);
            if (command == null) {
                System.out.println("Unknown command: " + commandName);
                System.out.println("Type 'help' for available commands");
                return;
            }


            // Execute command
            long startTime = System.currentTimeMillis();
            boolean success = command.execute(args);
            long duration = System.currentTimeMillis() - startTime;

            if (success) {
                System.out.println("✓ Command executed in " + duration + "ms");
            } else {
                System.out.println("✗ Command failed");
            }

        } catch (Exception e) {
            System.err.println("Error executing command: " + e.getMessage());
            logger.error("Command error", e);
        }
    }

    /**
     * Handle help command.
     */
    private void handleHelp(String line) {
        String[] parts = parseCommand(line);

        if (parts.length == 1) {
            printGeneralHelp();
        } else {
            String commandName = parts[1].toLowerCase();
            Command command = commandRegistry.getCommand(commandName);
            if (command != null) {
                System.out.println("\n" + command.getName() + " - " + command.getDescription());
                System.out.println("Usage: " + command.getUsage());
                System.out.println("Examples:");
                for (String example : command.getExamples()) {
                    System.out.println("  " + example);
                }
            } else {
                System.out.println("Unknown command: " + commandName);
            }
        }
    }

    /**
     * Parse command line into parts, handling quoted strings.
     */
    private String[] parseCommand(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (current.length() > 0) {
                    parts.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            parts.add(current.toString());
        }

        return parts.toArray(new String[0]);
    }

    /**
     * Get current prompt.
     */
    private String getPrompt() {
        if (currentUser != null) {
            return "temporaldb(" + currentUser + ")> ";
        } else {
            return "temporaldb> ";
        }
    }

    /**
     * Print welcome message.
     */
    private void printWelcome() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   TemporalDB Command Line Interface    ║");
        System.out.println("║          Version 2.0.0                 ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Type 'help' for available commands");
        System.out.println("Type 'exit' to quit");
        System.out.println();
    }

    /**
     * Print general help.
     */
    private void printGeneralHelp() {
        System.out.println("\nAvailable Commands:\n");

        List<Command> commands = commandRegistry.getAllCommands();
        for (Command cmd : commands) {
            System.out.printf("  %-15s %s\n", cmd.getName(), cmd.getDescription());
        }

        System.out.println("\nFor help on a specific command, type: help <command>");
    }

    /**
     * Stop the CLI.
     */
    public void stop() {
        running = false;
        try {
            reader.close();
        } catch (IOException e) {
            logger.error("Error closing reader", e);
        }
        logger.info("TemporalDB CLI Stopped");
    }

    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        try {
            // Start server
            TemporalDBServer server = new TemporalDBServer();
            server.start();

            // Start CLI
            TemporalDBCLI cli = new TemporalDBCLI(server);
            cli.start();

            // Cleanup
            server.stop();

        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}