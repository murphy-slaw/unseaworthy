package net.funkpla.unseaworthy.capability;

import net.minecraft.nbt.CompoundTag;

public class SinkTimeCapabilityImpl implements ISinkTimeCapability {

    private static final String NBT_KEY_SINK_TIME = "sinkTime";

    private int myValue = 0;

    @Override
    public int getValue() {
        return this.myValue;
    }

    @Override
    public void setMyValue(int myValue) {
        this.myValue = myValue;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_KEY_SINK_TIME, this.myValue);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.myValue = nbt.getInt(NBT_KEY_SINK_TIME);
    }
}
