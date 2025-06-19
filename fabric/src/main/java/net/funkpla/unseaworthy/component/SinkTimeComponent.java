package net.funkpla.unseaworthy.component;

import net.funkpla.unseaworthy.UnseaworthyCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;


public class SinkTimeComponent implements Component, AutoSyncedComponent {
    public static final ComponentKey<SinkTimeComponent> SINK_TIME =
            ComponentRegistry.getOrCreate(UnseaworthyCommon.locate("sink_time"), SinkTimeComponent.class);
    private final Entity provider;
    private int value;

    public SinkTimeComponent(Entity e) {
        this.provider = e;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int i) {
        if (this.value != i) {
            this.value = i;
            SINK_TIME.sync(this.provider);
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        this.value = tag.getInt("value");
    }

    @Override
    public void writeToNbt(CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        tag.putInt("value", this.value);
    }
}