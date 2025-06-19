package net.funkpla.unseaworthy.data;

import com.mojang.serialization.Codec;
import net.funkpla.unseaworthy.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SinkTimeData {
    // Create the DeferredRegister for attachment types
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    // Serialization via INBTSerializable
    public static final Supplier<AttachmentType<Integer>> SINK_TIME = ATTACHMENT_TYPES.register("sink_time",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
