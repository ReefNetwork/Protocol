package com.nukkitx.protocol.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import me.steinborn.libdeflate.CompressionType;
import me.steinborn.libdeflate.Libdeflate;
import me.steinborn.libdeflate.LibdeflateCompressor;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DataFormatException;

public class ZlibNative implements ZlibProvider {

    private static final Map<Integer, ThreadLocal<LibdeflateCompressor>> localCompressors = new ConcurrentHashMap<>();

    static {
        // Deflater.DEFAULT_COMPRESSION = -1
        // Deflater.BEST_COMPRESSION = 9
        for (int level = -1; level <= 9; level++) {
            int finalLevel = level;
            localCompressors.put(level, ThreadLocal.withInitial(() -> new LibdeflateCompressor(finalLevel)));
        }
    }

    public static boolean isSupported() {
        return Libdeflate.isAvailable();
    }

    public static ZlibNative defaultNative() {
        return new ZlibNative(false, Zlib.DEFAULT_JAVA);
    }

    public static ZlibNative rawNative() {
        return new ZlibNative(true, Zlib.RAW_JAVA);
    }

    private final ZlibProvider inflaterProvider;
    private final CompressionType type;

    protected ZlibNative(boolean raw, ZlibProvider javaParent) {
        this.type = raw ? CompressionType.DEFLATE : CompressionType.ZLIB;
        this.inflaterProvider = javaParent;
    }

    @Override
    public ByteBuf inflate(ByteBuf buffer, int maxSize) throws DataFormatException {
        // Decompression is not the bright side of libdeflate
        return this.inflaterProvider.inflate(buffer, maxSize);
    }

    @Override
    public void deflate(ByteBuf uncompressed, ByteBuf compressed, int level) throws DataFormatException {
        ByteBuf destination = null;
        ByteBuf source = null;
        try {
            if (!uncompressed.isDirect()) {
                // Source is not a direct buffer. Work on a temporary direct buffer and then write the contents out.
                source = ByteBufAllocator.DEFAULT.ioBuffer();
                source.writeBytes(uncompressed);
            } else {
                source = uncompressed;
            }

            if (!compressed.isDirect()) {
                // Destination is not a direct buffer. Work on a temporary direct buffer and then write the contents out.
                destination = ByteBufAllocator.DEFAULT.ioBuffer();
            } else {
                destination = compressed;
            }

            // Implementation of libdeflate doesnt like same size as input size
            int chunkSize = source.readableBytes() + Zlib.CHUNK;
            ByteBuffer input = source.internalNioBuffer(source.readerIndex(), source.readableBytes());
            LibdeflateCompressor deflater = localCompressors.get(level).get();

            while (input.remaining() > 0) {
                int index = destination.writerIndex();
                destination.ensureWritable(chunkSize);
                int written = deflater.compress(input, destination.internalNioBuffer(index, chunkSize), this.type);
                destination.writerIndex(index + written);
            }

            if (destination != compressed) {
                compressed.writeBytes(destination);
            }
        } finally {
            if (source != null && source != uncompressed) {
                source.release();
            }
            if (destination != null && destination != compressed) {
                destination.release();
            }
        }
    }
}
