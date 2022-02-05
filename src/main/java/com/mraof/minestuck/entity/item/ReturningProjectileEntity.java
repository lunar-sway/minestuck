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

public class ReturningProjectileEntity extends ProjectileItemEntity
{
	private int bounce;
	public int maxTick = 0;
	private int inBlockTicks = 0;
	private boolean noBlockCollision;
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, LivingEntity livingEntityIn, World worldIn, int maxTick, boolean noBlockCollision)
	{
		super(type, livingEntityIn, worldIn);
		this.maxTick = maxTick;
		this.noBlockCollision = noBlockCollision;
	}
	
	@Override
	protected void onHit(RayTraceResult result) //TODO onImpact does not trigger if already used within a couple ticks, allowing it to pass through blocks or entities
	{
		int damage = ProjectileDamaging.getDamageFromItem(getItemFromItemStack().getItem());
		
		if(result.getType() == RayTraceResult.Type.ENTITY)
		{
			this.setDeltaMovement(getDeltaMovement().scale(-1.05));
			if(!level.isClientSide)
			{
				++bounce;
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
		} else if(result.getType() == RayTraceResult.Type.BLOCK && !noBlockCollision)
		{
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
			Direction blockFace = blockResult.getDirection();
			BlockPos blockPos = blockResult.getBlockPos();
			if(Block.canSupportCenter(level, blockPos, blockFace))
			{
				this.setDeltaMovement(getDeltaMovement().scale(-1.05));
				if(!level.isClientSide)
				{
					++bounce;
					this.level.playSound(null, this.getX(), this.getY(), this.getZ(), MSSoundEvents.ITEM_PROJECTILE_BOUNCE, SoundCategory.NEUTRAL, 0.6F, 2.0F);
				}
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
			this.remove(); //TODO find a better set of conditions to remove entity(ticksExisted?)
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
		inBlockTicks = compound.getInt("inBlockTicks");;
		noBlockCollision = compound.getBoolean("noBlockCollision");
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("bounce", bounce);
		compound.putInt("maxTick", maxTick);
		compound.putInt("inBlockTicks", inBlockTicks);
		compound.putBoolean("noBlockCollision", noBlockCollision);
	}
	
	@Override
	public IPacket<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.CHAKRAM;
	}
	
	public ItemStack getItemFromItemStack() {
		ItemStack itemstack = this.getItemRaw();
		return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
	}
}
