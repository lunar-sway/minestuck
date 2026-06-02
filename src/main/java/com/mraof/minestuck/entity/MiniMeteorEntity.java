package com.mraof.minestuck.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MiniMeteorEntity extends ThrowableProjectile implements ItemSupplier, GeoEntity
{
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private static final EntityDataAccessor<Float> RENDER_YAW = SynchedEntityData.defineId(MiniMeteorEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> RENDER_PITCH = SynchedEntityData.defineId(MiniMeteorEntity.class, EntityDataSerializers.FLOAT);
	
	
	private static final float SPEED = 2.5f;
	private BlockPos targetPos = BlockPos.ZERO;
	
	private Vec3 direction = new Vec3(0, -1, 0);
	
	public MiniMeteorEntity(EntityType<? extends ThrowableProjectile> type, Level level)
	{
		super(type, level);
		this.noPhysics = true;
		this.setNoGravity(true);
	}
	
	public void setTargetPos(BlockPos pos)
	{
		this.targetPos = pos;
		Vec3 diff = Vec3.atCenterOf(pos).subtract(position());
		if(diff.length() > 0.01)
		{
			this.direction = diff.normalize();
			applyRotationFromDirection(this.direction);
		}
	}
	
	public BlockPos getTargetPos()
	{
		return targetPos;
	}
	
	private void applyRotationFromDirection(Vec3 dir)
	{
		float yaw = (float) (Math.toDegrees(Math.atan2(-dir.x, dir.z)));
		float pitch = (float) (Math.toDegrees(-Math.asin(Math.max(-1.0, Math.min(1.0, dir.y)))));
		this.entityData.set(RENDER_YAW, yaw);
		this.entityData.set(RENDER_PITCH, pitch);
		this.setYRot(yaw);
		this.yRotO = yaw;
		this.setXRot(pitch);
		this.xRotO = pitch;
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
		builder.define(RENDER_YAW, 0.0f);
		builder.define(RENDER_PITCH, 0.0f);
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
		
		this.baseTick();
		if(this.isRemoved()) return;
		
		if(!level().isClientSide)
		{
			Vec3 current = position();
			Vec3 next = current.add(direction.scale(SPEED));
			
			Vec3 toTarget = Vec3.atCenterOf(targetPos).subtract(current);
			if(toTarget.dot(direction) <= 0)
			{
				onImpactManual();
				discard();
				return;
			}
			
			BlockPos nextBlock = BlockPos.containing(next);
			if(!level().getBlockState(nextBlock).isAir() && level().getBlockState(nextBlock).isSolidRender(level(), nextBlock))
			{
				onImpactManual();
				discard();
				return;
			}
			
			setPos(next.x, next.y, next.z);
		} else
		{
			this.setYRot(this.getRenderYaw());
			this.setXRot(this.getRenderPitch());
		}
	}
	
	
	@Override
	protected void onHit(HitResult result)
	{
		if(!level().isClientSide) onImpactManual();
		discard();
	}
	
	private void onImpactManual()
	{
		int radius = 1 + level().random.nextInt(3);
		float power = 0.5f + radius * 0.5f;
		
		BlockPos surfacePos = level().getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, blockPosition());
		
		level().explode(this, surfacePos.getX() + 0.5, surfacePos.getY(), surfacePos.getZ() + 0.5, power, Level.ExplosionInteraction.BLOCK);
		
		for(int dx = -radius; dx <= radius; dx++)
		{
			for(int dz = -radius; dz <= radius; dz++)
			{
				if(dx * dx + dz * dz > radius * radius) continue;
				BlockPos pos = surfacePos.offset(dx, 0, dz);
				BlockPos surface = level().getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, pos);
				if(level().isEmptyBlock(surface) && level().getBlockState(surface.below()).isSolidRender(level(), surface.below()))
				{
					if(level().random.nextFloat() < 0.6f)
						level().setBlockAndUpdate(surface, net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState());
				}
			}
		}
		
		if(!level().isClientSide)
		{
			var server = ((net.minecraft.server.level.ServerLevel) level()).getServer();
			server.getPlayerList().getPlayers().forEach(p -> net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(p, new com.mraof.minestuck.network.MeteorPackets.MiniMeteorImpact(getX(), getY(), getZ())));
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putInt("TargetX", targetPos.getX());
		tag.putInt("TargetY", targetPos.getY());
		tag.putInt("TargetZ", targetPos.getZ());
		tag.putDouble("DirX", direction.x);
		tag.putDouble("DirY", direction.y);
		tag.putDouble("DirZ", direction.z);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		targetPos = new BlockPos(tag.getInt("TargetX"), tag.getInt("TargetY"), tag.getInt("TargetZ"));
		direction = new Vec3(tag.getDouble("DirX"), tag.getDouble("DirY"), tag.getDouble("DirZ"));
		if(direction.length() < 0.01) direction = new Vec3(0, -1, 0);
		applyRotationFromDirection(direction);
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
	
	@Override
	public ItemStack getItem()
	{
		return new ItemStack(Items.FIRE_CHARGE);
	}
	
	@Override
	public ItemStack getPickResult()
	{
		return new ItemStack(Items.FIRE_CHARGE);
	}
}