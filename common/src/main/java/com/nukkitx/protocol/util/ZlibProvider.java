package com.nukkitx.protocol.util;

import io.netty.buffer.ByteBuf;

import java.util.zip.DataFormatException;

public interface ZlibProvider {

    ByteBuf inflate(ByteBuf buffer, int maxSize) throws DataFormatException;
    void deflate(ByteBuf uncompressed, ByteBuf compressed, int level) throws DataFormatException;

}
