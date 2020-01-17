package com.mraof.minestuck.item.foods;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Random;

public class SurpriseEmbryoItem extends Item {
	
	public SurpriseEmbryoItem(Properties properties)
	{
		super(properties);
	}

	protected void onItemUseFinish(ItemStack stack, World world, PlayerEntity player)
	{
		super.onItemUseFinish(stack, world, player);
		if(!player.world.isRemote)
		{
			Random ran = new Random();
			ItemStack[] items = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.EGG),
					new ItemStack(Blocks.DIRT), new ItemStack(Blocks.PUMPKIN), new ItemStack(Blocks.COBBLESTONE)};
			int num = ran.nextInt(items.length);
			player.inventory.addItemStackToInventory(items[num].copy());
			ITextComponent message = new TranslationTextComponent("item.surpriseEmbryo.message", items[num].getDisplayName());
			message.getStyle().setColor(TextFormatting.GOLD);
			player.sendMessage(message);
		}
	}
}