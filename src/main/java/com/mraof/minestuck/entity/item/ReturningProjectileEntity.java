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

public class ReturningProjectileEntity extends ThrowableItemProjectile
{
	private int bounce;
	private int inBlockTicks = 0;
	
	private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(ReturningProjectileEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> NOCLIP = SynchedEntityData.defineId(ReturningProjectileEntity.class, EntityDataSerializers.BOOLEAN);
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, Level level)
	{
		super(type, level);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, double x, double y, double z, Level level)
	{
		super(type, x, y, z, level);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, LivingEntity livingEntityIn, Level level, int maxTick, boolean noBlockCollision)
	{
		super(type, livingEntityIn, level);
		setLifespan(maxTick);
		setNoclip(noBlockCollision);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(LIFESPAN, 200);
		entityData.define(NOCLIP, false);
	}
	
	@Override
	protected void onHit(HitResult result) //TODO onImpact does not trigger if already used within a couple ticks, allowing it to pass through blocks or entities
	{
		int damage = ProjectileDamaging.getDamageFromItem(getItemFromItemStack().getItem());
		
		if(result.getType() == HitResult.Type.ENTITY)
		{
			this.setDeltaMovement(getDeltaMovement().scale(-1.05));
			if(!level().isClientSide)
			{
				++bounce;
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
		} else if(result.getType() == HitResult.Type.BLOCK && !isNoclip())
		{
			BlockHitResult blockResult = (BlockHitResult) result;
			Direction blockFace = blockResult.getDirection();
			BlockPos blockPos = blockResult.getBlockPos();
			if(Block.canSupportCenter(level(), blockPos, blockFace))
			{
				this.setDeltaMovement(getDeltaMovement().scale(-1.05));
				if(!level().isClientSide)
				{
					++bounce;
					this.level().playSound(null, this.getX(), this.getY(), this.getZ(), MSSoundEvents.ITEM_PROJECTILE_BOUNCE.get(), SoundSource.NEUTRAL, 0.6F, 2.0F);
				}
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
			this.discard(); //TODO find a better set of conditions to remove entity(ticksExisted?)
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
		setNoclip(compound.getBoolean("noBlockCollision"));
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("bounce", bounce);
		compound.putInt("maxTick", getLifespan());
		compound.putInt("inBlockTicks", inBlockTicks);
		compound.putBoolean("noBlockCollision", isNoclip());
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.CHAKRAM.get();
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
	
	public boolean isNoclip()
	{
		return entityData.get(NOCLIP);
	}
	
	public void setNoclip(boolean v)
	{
		entityData.set(NOCLIP, v);
	}
}
