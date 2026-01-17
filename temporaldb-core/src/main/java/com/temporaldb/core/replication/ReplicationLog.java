package com.temporaldb.core.replication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Logs replication events.
 */
public class ReplicationLog {
    private static final Logger logger = LoggerFactory.getLogger(ReplicationLog.class);

    private final List<ReplicationEvent> events = new CopyOnWriteArrayList<>();

    public void log(ReplicationEvent event) {
        events.add(event);
        logger.debug("Logged replication event: {}", event.getOperation());
    }

    public List<ReplicationEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public List<ReplicationEvent> getEventsSince(long timestamp) {
        return events.stream()
                .filter(e -> e.getTimestamp() >= timestamp)
                .toList();
    }
}
