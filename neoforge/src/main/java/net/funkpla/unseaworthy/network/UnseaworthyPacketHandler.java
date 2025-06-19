package net.funkpla.unseaworthy.network;


import net.funkpla.unseaworthy.Sinker;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.PacketListener;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class UnseaworthyPacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(SinkTimePacket.TYPE, SinkTimePacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(UnseaworthyPacketHandler::handle, UnseaworthyPacketHandler::handle));
    }

    public static void handle(final SinkTimePacket msg, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> handleSinkTimePacket(msg, ctx));
    }

    public static void handleSinkTimePacket(final SinkTimePacket packet, final IPayloadContext ctx) {
        PacketListener listener = ctx.listener();
        Entity boat = ((ClientPacketListener) listener).getLevel().getEntity(packet.entityId());
        if (boat instanceof Sinker) {
            ((Sinker) boat).setSinkTime(packet.sinkTime());
        }
    }

    public static void sendSinkTimePacket(Entity boat, int sinkTime) {
        PacketDistributor.sendToPlayersTrackingEntity(boat, new SinkTimePacket(boat.getId(), sinkTime));
    }

}