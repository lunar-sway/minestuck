package com.mraof.minestuck.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RightClickMessageItem extends Item
{
	
	public RightClickMessageItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		if(worldIn.isRemote)
			playerIn.sendMessage(new TranslationTextComponent(getTranslationKey() + ".message"));
		playerIn.swing(handIn, true);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
