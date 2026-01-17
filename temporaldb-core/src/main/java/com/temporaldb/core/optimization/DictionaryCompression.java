package com.temporaldb.core.optimization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dictionary encoding compression.
 */
public class DictionaryCompression implements CompressionStrategy {
    private static final Logger logger = LoggerFactory.getLogger(DictionaryCompression.class);

    /**
     * Compress using dictionary encoding.
     */
    @Override
    public byte[] compress(Object data) {
        if (!(data instanceof String[])) {
            throw new IllegalArgumentException("Dictionary compression works on string arrays");
        }

        String[] strings = (String[]) data;
        Map<String, Integer> dictionary = new HashMap<>();
        List<Integer> indexes = new ArrayList<>();

        int dictIndex = 0;
        for (String s : strings) {
            if (!dictionary.containsKey(s)) {
                dictionary.put(s, dictIndex++);
            }
            indexes.add(dictionary.get(s));
        }

        logger.debug("Dictionary compressed {} strings to {} unique entries",
                strings.length, dictionary.size());

        return serializeIndexes(indexes);
    }

    @Override
    public Object decompress(byte[] data) {
        // Would need to store dictionary separately
        throw new UnsupportedOperationException("Deserialization requires dictionary metadata");
    }

    private byte[] serializeIndexes(List<Integer> indexes) {
        byte[] bytes = new byte[indexes.size() * 4];
        for (int i = 0; i < indexes.size(); i++) {
            int idx = indexes.get(i);
            for (int j = 0; j < 4; j++) {
                bytes[i * 4 + j] = (byte) ((idx >> (j * 8)) & 0xFF);
            }
        }
        return bytes;
    }
}
