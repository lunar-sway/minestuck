package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.projectiles.ProjectileDamaging;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ConsumableProjectileEntity extends ThrowableItemProjectile
{
	public ConsumableProjectileEntity(EntityType<? extends ConsumableProjectileEntity> type, Level level)
	{
		super(type, level);
	}
	
	public ConsumableProjectileEntity(EntityType<? extends ConsumableProjectileEntity> type, double x, double y, double z, Level level)
	{
		super(type, x, y, z, level);
	}
	
	public ConsumableProjectileEntity(EntityType<? extends ConsumableProjectileEntity> type, LivingEntity livingEntityIn, Level level)
	{
		super(type, livingEntityIn, level);
	}
	
	private static boolean isNonCreativePlayer(Entity entity)
	{
		return entity instanceof Player player && !player.isCreative();
	}
	
	@Override
	protected void onHit(HitResult result)
	{
		int damage = ProjectileDamaging.getDamageFromItem(getItemFromItemStack().getItem());
		
		if(result.getType() == HitResult.Type.ENTITY)
		{
			Entity entity = ((EntityHitResult) result).getEntity();
			if(entity instanceof UnderlingEntity)
				entity.hurt(this.damageSources().thrown(this, getOwner()), damage * 1.5F);
			else
				entity.hurt(this.damageSources().thrown(this, getOwner()), damage);
		}
		if(isNonCreativePlayer(getOwner()))
		{
			if(random.nextFloat() < 0.99F)
			{
				ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getItemFromItemStack());
				level().addFreshEntity(itemEntity);
			} else
			{
				this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 0.8F, 1.5F);
			}
		}
		this.discard();
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.SHURIKEN.get();
	}
	
	public ItemStack getItemFromItemStack()
	{
		ItemStack itemstack = this.getItemRaw();
		return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
	}
}
