package net.funkpla.unseaworthy.capability;

import net.funkpla.unseaworthy.platform.services.IBoatSinkTimeAccessor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class SinkTimeCapability {

    public static final Capability<ISinkTimeCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IBoatSinkTimeAccessor.class);
    }

    private SinkTimeCapability() {
    }
}
