package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;

public class KnockbackWeaponItem extends WeaponItem
{
	
	public KnockbackWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
	{
		float randFloat = 1.5F + attacker.getRNG().nextFloat();
		if(!attacker.getEntityWorld().isRemote)
		{
			target.setMotion(target.getMotion().x * randFloat, target.getMotion().y, target.getMotion().z * randFloat);
		}
		return super.hitEntity(itemStack, target, attacker);
	}
}
