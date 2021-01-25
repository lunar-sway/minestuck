package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.client.renderer.entity.RendersAsItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ItemRenderedProjectileEntity extends ProjectileItemEntity implements RendersAsItem
{
	public ItemRenderedProjectileEntity(EntityType<? extends ItemRenderedProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public ItemRenderedProjectileEntity(EntityType<? extends ItemRenderedProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public ItemRenderedProjectileEntity(EntityType<? extends ItemRenderedProjectileEntity> type, LivingEntity livingEntityIn, World worldIn)
	{
		super(type, livingEntityIn, worldIn);
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
	}
	
	@Override
	protected void onImpact(RayTraceResult result)
	{
		if(this.getThrower() instanceof PlayerEntity)
		{
			PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
			if(!this.world.isRemote && result.getType() == RayTraceResult.Type.ENTITY)
			{
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 3);
			}
			if(!throwerPlayer.isCreative())
			{
				ItemEntity itemEntity = new ItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), this.getItem());
				world.addEntity(itemEntity);
			}
			this.remove();
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
		return MSItems.CLUBS_SUITARANG;
	}
}
