package net.funkpla.unseaworthy;

import net.funkpla.unseaworthy.capability.ISinkTimeCapability;
import net.funkpla.unseaworthy.capability.SinkTimeCapabilityAttacher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class UnseaworthyModForge {

    public UnseaworthyModForge() {

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        UnseaworthyCommon.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, this::onAttachingCapabilities);
        UnseaworthyPacketHandler.registerPackets();
    }

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ISinkTimeCapability.class);
    }

    @SubscribeEvent
    public void onAttachingCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Boat))
            return;
        SinkTimeCapabilityAttacher.attach(event);
    }
}