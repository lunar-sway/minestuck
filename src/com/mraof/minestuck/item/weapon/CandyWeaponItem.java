package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.entity.underling.EntityUnderling;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;

/**
 * Created by mraof on 2017 January 18 at 7:42 PM.
 */
public class CandyWeaponItem extends WeaponItem
{
	public CandyWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		if(target instanceof EntityUnderling)
		{
			((EntityUnderling) target).dropCandy = true;
		}
		return super.hitEntity(itemStack, target, player);
	}
}