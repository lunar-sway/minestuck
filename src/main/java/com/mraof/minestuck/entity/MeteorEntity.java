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
	public static final double MAX_WORLD_HEIGHT_LIMIT = 380.0;
	public static final double HEIGHT_ABOVE_TARGET = 300.0;
	
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
		
		if(!level().isClientSide)
			this.entityData.set(IN_DASH_PHASE, ticksElapsed >= MeteorManager.DASH_PHASE_TICKS);
		
		Vec3 target = Vec3.atCenterOf(targetPos);
		double targetX = target.x;
		double targetZ = target.z;
		
		double startHeightY = getSpawnHeightY(targetPos);
		double totalDistanceY = startHeightY - target.y;
		
		float progress = getUnifiedProgress(ticksElapsed);
		double expectedY = startHeightY - (totalDistanceY * progress);
		
		if(expectedY < target.y) expectedY = target.y;
		
		setPos(targetX, expectedY, targetZ);
		applyRotation();
	}
	
	public boolean isDashPhase()
	{
		return this.entityData.get(IN_DASH_PHASE);
	}
	
	private void updateMovement()
	{
		var server = ((ServerLevel) level()).getServer();
		var manager = MeteorManager.get(server);
		
		int ticks = manager.getTicksForMeteor(this.getId());
		
		moveTick(ticks);
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
		double calculatedY = targetPos.getY() + HEIGHT_ABOVE_TARGET;
		return Math.min(calculatedY, MAX_WORLD_HEIGHT_LIMIT);
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