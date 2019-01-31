package com.mraof.minestuck.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemStrifeCard extends Item
{
	public ItemStrifeCard()
	{
		setUnlocalizedName("strifeCard");
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		// TODO Auto-generated method stub
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
