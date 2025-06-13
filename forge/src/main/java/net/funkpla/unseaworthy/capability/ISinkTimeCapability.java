package net.funkpla.unseaworthy.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISinkTimeCapability extends INBTSerializable<CompoundTag> {

    int getValue();

    void setMyValue(int myValue);
}
