package com.temporaldb.core.optimization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Run-length encoding compression.
 */
public class RunLengthCompression implements CompressionStrategy {
    private static final Logger logger = LoggerFactory.getLogger(RunLengthCompression.class);

    /**
     * Compress using RLE.
     */
    @Override
    public byte[] compress(Object data) {
        if (!(data instanceof byte[])) {
            throw new IllegalArgumentException("RLE works on byte arrays");
        }

        byte[] input = (byte[]) data;
        List<Byte> compressed = new ArrayList<>();

        if (input.length == 0) return new byte[0];

        int i = 0;
        while (i < input.length) {
            byte current = input[i];
            int count = 1;

            while (i + count < input.length && input[i + count] == current && count < 255) {
                count++;
            }

            compressed.add(current);
            compressed.add((byte) count);
            i += count;
        }

        byte[] result = new byte[compressed.size()];
        for (int j = 0; j < compressed.size(); j++) {
            result[j] = compressed.get(j);
        }

        logger.debug("RLE compressed {} bytes to {} bytes", input.length, result.length);
        return result;
    }

    @Override
    public Object decompress(byte[] data) {
        List<Byte> decompressed = new ArrayList<>();

        for (int i = 0; i < data.length; i += 2) {
            byte value = data[i];
            byte count = data[i + 1];
            for (int j = 0; j < count; j++) {
                decompressed.add(value);
            }
        }

        byte[] result = new byte[decompressed.size()];
        for (int i = 0; i < decompressed.size(); i++) {
            result[i] = decompressed.get(i);
        }

        return result;
    }
}
