package net.funkpla.unseaworthy.platform.services;

import net.minecraft.world.entity.Entity;

public interface IBoatSinkTimeAccessor {
    IBoatSinkTimeAccessor from(Entity e);
    void setValue(int i);
    int getValue();
}
