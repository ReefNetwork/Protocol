package com.nukkitx.protocol.bedrock.packet;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketType;
import com.nukkitx.protocol.bedrock.data.HeightMapDataType;
import com.nukkitx.protocol.bedrock.data.SubChunkRequestResult;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCounted;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class SubChunkPacket extends BedrockPacket implements ReferenceCounted {
    private int dimension;
    private Vector3i subChunkPosition;
    private ByteBuf data;
    private SubChunkRequestResult result;
    private HeightMapDataType heightMapType;
    private ByteBuf heightMapData;

    @Override
    public final boolean handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SUB_CHUNK;
    }

    @Override
    public int refCnt() {
        return this.data.refCnt();
    }

    @Override
    public ReferenceCounted retain() {
        this.data.retain();
        this.heightMapData.retain();
        return this;
    }

    @Override
    public ReferenceCounted retain(int increment) {
        this.data.retain(increment);
        this.heightMapData.retain(increment);
        return this;
    }

    @Override
    public ReferenceCounted touch() {
        this.data.touch();
        this.heightMapData.touch();
        return this;
    }

    @Override
    public SubChunkPacket touch(Object o) {
        this.data.touch(o);
        this.heightMapData.touch(o);
        return this;
    }

    @Override
    public boolean release() {
        return this.data.release() && this.heightMapData.release();
    }

    @Override
    public boolean release(int decrement) {
        return this.data.release(decrement) && this.heightMapData.release(decrement);
    }
}
