package com.temporaldb.cli.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Format utilities.
 */
public class Formatter {
    private static final Logger logger = LoggerFactory.getLogger(Formatter.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Format bytes as human readable.
     */
    public static String formatBytes(long bytes) {
        if (bytes <= 0) return "0 B";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));

        return String.format("%.1f %s",
                bytes / Math.pow(1024, digitGroups),
                units[digitGroups]);
    }

    /**
     * Format milliseconds as human readable duration.
     */
    public static String formatDuration(long milliseconds) {
        if (milliseconds < 1000) {
            return milliseconds + "ms";
        } else if (milliseconds < 60000) {
            return (milliseconds / 1000) + "s";
        } else if (milliseconds < 3600000) {
            return (milliseconds / 60000) + "m";
        } else {
            return (milliseconds / 3600000) + "h";
        }
    }

    /**
     * Format timestamp as human readable date.
     */
    public static String formatDate(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

    /**
     * Format percentage.
     */
    public static String formatPercent(double percent) {
        return String.format("%.2f%%", percent * 100);
    }

    /**
     * Format number with thousands separator.
     */
    public static String formatNumber(long number) {
        return String.format("%,d", number);
    }

    /**
     * Center text in width.
     */
    public static String center(String text, int width) {
        int padding = (width - text.length()) / 2;
        return String.format("%" + (padding + text.length()) + "s", text)
                + String.format("%-" + (width - padding - text.length()) + "s", "");
    }
}
