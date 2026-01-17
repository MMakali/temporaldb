package com.temporaldb.cli.utils;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Table formatter for CLI output.
 */
public class TableFormatter {
    private static final Logger logger = LoggerFactory.getLogger(TableFormatter.class);

    private final List<String> headers;
    private final List<int[]> rows;
    private final int[] columnWidths;

    public TableFormatter(String... headers) {
        this.headers = Arrays.asList(headers);
        this.rows = new ArrayList<>();
        this.columnWidths = new int[headers.length];

        // Initialize column widths based on headers
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
        }
    }

    /**
     * Add row to table.
     */
    public void addRow(Object... values) {
        List<String> stringValues = new ArrayList<>();
        for (Object value : values) {
            String str = value != null ? value.toString() : "";
            stringValues.add(str);
            int idx = stringValues.size() - 1;
            if (idx < columnWidths.length) {
                columnWidths[idx] = Math.max(columnWidths[idx], str.length());
            }
        }
    }

    /**
     * Print table.
     */
    public void print() {
        // Print header
        printSeparator();
        for (int i = 0; i < headers.size(); i++) {
            System.out.printf("%-" + (columnWidths[i] + 2) + "s", headers.get(i));
        }
        System.out.println();
        printSeparator();

        // Print rows
        for (List<String> row : convertRowsToStrings()) {
            for (int i = 0; i < row.size(); i++) {
                System.out.printf("%-" + (columnWidths[i] + 2) + "s", row.get(i));
            }
            System.out.println();
        }

        printSeparator();
    }

    private void printSeparator() {
        int totalWidth = Arrays.stream(columnWidths).sum() + (columnWidths.length * 2);
        System.out.println(String.join("", Collections.nCopies(totalWidth, "-")));
    }

    private List<List<String>> convertRowsToStrings() {
        List<List<String>> result = new ArrayList<>();
        // Would convert stored rows to string lists
        return result;
    }
}

