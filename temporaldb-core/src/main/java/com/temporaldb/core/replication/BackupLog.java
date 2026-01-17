package com.temporaldb.core.replication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Logs backup events.
 */
public class BackupLog {
    private final List<BackupPoint> log = new CopyOnWriteArrayList<>();

    public void log(BackupPoint backup) {
        log.add(backup);
    }

    public List<BackupPoint> getLog() {
        return new ArrayList<>(log);
    }
}
