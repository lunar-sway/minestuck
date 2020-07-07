package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;

/**
 * Created by mraof on 2017 January 18 at 6:24 PM.
 */
public class FireWeaponItem extends WeaponItem
{
	private final int duration;
	
	public FireWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, int duration, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
		this.duration = duration;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity player)
	{
		target.setFire(duration);
		return super.hitEntity(itemStack, target, player);
	}
}