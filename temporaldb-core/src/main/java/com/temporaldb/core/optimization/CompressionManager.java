package com.temporaldb.core.optimization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages compression of data.
 */
public class CompressionManager {
    private static final Logger logger = LoggerFactory.getLogger(CompressionManager.class);

    private final Map<String, CompressionStrategy> strategies = new ConcurrentHashMap<>();

    public CompressionManager() {
        // Register compression strategies
        strategies.put("DELTA", new DeltaCompression());
        strategies.put("DICTIONARY", new DictionaryCompression());
        strategies.put("RLE", new RunLengthCompression());
    }

    /**
     * Compress data with specified strategy.
     */
    public byte[] compress(Object data, String strategy) {
        CompressionStrategy comp = strategies.get(strategy);
        if (comp == null) {
            throw new IllegalArgumentException("Unknown compression strategy: " + strategy);
        }
        return comp.compress(data);
    }

    /**
     * Decompress data.
     */
    public Object decompress(byte[] data, String strategy) {
        CompressionStrategy comp = strategies.get(strategy);
        if (comp == null) {
            throw new IllegalArgumentException("Unknown compression strategy: " + strategy);
        }
        return comp.decompress(data);
    }

    /**
     * Calculate compression ratio.
     */
    public double getCompressionRatio(byte[] original, byte[] compressed) {
        if (original.length == 0) return 1.0;
        return (double) compressed.length / original.length;
    }
}
