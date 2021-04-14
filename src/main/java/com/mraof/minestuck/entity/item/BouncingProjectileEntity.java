package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.projectiles.ProjectileDamaging;
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
import net.minecraft.util.SoundEvents;
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
	protected void onImpact(RayTraceResult result)
	{
		int damage = ProjectileDamaging.getDamageFromItem(getItemFromItemStack().getItem());
		++bounce;
		
		double velocityX = this.getMotion().x;
		double velocityY = this.getMotion().y;
		double velocityZ = this.getMotion().z;
		double absVelocityX = Math.abs(velocityX);
		double absVelocityY = Math.abs(velocityY);
		double absVelocityZ = Math.abs(velocityZ);
		
		if(result.getType() == RayTraceResult.Type.ENTITY)
		{
			if(!world.isRemote)
			{
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				if(entity instanceof UnderlingEntity)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, getShooter()), damage * 1.5F);
				else if(entity != getShooter())
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, getShooter()), damage);
				else
				{
					resetThrower();
				}
			}
			if(absVelocityX >= absVelocityY && absVelocityX >= absVelocityZ)
				this.setMotion(-velocityX, velocityY, velocityZ);
			if(absVelocityY >= absVelocityX && absVelocityY >= absVelocityZ)
				this.setMotion(velocityX, -velocityY, velocityZ);
			if(absVelocityZ >= absVelocityY && absVelocityZ >= absVelocityX)
				this.setMotion(velocityX, velocityY, -velocityZ);
			
		} else if(result.getType() == RayTraceResult.Type.BLOCK)
		{
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
			Direction blockFace = blockResult.getFace();
			BlockPos blockPos = blockResult.getPos();
			
			if(Block.hasEnoughSolidSide(world, blockPos, blockFace))
			{
				if(!world.isRemote)
				{
					this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 0.6F, 4.0F);
					++bounce;
				}
				
				if(blockFace == Direction.EAST || blockFace == Direction.WEST)
					this.setMotion(-velocityX, velocityY, velocityZ);
				if(blockFace == Direction.DOWN || blockFace == Direction.UP)
					this.setMotion(velocityX, -velocityY, velocityZ);
				if(blockFace == Direction.NORTH || blockFace == Direction.SOUTH)
					this.setMotion(velocityX, velocityY, -velocityZ);
			}
			
			if(Block.hasEnoughSolidSide(world, blockPos, blockFace) && blockResult.isInside())
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
		if(getShooter() instanceof PlayerEntity)
		{
			((PlayerEntity)getShooter()).getCooldownTracker().setCooldown(func_213882_k().getItem(), 5);
			this.remove();
		}
	}
	
	public void tick()
	{
		Vector3d pos = getPositionVec();
		this.lastTickPosX = pos.x;
		this.lastTickPosY = pos.y;
		this.lastTickPosZ = pos.z;
		
		if(this.isInWater())
			this.setMotion(getMotion().scale(1.2));
		else
			this.setMotion(getMotion().scale(1.005));
		
		if(this.ticksExisted >= maxTick || inBlockTicks >= 2)
		{
			resetThrower();
		}
		
		super.tick();
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		bounce = compound.getInt("bounce");
		maxTick = compound.getInt("maxTick");
		inBlockTicks = compound.getInt("inBlockTicks");
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putInt("bounce", bounce);
		compound.putInt("maxTick", maxTick);
		compound.putInt("inBlockTicks", inBlockTicks);
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.SORCERERS_PINBALL;
	}
	
	public ItemStack getItemFromItemStack() {
		ItemStack itemstack = this.func_213882_k();
		return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
	}
}
