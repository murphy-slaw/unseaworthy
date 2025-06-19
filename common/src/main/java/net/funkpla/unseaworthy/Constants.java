package net.funkpla.unseaworthy;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "unseaworthy";
    public static final String MOD_NAME = "Unseaworthy";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final TagKey<Biome> SINKS_BOATS = TagKey.create(Registries.BIOME, UnseaworthyCommon.locate(
			"sinks_boats"));
    public static final TagKey<EntityType<?>> SINKABLE_BOATS = TagKey.create(Registries.ENTITY_TYPE,
			UnseaworthyCommon.locate("sinkable"));
}