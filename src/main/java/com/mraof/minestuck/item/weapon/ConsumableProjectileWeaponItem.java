package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.ConsumableProjectileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ConsumableProjectileWeaponItem extends Item
{
	public ConsumableProjectileWeaponItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack item = playerIn.getHeldItem(handIn);
		
		worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 0.8F, 1.5F);
		
		if(!worldIn.isRemote)
		{
			ConsumableProjectileEntity projectileEntity = new ConsumableProjectileEntity(MSEntityTypes.CONSUMABLE_PROJECTILE, playerIn, worldIn);
			projectileEntity.setItem(item);
			projectileEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.0F, 1.7F);
			worldIn.addEntity(projectileEntity);
		}
		if(!playerIn.isCreative())
		{
			item.shrink(1);
		}
		
		playerIn.getCooldownTracker().setCooldown(this, 7);
		playerIn.addStat(Stats.ITEM_USED.get(this));
		return new ActionResult<>(ActionResultType.SUCCESS, item);
	}
}