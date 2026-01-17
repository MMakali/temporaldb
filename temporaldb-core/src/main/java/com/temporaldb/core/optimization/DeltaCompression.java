package com.temporaldb.core.optimization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Delta encoding compression.
 */
public class DeltaCompression implements CompressionStrategy {
    private static final Logger logger = LoggerFactory.getLogger(DeltaCompression.class);

    /**
     * Compress using delta encoding.
     * Stores differences between consecutive values instead of values themselves.
     */
    @Override
    public byte[] compress(Object data) {
        if (!(data instanceof long[])) {
            throw new IllegalArgumentException("Delta compression works on long arrays");
        }

        long[] values = (long[]) data;
        if (values.length == 0) return new byte[0];

        List<Long> deltas = new ArrayList<>();
        deltas.add(values[0]);  // First value as-is

        for (int i = 1; i < values.length; i++) {
            deltas.add(values[i] - values[i - 1]);
        }

        logger.debug("Delta compressed {} values", values.length);
        return serializeLongs(deltas);
    }

    @Override
    public Object decompress(byte[] data) {
        List<Long> deltas = deserializeLongs(data);
        long[] values = new long[deltas.size()];

        values[0] = deltas.get(0);
        for (int i = 1; i < deltas.size(); i++) {
            values[i] = values[i - 1] + deltas.get(i);
        }

        return values;
    }

    private byte[] serializeLongs(List<Long> longs) {
        byte[] bytes = new byte[longs.size() * 8];
        for (int i = 0; i < longs.size(); i++) {
            long l = longs.get(i);
            for (int j = 0; j < 8; j++) {
                bytes[i * 8 + j] = (byte) ((l >> (j * 8)) & 0xFF);
            }
        }
        return bytes;
    }

    private List<Long> deserializeLongs(byte[] bytes) {
        List<Long> longs = new ArrayList<>();
        for (int i = 0; i < bytes.length; i += 8) {
            long l = 0;
            for (int j = 0; j < 8; j++) {
                l |= ((long) (bytes[i + j] & 0xFF)) << (j * 8);
            }
            longs.add(l);
        }
        return longs;
    }
}
