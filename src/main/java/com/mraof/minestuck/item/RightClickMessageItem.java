package com.mraof.minestuck.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
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
		{
			if(playerIn.getHeldItem(handIn).getItem() == MSItems.EIGHTBALL)
			{
				int key = playerIn.getRNG().nextInt(20);
				ITextComponent message = new TranslationTextComponent("message.eightball." + key);
				playerIn.sendMessage(message.applyTextStyle(TextFormatting.BLUE));
			} else if(playerIn.getHeldItem(handIn).getItem() == MSItems.DICE)
			{
				int key = playerIn.getRNG().nextInt(6);
				ITextComponent message = new TranslationTextComponent("message.dice." + key);
				playerIn.sendMessage(message.applyTextStyle(TextFormatting.WHITE));
			} else
			{
				playerIn.sendMessage(new TranslationTextComponent(getTranslationKey() + ".message")); //default, if you
			}
		}
		
		playerIn.swing(handIn, true);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
