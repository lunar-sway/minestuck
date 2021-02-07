package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.ReturningProjectileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ReturningProjectileWeaponItem extends Item
{
	public ReturningProjectileWeaponItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack item = playerIn.getHeldItem(handIn);
		
		worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.2F);
		
		if(!worldIn.isRemote)
		{
			ReturningProjectileEntity projectileEntity = new ReturningProjectileEntity(MSEntityTypes.RETURNING_PROJECTILE, playerIn, worldIn);
			projectileEntity.setItem(item);
			projectileEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			projectileEntity.setNoGravity(true);
			worldIn.addEntity(projectileEntity);
		}
		
		item.damageItem(1, playerIn, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
		
		playerIn.getCooldownTracker().setCooldown(this, 60);
		playerIn.addStat(Stats.ITEM_USED.get(this));
		return new ActionResult<>(ActionResultType.SUCCESS, item);
	}
}