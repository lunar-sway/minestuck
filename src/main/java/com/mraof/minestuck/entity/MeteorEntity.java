package com.mraof.minestuck.entity;

import com.mraof.minestuck.entry.meteor.MeteorManager;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.intellij.lang.annotations.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * The large meteor entity that approaches the player's home.
 * Synced to clients via MeteorPackets
 * Rendering is handled by MeteorRenderer (GeckoLib).
 */
public class MeteorEntity extends Entity implements GeoAnimatable
{
	private static final EntityDataAccessor<Float> METEOR_SIZE = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.FLOAT);
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private static final EntityDataAccessor<Float> RENDER_YAW = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> RENDER_PITCH = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<BlockPos> TARGET_POS = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Boolean> IN_DASH_PHASE = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> TICKS_ELAPSED = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> FADING = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> FADE_TICKS = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.INT);
	public static final int FADE_DURATION_TICKS = 60;
	private double fadeFallSpeed = 0;
	public static final double HEIGHT_ABOVE_TARGET = 1000.0;
	
	private BlockPos targetPos = BlockPos.ZERO;
	private PlayerIdentifier owner;
	
	public MeteorEntity(EntityType<?> type, Level level)
	{
		super(type, level);
		this.noPhysics = true;
		this.setNoGravity(true);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
		builder.define(METEOR_SIZE, 1.0f);
		builder.define(RENDER_YAW, 0.0f);
		builder.define(RENDER_PITCH, 0.0f);
		builder.define(TARGET_POS, BlockPos.ZERO);
		builder.define(IN_DASH_PHASE, false);
		builder.define(TICKS_ELAPSED, 0);
		builder.define(FADING, false);
		builder.define(FADE_TICKS, 0);
	}
	
	public void setRenderAngles(float yaw, float pitch)
	{
		this.entityData.set(RENDER_YAW, yaw);
		this.entityData.set(RENDER_PITCH, pitch);
		this.setYRot(yaw);
		this.yRotO = yaw;
	}
	
	public float getRenderYaw()
	{
		return this.entityData.get(RENDER_YAW);
	}
	
	public float getRenderPitch()
	{
		return this.entityData.get(RENDER_PITCH);
	}
	
	
	@Override
	public void tick()
	{
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
		
		if(!level().isClientSide)
		{
			updateMovement();
			if(isFading())
				tickFade();
		}
		
		super.tick();
		
		if(level().isClientSide)
		{
			this.setYRot(this.getRenderYaw());
			this.setXRot(this.getRenderPitch());
		}
	}
	
	
	public void moveTick(int ticksElapsed)
	{
		if(targetPos == null) return;
		
		boolean dashPhase = ticksElapsed >= MeteorManager.DASH_PHASE_TICKS;
		if(!level().isClientSide)
		{
			this.entityData.set(IN_DASH_PHASE, dashPhase);
			this.entityData.set(TICKS_ELAPSED, ticksElapsed);
		}
		
		Vec3 target = Vec3.atCenterOf(targetPos);
		double startHeightY = getSpawnHeightY(targetPos);
		double totalDistance = startHeightY - target.y;
		
		float progress = getUnifiedProgress(ticksElapsed);
		double newY = startHeightY - totalDistance * progress;
		
		setPos(target.x, newY, target.z);
		applyRotation();
	}
	
	public boolean isDashPhase()
	{
		return this.entityData.get(IN_DASH_PHASE);
	}
	
	public int getTicksElapsed()
	{
		return this.entityData.get(TICKS_ELAPSED);
	}
	
	private void updateMovement()
	{
		if(isFading())
		{
			moveTickFading();
			applyRotation();
			return;
		}
		
		var server = ((ServerLevel) level()).getServer();
		var manager = MeteorManager.get(server);
		
		int ticks = manager.getTicksForMeteor(this.getId());
		moveTick(ticks);
	}
	
	private void moveTickFading()
	{
		if(targetPos == null) return;
		
		Vec3 target = Vec3.atCenterOf(targetPos);
		double newY = this.getY() - fadeFallSpeed;
		setPos(target.x, newY, target.z);
	}
	public void startFadeOut()
	{
		if(!level().isClientSide)
		{
			this.fadeFallSpeed = calculateCurrentSpeed();
			this.entityData.set(FADING, true);
			this.entityData.set(FADE_TICKS, 0);
		}
	}
	
	private double calculateCurrentSpeed()
	{
		if(targetPos == null) return 0;
		
		Vec3 target = Vec3.atCenterOf(targetPos);
		double totalDistance = getSpawnHeightY(targetPos) - target.y;
		
		float dashPhaseTicks = MeteorManager.TOTAL_TICKS - MeteorManager.DASH_PHASE_TICKS;
		if(dashPhaseTicks <= 0) return 0;
		
		int ticksElapsed = this.entityData.get(TICKS_ELAPSED);
		float alpha = Mth.clamp((ticksElapsed - MeteorManager.DASH_PHASE_TICKS) / dashPhaseTicks, 0.0F, 1.0F);
		
		double speed = totalDistance * 0.90 * 3.0 * (alpha * alpha) / dashPhaseTicks;
		return Double.isFinite(speed) ? speed : 0;
	}
	
	public boolean isFading()
	{
		return this.entityData.get(FADING);
	}
	
	public float getFadeAlpha()
	{
		int ticks = this.entityData.get(FADE_TICKS);
		float progress = (float) ticks / FADE_DURATION_TICKS;
		return Mth.clamp(1.0F - progress, 0.0F, 1.0F);
	}
	
	private void tickFade()
	{
		int ticks = this.entityData.get(FADE_TICKS) + 1;
		this.entityData.set(FADE_TICKS, ticks);
		if(ticks >= FADE_DURATION_TICKS)
			discard();
	}
	
	
	public void setTargetPos(BlockPos pos)
	{
		this.targetPos = pos;
		this.entityData.set(TARGET_POS, pos);
		
		this.entityData.set(RENDER_YAW, 0.0F);
		this.entityData.set(RENDER_PITCH, 90.0F);
		this.setYRot(0.0F);
		this.yRotO = 0.0F;
		this.setXRot(90.0F);
		this.xRotO = 90.0F;
	}
	
	private void applyRotation()
	{
		this.entityData.set(RENDER_YAW, 0.0F);
		this.entityData.set(RENDER_PITCH, 90.0F);
		this.setYRot(0.0F);
		this.yRotO = 0.0F;
		this.setXRot(90.0F);
		this.xRotO = 90.0F;
	}
	
	
	public static double getSpawnHeightY(BlockPos targetPos)
	{
		return targetPos.getY() + HEIGHT_ABOVE_TARGET;
	}
	
	private float getUnifiedProgress(int ticksElapsed)
	{
		int total = MeteorManager.TOTAL_TICKS;
		int dashStart = MeteorManager.DASH_PHASE_TICKS;
		
		if(ticksElapsed <= 0) return 0.0F;
		if(ticksElapsed >= total) return 1.0F;
		
		float slowPhaseTicks = (float) dashStart;
		float dashPhaseTicks = (float) (total - dashStart);
		float slowDistanceFraction = 0.10F;
		
		if(ticksElapsed < dashStart)
		{
			float alpha = (float) ticksElapsed / slowPhaseTicks;
			return alpha * slowDistanceFraction;
		} else
		{
			float alpha = (float) (ticksElapsed - dashStart) / dashPhaseTicks;
			float dashInterpolation = (float) Math.pow(alpha, 3);
			
			return slowDistanceFraction + (1.0F - slowDistanceFraction) * dashInterpolation;
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		if (owner != null)
			owner.saveToNBT(compound, "owner");
		compound.putInt("targetX", targetPos.getX());
		compound.putInt("targetY", targetPos.getY());
		compound.putInt("targetZ", targetPos.getZ());
		compound.putFloat("size", getMeteorSize());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt)
	{
		if (nbt.contains("targetX") || nbt.contains("targetY") || nbt.contains("targetZ"))
			targetPos = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
		if(nbt.contains("owner"))
			owner = IdentifierHandler.load(nbt, "owner").result().orElse(null);
		if(nbt.contains("size"))
			setMeteorSize(nbt.getFloat("size"));
	}
	
	public PlayerIdentifier getOwner(){
		return owner;
	}
	
	public void setOwner(PlayerIdentifier owner)
	{
		this.owner = owner;
	}
	
	public void setMeteorSize(float size)
	{
		this.entityData.set(METEOR_SIZE, size);
	}
	
	public float getMeteorSize()
	{
		return this.entityData.get(METEOR_SIZE);
	}
	
	@Override
	public boolean isPickable()
	{
		return false;
	}
	
	@Override
	public boolean shouldBeSaved()
	{
		return false;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist)
	{
		return true;
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "smoke", 3, state -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.meteor.smoke"))));
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}
	
	@Override
	public double getTick(Object object)
	{
		return tickCount;
	}
}