package net.funkpla.unseaworthy.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.unseaworthy.Constants;
import net.funkpla.unseaworthy.Sinker;
import net.funkpla.unseaworthy.UnseaworthyConfig;
import net.funkpla.unseaworthy.platform.Services;
import net.funkpla.unseaworthy.platform.services.IBoatSinkTimeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Boat.class)
public abstract class BoatMixin extends Entity implements Sinker {

    @Unique
    protected final UnseaworthyConfig unseaworthy$config =
            AutoConfig.getConfigHolder(UnseaworthyConfig.class).getConfig();
    @Unique
    private final IBoatSinkTimeAccessor unseaworthy$sinkTime = Services.SINK_TIME_ACCESSOR.from(this);
    @Unique
    private int unseaworthy$bounceTimer = 0;

    @Unique
    private boolean unseaworthy$isSinking = false;

    @Unique
    private float unseaworthy$sinkMultiplier;

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
    private int unseaworthy$getSinkTime() {
        return this.unseaworthy$sinkTime.getValue();
    }

    @Unique
    public void setSinkTime(int ticks) {
        if (unseaworthy$sinkTime.getValue() != ticks)
            this.unseaworthy$sinkTime.setValue(ticks);
    }

    @Unique
    private boolean unseaworthy$isSinking() {
        return unseaworthy$isSinking;
    }

    @Unique
    private void unseaworthy$setSinking(boolean b) {
        this.unseaworthy$isSinking = b;
    }


    @Inject(at = @At("HEAD"), method = "tickBubbleColumn", cancellable = true)
    private void cancelBubbleColumn(CallbackInfo ci) {
        if (this.unseaworthy$isSinking()) {
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;tickBubbleColumn()V"),
            method = "tick")
    private void tickSinking(CallbackInfo info) {
        if (!getType().is(Constants.SINKABLE_BOATS))
            return;
        int i = this.unseaworthy$getSinkTime();
        if (this.level().isClientSide()) {
            if (i >= 0) {
                this.unseaworthy$sinkMultiplier += 0.01F;
                if (this.random.nextInt(15) == 0)
                    this.doWaterSplashEffect();
            } else {
                this.unseaworthy$sinkMultiplier -= 0.1F;
            }
            this.unseaworthy$sinkMultiplier = Mth.clamp(this.unseaworthy$sinkMultiplier, 0.0F, 1.0F);
            this.bubbleAngleO = this.bubbleAngle;
            this.bubbleAngle =
                    15.0F * (float) Math.sin((0.5F * (float) this.level().getGameTime())) * this.unseaworthy$sinkMultiplier;

        } else if (this.shouldSink() && getStatus() != Boat.Status.UNDER_WATER) {
            if (!unseaworthy$isSinking()) {
                unseaworthy$setSinking(true);
                setSinkTime(unseaworthy$config.interval);
            } else if (this.unseaworthy$getSinkTime() <= 0) {
                if (this.random.nextInt(100) > unseaworthy$config.breakChance) {
                    this.setSinkTime(unseaworthy$config.interval);
                } else {
                    this.unseaworthy$sink();
                }
            } else {
                this.unseaworthy$tryBounce();
                this.setSinkTime(--i);
            }
        } else {
            unseaworthy$setSinking(false);
            setSinkTime(-1);
        }
    }

    @Unique
    private float unseaworthy$getWaterLevelBelow() {
        AABB aabb = this.getBoundingBox();
        int minX = Mth.floor(aabb.minX);
        int maxX = Mth.ceil(aabb.maxX);
        int minY = Mth.floor(aabb.minY);
        int minZ = Mth.floor(aabb.minZ);
        int maxZ = Mth.ceil(aabb.maxZ);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int curY = minY;
        depthLoop:
        while (true) {
            for (int curX = minX; curX < maxX; ++curX) {
                for (int curZ = minZ; curZ < maxZ; ++curZ) {
                    mutableBlockPos.set(curX, curY, curZ);
                    FluidState fluidState = this.level().getFluidState(mutableBlockPos);
                    if (!fluidState.is(FluidTags.WATER)) {
                        break depthLoop;
                    }
                }
            }
            curY--;
        }
        return (float) minY - curY;
    }

    @Unique
    boolean shouldSink() {
        return this.unseaworthy$getWaterLevelBelow() >= unseaworthy$config.minDepth && this.level().getBiome(blockPosition()).is(Constants.SINKS_BOATS) && unseaworthy$weatherBadEnough();
    }

    @Unique
    boolean unseaworthy$weatherBadEnough() {
        switch (unseaworthy$config.weatherRequired) {
            case CLEAR -> {
                return true;
            }
            case RAINING -> {
                return this.level().isRaining();
            }
            case THUNDERING -> {
                return this.level().isThundering();
            }
        }
        return false;
    }

    @Unique
    private void unseaworthy$tryBounce() {
        if (this.unseaworthy$bounceTimer > 0) {
            this.unseaworthy$bounceTimer--;
            return;
        }
        this.unseaworthy$bounce();
        this.doWaterSplashEffect();
        this.unseaworthy$bounceTimer = 20 + this.random.nextInt(10);
    }

    @Unique
    private void unseaworthy$bounce() {
        Vec3 vec3 = this.getDeltaMovement();
        float jitterX = (this.random.nextFloat() - 0.5F) * 0.2F;
        float jitterZ = (this.random.nextFloat() - 0.5F) * 0.2F;
        this.setDeltaMovement(vec3.x + (vec3.x * jitterX), vec3.y + (0.1 * this.random.nextInt(3, 5)),
                vec3.z + jitterZ);
        this.setYRot(this.getYRot() + ((this.random.nextFloat() - 0.5F) * 90));
    }

    @Unique
    private void unseaworthy$sink() {
        if (unseaworthy$isSinking()) {
            this.level().playSound(this, BlockPos.containing(this.position()), SoundEvents.PLAYER_SPLASH_HIGH_SPEED,
                    this.getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
            if (unseaworthy$config.fate == UnseaworthyConfig.BoatFate.DESTROY) {
                this.kill();
                int spawnCount = this.random.nextInt(3, 5);
                // TODO: use proper loot tables here
                for (int i = 0; i < spawnCount; i++) {
                    this.spawnAtLocation(new ItemStack(this.getVariant().getPlanks()), 1);
                    this.spawnAtLocation(new ItemStack(Items.STICK), 1);
                }
            } else if (unseaworthy$config.fate == UnseaworthyConfig.BoatFate.BREAK) {
                this.kill();
                this.destroy(new DamageSources(this.level().registryAccess()).drown());
            } else if (unseaworthy$config.fate == UnseaworthyConfig.BoatFate.SINK) {
                this.ejectPassengers();
                Vec3 vec3 = getDeltaMovement();
                this.setDeltaMovement(vec3.x, -2, vec3.z);
            }
        }
    }
}
