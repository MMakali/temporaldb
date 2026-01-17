package com.temporaldb.core.replication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Records recovery events.
 */
public class RecoveryLog {
    private final List<RecoveryEvent> events = new CopyOnWriteArrayList<>();

    public void log(RecoveryEvent event) {
        events.add(event);
    }

    public List<RecoveryEvent> getEvents() {
        return new ArrayList<>(events);
    }
}
