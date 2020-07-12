package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class DualWeaponItem extends WeaponItem
{
	private final Supplier<Item> otherItem;
	
	public DualWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Supplier<Item> otherItem, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
		this.otherItem = otherItem;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		if(playerIn.isSneaking())
		{
			ItemStack newItem = new ItemStack(otherItem.get(), itemStackIn.getCount());
			newItem.setTag(itemStackIn.getTag());
			
			return new ActionResult<>(ActionResultType.SUCCESS, newItem);
		}
		return new ActionResult<>(ActionResultType.PASS, itemStackIn);
	}
}