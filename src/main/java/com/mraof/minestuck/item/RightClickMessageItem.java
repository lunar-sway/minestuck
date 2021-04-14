package com.mraof.minestuck.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RightClickMessageItem extends Item
{
	private final Type type;
	
	public enum Type {
		EIGHTBALL,
		DICE,
		DEFAULT
	}
	
	public RightClickMessageItem(Properties properties, Type type)
	{
		super(properties);
		this.type = type;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		if(worldIn.isRemote)
		{
			if(type == Type.EIGHTBALL)
			{
				int key = playerIn.getRNG().nextInt(20);
				IFormattableTextComponent message = new TranslationTextComponent("message.eightball." + key);
				playerIn.sendMessage(message.mergeStyle(TextFormatting.BLUE), Util.DUMMY_UUID);
			} else if(type == Type.DICE)
			{
				int key = playerIn.getRNG().nextInt(6);
				IFormattableTextComponent message = new TranslationTextComponent("message.dice." + key);
				playerIn.sendMessage(message.mergeStyle(TextFormatting.WHITE), Util.DUMMY_UUID);
			} else
			{
				playerIn.sendMessage(new TranslationTextComponent(getTranslationKey() + ".message"), Util.DUMMY_UUID); //default, creates message for that item under that item's "addExtra" in MSEnUsLang provider
			}
		}
		
		return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
	}
}
