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
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ConsumableProjectileEntity extends ProjectileItemEntity implements RendersAsItem
{
	public int damage = 0;
	
	public ConsumableProjectileEntity(EntityType<? extends ConsumableProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public ConsumableProjectileEntity(EntityType<? extends ConsumableProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public ConsumableProjectileEntity(EntityType<? extends ConsumableProjectileEntity> type, LivingEntity livingEntityIn, World worldIn, int damage)
	{
		super(type, livingEntityIn, worldIn);
		this.damage = damage;
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
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.damage);
			}
			if(!throwerPlayer.isCreative())
			{
				if(rand.nextFloat() < 0.99F)
				{
					ItemEntity itemEntity = new ItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), this.getItem());
					world.addEntity(itemEntity);
				} else
				{
					this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.8F, 1.5F);
				}
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
