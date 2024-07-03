package net.funkpla.unseaworthy.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.unseaworthy.UnseaworthyCommon;
import net.funkpla.unseaworthy.UnseaworthyConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
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
    private static final EntityDataAccessor<Integer> DATA_ID_SINK_TIME = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineSinkTime(CallbackInfo ci) {
        this.entityData.define(DATA_ID_SINK_TIME, 0);
    }

    @Unique
    protected final UnseaworthyConfig config = AutoConfig.getConfigHolder(UnseaworthyConfig.class).getConfig();

    @Unique
    private int bounceTimer = 0;

    @Unique
    private boolean isSinking = false;

    @Unique
    private float sinkMultiplier;

    @Shadow
    private float bubbleAngle;

    @Shadow
    private float bubbleAngleO;

    public BoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract Boat.Status getStatus();

    @Shadow
    public abstract Boat.Type getVariant();

    @Shadow
    protected abstract void destroy(DamageSource damageSource);

    @Unique
    private int getSinkTime() {
        return this.entityData.get(DATA_ID_SINK_TIME);
    }

    @Unique
    private void setSinkTime(int ticks) {
        this.entityData.set(DATA_ID_SINK_TIME, ticks);
    }

    @Unique
    private boolean isSinking() {
        return isSinking;
    }

    @Unique
    private void setSinking(boolean b) {
        this.isSinking = b;
    }


    @Inject(at = @At("HEAD"), method = "tickBubbleColumn", cancellable = true)
    private void cancelBubbleColumn(CallbackInfo ci) {
        if (this.isSinking()) {
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;tickBubbleColumn()V"), method = "tick")
    private void tickSinking(CallbackInfo info) {
        int i = this.getSinkTime();
        if (this.level().isClientSide()) {
            if (i >= 0) {
                this.sinkMultiplier += 0.01F;
                if (this.random.nextInt(15) == 0) this.doWaterSplashEffect();
            } else {
                this.sinkMultiplier -= 0.1F;
            }
            this.sinkMultiplier = Mth.clamp(this.sinkMultiplier, 0.0F, 1.0F);
            this.bubbleAngleO = this.bubbleAngle;
            this.bubbleAngle = 15.0F * (float) Math.sin((double) (0.5F * (float) this.level().getGameTime())) * this.sinkMultiplier;

        } else if (this.shouldSink() && getStatus() != Boat.Status.UNDER_WATER) {
            if (!isSinking()) {
                setSinking(true);
                setSinkTime(config.interval);
            } else if (this.getSinkTime() <= 0) {
                if (this.random.nextInt(100) > config.breakChance) {
                    this.setSinkTime(config.interval);
                } else {
                    this.sink();
                }
            } else {
                this.tryBounce();
                this.setSinkTime(--i);
            }
        } else {
            setSinking(false);
            setSinkTime(-1);
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
        this.doWaterSplashEffect();
        this.bounceTimer = 20 + this.random.nextInt(10);
    }

    @Unique
    private void bounce() {
        Vec3 vec3 = this.getDeltaMovement();
        float jitterX = (this.random.nextFloat() - 0.5F) * 0.2F;
        float jitterZ = (this.random.nextFloat() - 0.5F) * 0.2F;
        this.setDeltaMovement(vec3.x + (vec3.x * jitterX), vec3.y + (0.1 * this.random.nextInt(3, 5)), vec3.z + jitterZ);
        this.setYRot(this.getYRot() + ((this.random.nextFloat() - 0.5F) * 90));
    }

    @Unique
    private void sink() {
        if (isSinking()) {
            this.level().playSound(this, BlockPos.containing(this.position()), SoundEvents.PLAYER_SPLASH_HIGH_SPEED, this.getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
            if (config.fate == UnseaworthyConfig.BoatFate.DESTROY) {
                this.kill();
                int spawnCount = this.random.nextInt(3, 5);
                for (int i = 0; i < spawnCount; i++) {
                    this.spawnAtLocation(new ItemStack(this.getVariant().getPlanks()), 1);
                    this.spawnAtLocation(new ItemStack(Items.STICK), 1);
                }
            } else if (config.fate == UnseaworthyConfig.BoatFate.BREAK) {
                this.kill();
                this.destroy(new DamageSources(this.level().registryAccess()).drown());
            } else if (config.fate == UnseaworthyConfig.BoatFate.SINK) {
                this.ejectPassengers();
                Vec3 vec3 = getDeltaMovement();
                this.setDeltaMovement(vec3.x, -2, vec3.z);
            }
        }
    }
}
