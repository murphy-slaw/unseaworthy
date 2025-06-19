package net.funkpla.unseaworthy.platform;

import net.funkpla.unseaworthy.network.UnseaworthyPacketHandler;
import net.funkpla.unseaworthy.data.SinkTimeData;
import net.funkpla.unseaworthy.platform.services.IBoatSinkTimeAccessor;
import net.minecraft.world.entity.Entity;


public class NeoforgeBoatSinkTimeAccessor implements IBoatSinkTimeAccessor {
    private Entity boat;

    public NeoforgeBoatSinkTimeAccessor() {
    }

    public NeoforgeBoatSinkTimeAccessor(Entity entity) {
        boat = entity;
    }

    public IBoatSinkTimeAccessor from(Entity entity) {
        var acc = new NeoforgeBoatSinkTimeAccessor(entity);
        acc.boat = entity;
        return acc;
    }

    public int getValue() {
        return boat.getData(SinkTimeData.SINK_TIME);
    }

    public void setValue(int i) {
        boat.setData(SinkTimeData.SINK_TIME,i);
        UnseaworthyPacketHandler.sendSinkTimePacket(boat, i);
    }
}
