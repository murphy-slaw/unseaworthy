package net.funkpla.unseaworthy.network;

import io.netty.buffer.ByteBuf;
import net.funkpla.unseaworthy.UnseaworthyCommon;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SinkTimePacket(int entityId, int sinkTime) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SinkTimePacket> TYPE =
            new CustomPacketPayload.Type<>(UnseaworthyCommon.locate("sink_time"));
    public static final StreamCodec<ByteBuf, SinkTimePacket> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, SinkTimePacket::entityId, ByteBufCodecs.VAR_INT,
                    SinkTimePacket::sinkTime, SinkTimePacket::new);

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
