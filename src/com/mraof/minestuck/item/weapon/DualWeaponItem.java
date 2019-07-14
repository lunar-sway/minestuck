package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DualWeaponItem extends WeaponItem
{
	protected Item otherItem;
	
	public DualWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Properties builder)
	{
		this(tier, attackDamageIn, attackSpeedIn, efficiency, null, builder);
	}
	
	public DualWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, DualWeaponItem otherItem, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, builder);
		if(otherItem != null)
		{
			this.otherItem = otherItem;
			otherItem.otherItem = this;
		}
	}
	
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		if(playerIn.isSneaking())
		{
			ItemStack newItem = new ItemStack(otherItem, itemStackIn.getCount(), itemStackIn.getTag());
			
			return new ActionResult<>(ActionResultType.SUCCESS, newItem);
		}
		return new ActionResult<>(ActionResultType.PASS, itemStackIn);
	}
}