package net.funkpla.unseaworthy.platform;

import net.funkpla.unseaworthy.component.SinkTimeComponent;
import net.funkpla.unseaworthy.platform.services.IBoatSinkTimeAccessor;
import net.minecraft.world.entity.Entity;

import static net.funkpla.unseaworthy.component.SinkTimeComponent.SINK_TIME;

public class FabricBoatSinkTimeAccessor implements IBoatSinkTimeAccessor {
    private SinkTimeComponent sinkTime;

    public FabricBoatSinkTimeAccessor() {
    }

    public FabricBoatSinkTimeAccessor(Object entity) {
        sinkTime = SINK_TIME.get(entity);
    }

    public IBoatSinkTimeAccessor from(Entity entity) {
        return new FabricBoatSinkTimeAccessor(entity);
    }

    public int getValue() {
        return sinkTime.getValue();
    }

    public void setValue(int i) {
        sinkTime.setValue(i);
    }
}
