package com.mraof.minestuck.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Random;

public class SurpriseEmbryoItem extends ItemFood
{
	
	public SurpriseEmbryoItem(int healAmount, float saturationModifier, boolean meat, Properties builder)
	{
		super(healAmount, saturationModifier, meat, builder);
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		super.onFoodEaten(stack, world, player);
		if(!player.world.isRemote)
		{
			Random ran = new Random();
			ItemStack[] items = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.EGG),
					new ItemStack(Blocks.DIRT), new ItemStack(Blocks.PUMPKIN), new ItemStack(Blocks.COBBLESTONE)};
			int num = ran.nextInt(items.length);
			player.inventory.addItemStackToInventory(items[num].copy());
			ITextComponent message = new TextComponentTranslation("item.surpriseEmbryo.message", items[num].getDisplayName());
			message.getStyle().setColor(TextFormatting.GOLD);
			player.sendMessage(message);
		}
	}
}