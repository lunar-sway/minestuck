package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.projectiles.ProjectileDamaging;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BouncingProjectileEntity extends ProjectileItemEntity
{
	private int bounce;
	public int maxTick = 0;
	private int inBlockTicks = 0;
	
	public BouncingProjectileEntity(EntityType<? extends BouncingProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public BouncingProjectileEntity(EntityType<? extends BouncingProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public BouncingProjectileEntity(EntityType<? extends BouncingProjectileEntity> type, LivingEntity livingEntityIn, World worldIn, int maxTick)
	{
		super(type, livingEntityIn, worldIn);
		this.maxTick = maxTick;
	}
	
	@Override
	protected void onHit(RayTraceResult result)
	{
		int damage = ProjectileDamaging.getDamageFromItem(getItemFromItemStack().getItem());
		++bounce;
		
		double velocityX = this.getDeltaMovement().x;
		double velocityY = this.getDeltaMovement().y;
		double velocityZ = this.getDeltaMovement().z;
		double absVelocityX = Math.abs(velocityX);
		double absVelocityY = Math.abs(velocityY);
		double absVelocityZ = Math.abs(velocityZ);
		
		if(result.getType() == RayTraceResult.Type.ENTITY)
		{
			if(!level.isClientSide)
			{
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				if(entity instanceof UnderlingEntity)
					entity.hurt(DamageSource.thrown(this, getOwner()), damage * 1.5F);
				else if(entity != getOwner())
					entity.hurt(DamageSource.thrown(this, getOwner()), damage);
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
			
		} else if(result.getType() == RayTraceResult.Type.BLOCK)
		{
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
			Direction blockFace = blockResult.getDirection();
			BlockPos blockPos = blockResult.getBlockPos();
			
			if(Block.canSupportCenter(level, blockPos, blockFace))
			{
				if(!level.isClientSide)
				{
					this.level.playSound(null, this.getX(), this.getY(), this.getZ(), MSSoundEvents.ITEM_PROJECTILE_BOUNCE, SoundCategory.NEUTRAL, 0.6F, 2.0F);
					++bounce;
				}
				
				if(blockFace == Direction.EAST || blockFace == Direction.WEST)
					this.setDeltaMovement(-velocityX, velocityY, velocityZ);
				if(blockFace == Direction.DOWN || blockFace == Direction.UP)
					this.setDeltaMovement(velocityX, -velocityY, velocityZ);
				if(blockFace == Direction.NORTH || blockFace == Direction.SOUTH)
					this.setDeltaMovement(velocityX, velocityY, -velocityZ);
			}
			
			if(Block.canSupportCenter(level, blockPos, blockFace) && blockResult.isInside())
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
		if(getOwner() instanceof PlayerEntity)
		{
			((PlayerEntity)getOwner()).getCooldowns().addCooldown(getItemRaw().getItem(), 5);
			this.remove();
		}
	}
	
	public void tick()
	{
		Vector3d pos = position();
		this.xOld = pos.x;
		this.yOld = pos.y;
		this.zOld = pos.z;
		
		if(this.isInWater())
			this.setDeltaMovement(getDeltaMovement().scale(1.2));
		else
			this.setDeltaMovement(getDeltaMovement().scale(1.005));
		
		if(this.tickCount >= maxTick || inBlockTicks >= 2)
		{
			resetThrower();
		}
		
		super.tick();
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound)
	{
		super.readAdditionalSaveData(compound);
		bounce = compound.getInt("bounce");
		maxTick = compound.getInt("maxTick");
		inBlockTicks = compound.getInt("inBlockTicks");
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("bounce", bounce);
		compound.putInt("maxTick", maxTick);
		compound.putInt("inBlockTicks", inBlockTicks);
	}
	
	@Override
	public IPacket<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.SORCERERS_PINBALL;
	}
	
	public ItemStack getItemFromItemStack() {
		ItemStack itemstack = this.getItemRaw();
		return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
	}
}
