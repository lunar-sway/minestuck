package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.client.renderer.entity.RendersAsItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BouncingProjectileEntity extends ReturningProjectileEntity implements RendersAsItem
{
	private int bounce;
	private int inBlockTicks = 0;
	private BlockRayTraceResult blockResult;
	private Direction blockFace;
	private BlockPos blockPos;
	
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
		if(throwerPlayer != null && !world.isRemote)
		{
			int cooldownTicks = throwerPlayer.getCooldownTracker().hashCode();
			
			++bounce;
			
			double velocityX = this.getMotion().x;
			double velocityY = this.getMotion().y;
			double velocityZ = this.getMotion().z;
			double absVelocityX = (velocityX * velocityX) / velocityX;
			double absVelocityY = (velocityY * velocityY) / velocityY;
			double absVelocityZ = (velocityZ * velocityZ) / velocityZ;
			
			if(result.getType() == RayTraceResult.Type.ENTITY)
			{
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				if(entity != throwerPlayer)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
				if(entity == throwerPlayer)
				{
					resetThrower();
				}
				
				if(absVelocityX >= absVelocityY && absVelocityX >= absVelocityZ)
					this.setMotion(-velocityX, velocityY, velocityZ);
				if(absVelocityY >= absVelocityX && absVelocityY >= absVelocityZ)
					this.setMotion(velocityX, -velocityY, velocityZ);
				if(absVelocityZ >= absVelocityY && absVelocityZ >= absVelocityX)
					this.setMotion(velocityX, velocityY, -velocityZ);
				
			} else if(result.getType() == RayTraceResult.Type.BLOCK)
			{
				blockResult = (BlockRayTraceResult) result;
				blockFace = blockResult.getFace();
				blockPos = blockResult.getPos();
				
				if(Block.hasEnoughSolidSide(world, blockPos, blockFace))
				{
					this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 0.6F, 4.0F);
					
					++bounce;
					
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
			
			if(bounce > 15 || cooldownTicks <= 5)
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
		
		if(this.ticksExisted >= maxTick || inBlockTicks >= 1)
		{
			resetThrower();
		}
		
		super.tick();
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
}
