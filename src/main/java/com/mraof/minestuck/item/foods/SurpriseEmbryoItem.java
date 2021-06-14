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
	public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity player)
	{
		if(player instanceof PlayerEntity && !player.level.isClientSide)
		{
			Random ran = new Random();
			ItemStack[] items = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.EGG),
					new ItemStack(Blocks.DIRT), new ItemStack(Blocks.PUMPKIN), new ItemStack(Blocks.COBBLESTONE)};
			int num = ran.nextInt(items.length);
			if(!((PlayerEntity) player).inventory.add(items[num].copy()))
				if(!world.isClientSide)
					((PlayerEntity) player).drop(items[num].copy(), false);
			
			IFormattableTextComponent message = new TranslationTextComponent(getDescriptionId() + ".message", items[num].getHoverName());
			message.withStyle(TextFormatting.GOLD);
			player.sendMessage(message, Util.NIL_UUID);
		}
		return super.finishUsingItem(stack, world, player);
	}
}