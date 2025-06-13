package net.funkpla.unseaworthy;


import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class UnseaworthyPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(UnseaworthyCommon.locate("main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    public static int messageId;

    public static void registerPackets() {
        INSTANCE.registerMessage(messageId++, SinkTimePacket.class, SinkTimePacket::encoder, SinkTimePacket::new,
                SinkTimePacket::handle);
    }

    public static void handle(SinkTimePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleSinkTimePacket(msg, ctx)));
        ctx.get().setPacketHandled(true);
    }

    public static void handleSinkTimePacket(SinkTimePacket packet, Supplier<NetworkEvent.Context> ctx) {
        PacketListener listener = ctx.get().getNetworkManager().getPacketListener();
        if (listener instanceof ClientPacketListener) {
            Entity boat = ((ClientPacketListener) listener).getLevel().getEntity(packet.entityId);
            if (boat instanceof Sinker) {
                ((Sinker) boat).setSinkTime(packet.sinkTime);
            }
        }
    }

    public static void sendSinkTimePacket(Entity boat, int sinkTime) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> boat), new SinkTimePacket(boat.getId(), sinkTime));
    }

    public static class SinkTimePacket {
        public final int entityId;
        public final int sinkTime;

        public SinkTimePacket(int entityId, int sinkTime) {
            this.entityId = entityId;
            this.sinkTime = sinkTime;
        }

        public SinkTimePacket(FriendlyByteBuf buffer) {
            entityId = buffer.readInt();
            sinkTime = buffer.readInt();
        }

        public void encoder(FriendlyByteBuf buffer) {
            buffer.writeInt(entityId);
            buffer.writeInt(sinkTime);
        }

        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> UnseaworthyPacketHandler.handle(this, ctx)));
            ctx.get().setPacketHandled(true);

        }
    }
}