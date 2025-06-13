package net.funkpla.unseaworthy;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.resources.ResourceLocation;

public class UnseaworthyCommon {

    public static void init() {
        AutoConfig.register(UnseaworthyConfig.class, JanksonConfigSerializer::new);
    }

    public static ResourceLocation locate(String path) {
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}