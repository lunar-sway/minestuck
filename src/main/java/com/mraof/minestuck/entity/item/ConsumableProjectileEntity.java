package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.projectiles.ProjectileDamaging;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ConsumableProjectileEntity extends ProjectileItemEntity
{
	//private int damage = 0;
	
	public ConsumableProjectileEntity(EntityType<? extends ConsumableProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public ConsumableProjectileEntity(EntityType<? extends ConsumableProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public ConsumableProjectileEntity(EntityType<? extends ConsumableProjectileEntity> type, LivingEntity livingEntityIn, World worldIn)
	{
		super(type, livingEntityIn, worldIn);
	}
	
	private static boolean isNonCreativePlayer(LivingEntity entity)
	{
		return entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative();
	}
	
	@Override
	protected void onImpact(RayTraceResult result)
	{
		int damage = ProjectileDamaging.getDamageFromItem(getItemFromItemStack().getItem());
		
		if(result.getType() == RayTraceResult.Type.ENTITY)
		{
			Entity entity = ((EntityRayTraceResult) result).getEntity();
			if(entity instanceof UnderlingEntity)
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage * 1.5F);
			else
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
		}
		if(isNonCreativePlayer(this.getThrower()))
		{
			if(rand.nextFloat() < 0.99F)
			{
				ItemEntity itemEntity = new ItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), this.getItem());
				world.addEntity(itemEntity);
			} else
			{
				this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.NEUTRAL, 0.8F, 1.5F);
			}
		}
		this.remove();
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.SHURIKEN;
	}
	
	public ItemStack getItemFromItemStack() {
		ItemStack itemstack = this.func_213882_k();
		return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
	}
}
