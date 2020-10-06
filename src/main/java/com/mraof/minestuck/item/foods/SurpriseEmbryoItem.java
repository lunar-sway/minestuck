package com.mraof.minestuck.item.foods;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Random;

public class SurpriseEmbryoItem extends Item
{
	
	public SurpriseEmbryoItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity player)
	{
		if(player instanceof PlayerEntity && !player.world.isRemote)
		{
			Random ran = new Random();
			ItemStack[] items = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.EGG),
					new ItemStack(Blocks.DIRT), new ItemStack(Blocks.PUMPKIN), new ItemStack(Blocks.COBBLESTONE)};
			int num = ran.nextInt(items.length);
			if(!((PlayerEntity) player).inventory.addItemStackToInventory(items[num].copy()))
				if(!world.isRemote)
					((PlayerEntity) player).dropItem(items[num].copy(), false);
			
			IFormattableTextComponent message = new TranslationTextComponent(getTranslationKey() + ".message", items[num].getDisplayName());
			message.mergeStyle(TextFormatting.GOLD);
			player.sendMessage(message, Util.DUMMY_UUID);
		}
		return super.onItemUseFinish(stack, world, player);
	}
}