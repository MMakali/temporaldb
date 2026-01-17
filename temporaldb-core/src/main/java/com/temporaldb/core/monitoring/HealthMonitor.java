package com.temporaldb.core.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Monitors system health.
 */
public class HealthMonitor {
    private static final Logger logger = LoggerFactory.getLogger(HealthMonitor.class);

    private final long startTime = System.currentTimeMillis();
    private volatile double cpuUsage = 0;
    private volatile double memoryUsage = 0;
    private volatile boolean isHealthy = true;
    private final List<HealthCheck> checks = new CopyOnWriteArrayList<>();

    /**
     * Add health check.
     */
    public void addHealthCheck(HealthCheck check) {
        checks.add(check);
    }

    /**
     * Perform health checks.
     */
    public SystemHealth performHealthCheck() {
        SystemHealth health = new SystemHealth();

        health.setUptime(System.currentTimeMillis() - startTime);
        health.setCpuUsage(cpuUsage);
        health.setMemoryUsage(memoryUsage);

        for (HealthCheck check : checks) {
            if (!check.isHealthy()) {
                isHealthy = false;
                health.addIssue(check.getName(), check.getIssue());
            }
        }

        health.setHealthy(isHealthy);

        if (isHealthy) {
            logger.debug("System health: OK");
        } else {
            logger.warn("System health: ISSUES DETECTED");
        }

        return health;
    }

    public void updateCpuUsage(double cpu) {
        this.cpuUsage = cpu;
    }

    public void updateMemoryUsage(double memory) {
        this.memoryUsage = memory;
    }

    /**
     * Health check interface.
     */
    public interface HealthCheck {
        String getName();

        boolean isHealthy();

        String getIssue();
    }
}
