package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.client.renderer.entity.RendersAsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ReturningProjectileEntity extends ConsumableProjectileEntity implements RendersAsItem
{
	private int bounce;
	public int maxTick = 0;
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, LivingEntity livingEntityIn, World worldIn, int damage, int maxTick)
	{
		super(type, livingEntityIn, worldIn, damage);
		this.maxTick = maxTick;
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
			++bounce;
			
			if(!this.world.isRemote && result.getType() == RayTraceResult.Type.ENTITY)
			{
				this.setMotion(getMotion().scale(-1.1));
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				if(entity != throwerPlayer)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
				if(entity == throwerPlayer)
				{
					resetThrower();
				}
			} else if(!this.world.isRemote && result.getType() == RayTraceResult.Type.BLOCK && !this.getItem().equals(new ItemStack(MSItems.UMBRAL_INFILTRATOR)))
			{
				this.setMotion(getMotion().scale(-1.1));
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
