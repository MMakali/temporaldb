package com.temporaldb.core.monitoring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * System health report.
 */
public class SystemHealth {
    private long uptime;
    private double cpuUsage;
    private double memoryUsage;
    private boolean healthy;
    private final Map<String, String> issues = new ConcurrentHashMap<>();

    public void addIssue(String component, String issue) {
        issues.put(component, issue);
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public Map<String, String> getIssues() {
        return new HashMap<>(issues);
    }

    @Override
    public String toString() {
        return String.format(
                "SystemHealth{uptime=%dms, cpu=%.2f%%, memory=%.2f%%, healthy=%s, issues=%d}",
                uptime, cpuUsage, memoryUsage, healthy, issues.size()
        );
    }
}
