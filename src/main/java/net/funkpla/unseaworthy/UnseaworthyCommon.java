package net.funkpla.unseaworthy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UnseaworthyCommon implements ModInitializer {

    public static final String MOD_ID = "unseaworthy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static TagKey<Biome> OCEANS = TagKey.create(Registries.BIOME, new ResourceLocation("c", "ocean"));

    @Override
    public void onInitialize() {
    }
}