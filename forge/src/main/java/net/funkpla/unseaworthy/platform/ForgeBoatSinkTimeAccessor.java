package net.funkpla.unseaworthy.platform;

import net.funkpla.unseaworthy.UnseaworthyPacketHandler;
import net.funkpla.unseaworthy.capability.ISinkTimeCapability;
import net.funkpla.unseaworthy.capability.SinkTimeCapability;
import net.funkpla.unseaworthy.platform.services.IBoatSinkTimeAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;


public class ForgeBoatSinkTimeAccessor implements IBoatSinkTimeAccessor {
    private LazyOptional<ISinkTimeCapability> sinkCapability;
    private Entity boat;

    public ForgeBoatSinkTimeAccessor() {
    }

    public ForgeBoatSinkTimeAccessor(ICapabilityProvider entity) {
        sinkCapability = entity.getCapability(SinkTimeCapability.INSTANCE);
    }

    public IBoatSinkTimeAccessor from(Entity entity) {
        var acc = new ForgeBoatSinkTimeAccessor(entity);
        acc.boat = entity;
        return acc;
    }

    public int getValue() {
        return sinkCapability.resolve().map(ISinkTimeCapability::getValue).orElse(0);
    }

    public void setValue(int i) {
        sinkCapability.ifPresent(cap -> cap.setMyValue(i));
        UnseaworthyPacketHandler.sendSinkTimePacket(boat, i);
    }
}
