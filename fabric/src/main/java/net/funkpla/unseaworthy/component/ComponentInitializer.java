package net.funkpla.unseaworthy.component;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.world.entity.vehicle.Boat;

import static net.funkpla.unseaworthy.component.SinkTimeComponent.SINK_TIME;

public final class ComponentInitializer implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(Boat.class, SINK_TIME, SinkTimeComponent::new);
    }
}
