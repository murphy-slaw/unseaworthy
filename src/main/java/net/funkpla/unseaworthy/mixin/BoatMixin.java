package net.funkpla.unseaworthy.mixin;

import net.funkpla.unseaworthy.UnseaworthyCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class BoatMixin extends Entity {

    @Unique
    private int bounceTimer = 0;
    @Unique
    private boolean isSinking = false;

    @Shadow
    private boolean isAboveBubbleColumn;


    public BoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract Boat.Status getStatus();

    @Shadow
    protected abstract void setBubbleTime(int bubbleTime);

    @Shadow
    public abstract void onAboveBubbleCol(boolean downwards);

    @Shadow
    public abstract Boat.Type getVariant();

    @Unique
    private boolean isSinking() {
        return this.isSinking;
    }

    @Unique
    private void setSinking(boolean b) {
        this.isSinking = b;
    }

    /*
    We pretend we are above a bubble column to get the jostling effect.
     */
    @Inject(method = "tickBubbleColumn", at = @At("HEAD"))
    private void fakeBubbles(CallbackInfo ci) {
        if (isSinking()) {
            isAboveBubbleColumn = true;
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tickSinking(CallbackInfo info) {
        if (this.level().isClientSide()) return;
        if (getStatus() == Boat.Status.IN_WATER) {
            if (this.shouldSink()) {
                // By pretending we are above a downward column, the boat will break when bubbleTime expires
                onAboveBubbleCol(true);
                if (!isSinking()) {
                    setSinking(true);
                    setBubbleTime(100);
                } else {
                    this.tryBounce();
                }
            } else {
                setSinking(false);
            }
        }
    }

    @Unique
    boolean shouldSink() {
        return this.level().getBiome(blockPosition()).is(UnseaworthyCommon.OCEANS);
    }

    @Unique
    private void tryBounce() {
        if (this.bounceTimer > 0) {
            this.bounceTimer--;
            return;
        }
        this.bounce();
        this.bounceTimer = 20 + this.random.nextInt(10);
    }

    @Unique
    private void bounce() {
        Vec3 vec3 = this.getDeltaMovement();
        float jitterX = (this.random.nextFloat() - 0.5F) * 0.2F;
        float jitterZ = (this.random.nextFloat() - 0.5F) * 0.2F;
        this.setDeltaMovement(vec3.x + (vec3.x * jitterX), vec3.y + (0.1 * this.random.nextInt(3, 5)), vec3.z + jitterZ);
    }

    //Only runs on server side
    @Inject(method = "tickBubbleColumn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;ejectPassengers()V"))
    private void destroyBoat(CallbackInfo ci) {
        if (isSinking()) {
            this.level().playSound(this, BlockPos.containing(this.position()), SoundEvents.PLAYER_SPLASH_HIGH_SPEED, this.getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
            this.discard();
            int spawnCount = this.random.nextInt(3, 5);
            for (int i = 0; i < spawnCount; i++) {
                this.spawnAtLocation(new ItemStack(this.getVariant().getPlanks()), 1);
                this.spawnAtLocation(new ItemStack(Items.STICK), 1);
            }
        }
    }
}
