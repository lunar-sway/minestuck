package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class PogoFarmineItem extends FarmineItem
{
	private double pogoMotion;
	
	public PogoFarmineItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, int radius, int terminus, double pogoMotion, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, radius, terminus, toolType, builder);
		this.pogoMotion = pogoMotion;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity player)
	{
		super.hitEntity(stack, target, player);
		PogoWeaponItem.hitEntity(stack, target, player, pogoMotion);
		return true;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		return PogoWeaponItem.onItemUse(context.getPlayer(), context.getWorld(), context.getPos(), context.getItem(), context.getFace(), pogoMotion);
	}
}