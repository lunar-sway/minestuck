package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemDualWeapon extends ItemWeapon
{
	protected Item otherItem;
	
	public ItemDualWeapon(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Properties builder)
	{
		this(tier, attackDamageIn, attackSpeedIn, efficiency, null, builder);
	}
	
	public ItemDualWeapon(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, ItemDualWeapon otherItem, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, builder);
		if(otherItem != null)
		{
			this.otherItem = otherItem;
			otherItem.otherItem = this;
		}
	}
	
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		if(playerIn.isSneaking())
		{
			ItemStack newItem = new ItemStack(otherItem, itemStackIn.getCount(), itemStackIn.getTag());
			
			return new ActionResult<>(EnumActionResult.SUCCESS, newItem);
		}
		return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
	}
}