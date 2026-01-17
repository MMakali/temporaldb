package com.temporaldb.core.optimization;

/**
 * Compression strategy interface.
 */
interface CompressionStrategy {
    byte[] compress(Object data);
    Object decompress(byte[] data);
}

