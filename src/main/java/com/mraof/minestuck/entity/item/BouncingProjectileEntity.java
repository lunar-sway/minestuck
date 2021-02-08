package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.client.renderer.entity.RendersAsItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BouncingProjectileEntity extends ReturningProjectileEntity implements RendersAsItem
{
	private int bounce;
	
	public BouncingProjectileEntity(EntityType<? extends BouncingProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public BouncingProjectileEntity(EntityType<? extends BouncingProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public BouncingProjectileEntity(EntityType<? extends BouncingProjectileEntity> type, LivingEntity livingEntityIn, World worldIn, int damage, int maxTick)
	{
		super(type, livingEntityIn, worldIn, damage, maxTick);
	}
	
	@Override
	protected void onImpact(RayTraceResult result)
	{
		PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
		if(throwerPlayer != null)
		{
			if(this.world.getEntitiesWithinAABB(PlayerEntity.class, getBoundingBox().grow(5)).contains(throwerPlayer))
			{
				resetThrower();
			}
			
			//RayTraceResult.Type.BLOCK;
			//result.getHitVec().inverse();
			
			if(!this.world.isRemote && result.getType() == RayTraceResult.Type.ENTITY)
			{
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				if(entity != throwerPlayer)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
				if(entity == throwerPlayer)
				{
					resetThrower();
				}
			} else if(!this.world.isRemote && result.getType() == RayTraceResult.Type.BLOCK)
			{
				++bounce;
				
				//result.getHitVec().rotatePitch(1.0F);
				//this.setMotion(getMotion());
				double velocityX = this.getMotion().x;
				double velocityY = this.getMotion().y;
				double velocityZ = this.getMotion().z;
				double absVelocityX = (velocityX * velocityX) / velocityX;
				double absVelocityY = (velocityY * velocityY) / velocityY;
				double absVelocityZ = (velocityZ * velocityZ) / velocityZ;
				/*
				this.moveRelative();
				this.handlePistonMovement;
				Vec3d pos = this.getMotion();
				pos = this.maybeBackOffFromEdge(pos, 0);*/
				
				if(absVelocityX >= absVelocityY && absVelocityX >= absVelocityZ)
					this.setMotion(-velocityX,velocityY,velocityZ);
				
				if(absVelocityY >= absVelocityX && absVelocityY >= absVelocityZ)
					this.setMotion(velocityX,-velocityY,velocityZ);
				
				if(absVelocityZ >= absVelocityY && absVelocityZ >= absVelocityX)
					this.setMotion(velocityX,velocityY,-velocityZ);
				
				this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 0.6F, 4.0F);
			}
			
			if(!world.isRemote)
			{
				int cooldownTicks = throwerPlayer.getCooldownTracker().hashCode();
				if(cooldownTicks <= 5)
				{
					resetThrower();
				}
			}
			if(bounce > 15)
			{
				resetThrower();
			}
		}
	}
	
	public void resetThrower()
	{
		PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
		if(throwerPlayer != null)
		{
			throwerPlayer.getCooldownTracker().setCooldown(func_213882_k().getItem(), 5);
			this.remove();
		}
	}
	
	public void tick()
	{
		Vec3d pos = getPositionVec();
		this.lastTickPosX = pos.x;
		this.lastTickPosY = pos.y;
		this.lastTickPosZ = pos.z;
		super.tick();
		
		if(this.ticksExisted >= maxTick)
		{
			resetThrower();
		}
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.CHAKRAM;
	}
}
