package net.funkpla.unseaworthy.mixin;

import net.funkpla.unseaworthy.UnseaworthyCommon;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class BoatMixin extends Entity {

    public BoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract Boat.Status getStatus();

    @Shadow
    private boolean isAboveBubbleColumn;


    @Shadow
    protected abstract void setBubbleTime(int bubbleTime);

    @Shadow
    private boolean bubbleColumnDirectionIsDown;
    @Unique
    private boolean isSinking = false;


    @Inject(method = "tickBubbleColumn", at = @At("HEAD"))
    private void murder(CallbackInfo ci) {
        if (isSinking) {
            isAboveBubbleColumn = true;
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void init(CallbackInfo info) {
        if (getStatus() == Boat.Status.IN_WATER) {
            if (!this.level().isClientSide()) {
                Holder<Biome> biome;
                biome = this.level().getBiome(blockPosition());
                if (biome.is(UnseaworthyCommon.OCEANS)) {
                    if (!isSinking) {
                        isSinking = true;
                        bubbleColumnDirectionIsDown = true;
                        setBubbleTime(120);
                    }
                } else {
                    isSinking = false;
                }
            }
        }
    }

    @Inject(method = "tickBubbleColumn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;ejectPassengers()V"))
    private void destroyBoat(CallbackInfo ci) {
        if (isSinking) {
            this.discard();
            this.spawnAtLocation(new ItemStack(Items.STICK, 5));
        }
    }
}
