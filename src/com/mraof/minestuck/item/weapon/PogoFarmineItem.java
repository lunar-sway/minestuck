package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;

public class PogoFarmineItem extends FarmineItem
{
	private double pogoMotion;
	
	public PogoFarmineItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, int radius, int terminus, double pogoMotion, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, radius, terminus, builder);
		this.pogoMotion = pogoMotion;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		super.hitEntity(stack, target, player);
		PogoWeaponItem.hitEntity(stack, target, player, pogoMotion);
		return true;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemUseContext context)
	{
		return PogoWeaponItem.onItemUse(context.getPlayer(), context.getWorld(), context.getPos(), context.getItem(), context.getFace(), pogoMotion);
	}
}