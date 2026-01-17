package com.temporaldb.cli.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * CLI utilities.
 */
public class CLIUtils {
    private static final Logger logger = LoggerFactory.getLogger(CLIUtils.class);

    /**
     * Parse key-value arguments.
     */
    public static Map<String, String> parseKeyValueArgs(String[] args) {
        Map<String, String> result = new HashMap<>();

        for (String arg : args) {
            if (arg.contains("=")) {
                String[] parts = arg.split("=", 2);
                result.put(parts[0], parts[1]);
            }
        }

        return result;
    }

    /**
     * Prompt user for input.
     */
    public static String promptUser(String prompt) {
        System.out.print(prompt);
        System.out.flush();

        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }

    /**
     * Prompt user for confirmation.
     */
    public static boolean confirmUser(String prompt) {
        String response = promptUser(prompt + " (yes/no): ");
        return response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y");
    }

    /**
     * Read masked input (for passwords).
     */
    public static String promptPassword(String prompt) {
        System.out.print(prompt);
        System.out.flush();

        Console console = System.console();
        if (console != null) {
            char[] password = console.readPassword();
            return new String(password);
        } else {
            // Fallback for non-interactive environments
            try (Scanner scanner = new Scanner(System.in)) {
                return scanner.nextLine();
            }
        }
    }

    /**
     * Print colored text.
     */
    public static void printSuccess(String message) {
        System.out.println("✓ " + message);
    }

    /**
     * Print error message.
     */
    public static void printError(String message) {
        System.err.println("✗ " + message);
    }

    /**
     * Print warning message.
     */
    public static void printWarning(String message) {
        System.out.println("⚠️  " + message);
    }

    /**
     * Print info message.
     */
    public static void printInfo(String message) {
        System.out.println("ℹ️  " + message);
    }

    /**
     * Print section header.
     */
    public static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(title);
        System.out.println("=".repeat(60) + "\n");
    }
}
