package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.projectiles.ProjectileDamaging;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BouncingProjectileEntity extends ThrowableItemProjectile
{
	private int bounce;
	private int inBlockTicks = 0;
	
	private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(BouncingProjectileEntity.class, EntityDataSerializers.INT);
	
	public BouncingProjectileEntity(EntityType<? extends BouncingProjectileEntity> type, Level level)
	{
		super(type, level);
	}
	
	public BouncingProjectileEntity(EntityType<? extends BouncingProjectileEntity> type, double x, double y, double z, Level level)
	{
		super(type, x, y, z, level);
	}
	
	public BouncingProjectileEntity(EntityType<? extends BouncingProjectileEntity> type, LivingEntity owner, Level level, int maxTick)
	{
		super(type, owner, level);
		setLifespan(maxTick);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(LIFESPAN, 200);
	}
	
	@Override
	protected void onHit(HitResult result)
	{
		int damage = ProjectileDamaging.getDamageFromItem(getItemFromItemStack().getItem());
		++bounce;
		
		double velocityX = this.getDeltaMovement().x;
		double velocityY = this.getDeltaMovement().y;
		double velocityZ = this.getDeltaMovement().z;
		double absVelocityX = Math.abs(velocityX);
		double absVelocityY = Math.abs(velocityY);
		double absVelocityZ = Math.abs(velocityZ);
		
		if(result.getType() == HitResult.Type.ENTITY)
		{
			if(!level().isClientSide)
			{
				Entity entity = ((EntityHitResult) result).getEntity();
				if(entity instanceof UnderlingEntity)
					entity.hurt(this.damageSources().thrown(this, getOwner()), damage * 1.5F);
				else if(entity != getOwner())
					entity.hurt(this.damageSources().thrown(this, getOwner()), damage);
				else
				{
					resetThrower();
				}
			}
			if(absVelocityX >= absVelocityY && absVelocityX >= absVelocityZ)
				this.setDeltaMovement(-velocityX, velocityY, velocityZ);
			if(absVelocityY >= absVelocityX && absVelocityY >= absVelocityZ)
				this.setDeltaMovement(velocityX, -velocityY, velocityZ);
			if(absVelocityZ >= absVelocityY && absVelocityZ >= absVelocityX)
				this.setDeltaMovement(velocityX, velocityY, -velocityZ);
			
		} else if(result.getType() == HitResult.Type.BLOCK)
		{
			BlockHitResult blockResult = (BlockHitResult) result;
			Direction blockFace = blockResult.getDirection();
			BlockPos blockPos = blockResult.getBlockPos();
			
			if(Block.canSupportCenter(level(), blockPos, blockFace))
			{
				if(!level().isClientSide)
				{
					this.level().playSound(null, this.getX(), this.getY(), this.getZ(), MSSoundEvents.ITEM_PROJECTILE_BOUNCE.get(), SoundSource.NEUTRAL, 0.6F, 2.0F);
					++bounce;
				}
				
				if(blockFace == Direction.EAST || blockFace == Direction.WEST)
					this.setDeltaMovement(-velocityX, velocityY, velocityZ);
				if(blockFace == Direction.DOWN || blockFace == Direction.UP)
					this.setDeltaMovement(velocityX, -velocityY, velocityZ);
				if(blockFace == Direction.NORTH || blockFace == Direction.SOUTH)
					this.setDeltaMovement(velocityX, velocityY, -velocityZ);
			}
			
			if(Block.canSupportCenter(level(), blockPos, blockFace) && blockResult.isInside())
			{
				++inBlockTicks;
			}
		}
		
		if(bounce > 10)
		{
			resetThrower();
		}
	}
	
	public void resetThrower()
	{
		if(getOwner() instanceof Player player)
		{
			player.getCooldowns().addCooldown(getItemRaw().getItem(), 5);
			this.discard();
		}
	}
	
	public void tick()
	{
		Vec3 pos = position();
		this.xOld = pos.x;
		this.yOld = pos.y;
		this.zOld = pos.z;
		
		if(this.isInWater())
			this.setDeltaMovement(getDeltaMovement().scale(1.2));
		else
			this.setDeltaMovement(getDeltaMovement().scale(1.005));
		
		if(this.tickCount >= getLifespan() || inBlockTicks >= 2)
		{
			resetThrower();
		}
		
		super.tick();
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		bounce = compound.getInt("bounce");
		setLifespan(compound.getInt("maxTick"));
		inBlockTicks = compound.getInt("inBlockTicks");
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("bounce", bounce);
		compound.putInt("maxTick", getLifespan());
		compound.putInt("inBlockTicks", inBlockTicks);
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.SORCERERS_PINBALL.get();
	}
	
	public ItemStack getItemFromItemStack()
	{
		ItemStack itemstack = this.getItemRaw();
		return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
	}
	
	public int getLifespan()
	{
		return entityData.get(LIFESPAN);
	}
	
	public void setLifespan(int v)
	{
		entityData.set(LIFESPAN, v);
	}
}
