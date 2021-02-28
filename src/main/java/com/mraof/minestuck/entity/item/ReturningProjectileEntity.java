package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.projectiles.ProjectileDamaging;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
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
import net.minecraft.util.math.*;
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
	protected void onImpact(RayTraceResult result) //TODO onImpact does not trigger if already used within a couple ticks, allowing it to pass through blocks or entities
	{
		int damage = ProjectileDamaging.getDamageFromItem(getItemFromItemStack().getItem());
		
		if(result.getType() == RayTraceResult.Type.ENTITY)
		{
			this.setMotion(getMotion().scale(-1.05));
			if(!world.isRemote)
			{
				++bounce;
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				
				if(entity instanceof UnderlingEntity)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage * 1.5F);
				else if(entity != this.getThrower())
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
				else
				{
					resetThrower();
				}
			}
		} else if(result.getType() == RayTraceResult.Type.BLOCK && !noBlockCollision)
		{
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
			Direction blockFace = blockResult.getFace();
			BlockPos blockPos = blockResult.getPos();
			if(Block.hasEnoughSolidSide(world, blockPos, blockFace))
			{
				this.setMotion(getMotion().scale(-1.05));
				if(!world.isRemote)
				{
					++bounce;
					this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 0.6F, 4.0F);
				}
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
		if(this.getThrower() instanceof PlayerEntity)
		{
			((PlayerEntity)this.getThrower()).getCooldownTracker().setCooldown(func_213882_k().getItem(), 5);
			this.remove(); //TODO find a better set of conditions to remove entity(ticksExisted?)
		}
	}
	
	public void tick()
	{
		Vec3d pos = getPositionVec();
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
		inBlockTicks = compound.getInt("inBlockTicks");;
		noBlockCollision = compound.getBoolean("noBlockCollision");
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putInt("bounce", bounce);
		compound.putInt("maxTick", maxTick);
		compound.putInt("inBlockTicks", inBlockTicks);
		compound.putBoolean("noBlockCollision", noBlockCollision);
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
	
	public ItemStack getItemFromItemStack() {
		ItemStack itemstack = this.func_213882_k();
		return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
	}
}
