package net.funkpla.unseaworthy.component;

import net.minecraft.world.entity.vehicle.Boat;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

import static net.funkpla.unseaworthy.component.SinkTimeComponent.SINK_TIME;

public final class ComponentInitializer implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(Boat.class, SINK_TIME, SinkTimeComponent::new);
    }
}
