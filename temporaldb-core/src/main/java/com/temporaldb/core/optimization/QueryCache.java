package com.temporaldb.core.optimization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Query result cache with LRU eviction.
 */
public class QueryCache {
    private static final Logger logger = LoggerFactory.getLogger(QueryCache.class);

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final LinkedBlockingQueue<String> lruQueue = new LinkedBlockingQueue<>();
    private final int maxSize;
    private long hitCount = 0;
    private long missCount = 0;

    public QueryCache(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Put query result in cache.
     */
    public void put(String queryHash, List<Map<String, Object>> result) {
        if (cache.size() >= maxSize) {
            evictLRU();
        }

        CacheEntry entry = new CacheEntry(result, System.currentTimeMillis());
        cache.put(queryHash, entry);

        try {
            lruQueue.offer(queryHash);
        } catch (Exception e) {
            logger.warn("Error adding to LRU queue", e);
        }

        logger.debug("Cached query: {} (cache size: {})", queryHash, cache.size());
    }

    /**
     * Get query result from cache.
     */
    public List<Map<String, Object>> get(String queryHash) {
        CacheEntry entry = cache.get(queryHash);

        if (entry != null) {
            if (isExpired(entry)) {
                cache.remove(queryHash);
                missCount++;
                return null;
            }

            entry.incrementAccessCount();
            hitCount++;
            logger.debug("Cache hit for query: {}", queryHash);
            return entry.getResult();
        }

        missCount++;
        return null;
    }

    /**
     * Evict least recently used entry.
     */
    private void evictLRU() {
        try {
            String lruKey = lruQueue.poll();
            if (lruKey != null) {
                cache.remove(lruKey);
                logger.debug("Evicted cache entry: {}", lruKey);
            }
        } catch (Exception e) {
            logger.error("Error evicting LRU entry", e);
        }
    }

    private boolean isExpired(CacheEntry entry) {
        // Cache entries expire after 1 hour
        long expirationTime = 60 * 60 * 1000;
        return System.currentTimeMillis() - entry.getCreatedAt() > expirationTime;
    }

    public double getHitRatio() {
        long total = hitCount + missCount;
        return total > 0 ? (double) hitCount / total : 0;
    }

    public int getSize() {
        return cache.size();
    }

    public long getHitCount() {
        return hitCount;
    }

    public long getMissCount() {
        return missCount;
    }

    /**
     * Cache entry holder.
     */
    private static class CacheEntry {
        private final List<Map<String, Object>> result;
        private final long createdAt;
        private long accessCount = 0;

        CacheEntry(List<Map<String, Object>> result, long createdAt) {
            this.result = new ArrayList<>(result);
            this.createdAt = createdAt;
        }

        List<Map<String, Object>> getResult() {
            return result;
        }

        long getCreatedAt() {
            return createdAt;
        }

        void incrementAccessCount() {
            accessCount++;
        }

        long getAccessCount() {
            return accessCount;
        }
    }
}
