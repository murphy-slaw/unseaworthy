package net.funkpla.unseaworthy;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UnseaworthyCommon implements ModInitializer {

    public static final String MOD_ID = "unseaworthy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static TagKey<Biome> OCEANS = TagKey.create(Registries.BIOME, new ResourceLocation("c", "ocean"));
    public static TagKey<EntityType<?>> SINKABLE_BOATS = TagKey.create(Registries.ENTITY_TYPE, locate("sinkable"));

    public static ResourceLocation locate(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(UnseaworthyConfig.class, JanksonConfigSerializer::new);
    }
}
