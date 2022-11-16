package com.mraof.minestuck.item.foods;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public class SurpriseEmbryoItem extends Item
{
	
	public SurpriseEmbryoItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity player)
	{
		if(player instanceof Player && !player.level.isClientSide)
		{
			Random ran = new Random();
			ItemStack[] items = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.EGG),
					new ItemStack(Blocks.DIRT), new ItemStack(Blocks.PUMPKIN), new ItemStack(Blocks.COBBLESTONE)};
			int num = ran.nextInt(items.length);
			if(!((Player) player).getInventory().add(items[num].copy()))
				if(!level.isClientSide)
					((Player) player).drop(items[num].copy(), false);
			
			TranslatableComponent message = new TranslatableComponent(getDescriptionId() + ".message", items[num].getHoverName());
			message.withStyle(ChatFormatting.GOLD);
			player.sendMessage(message, Util.NIL_UUID);
		}
		return super.finishUsingItem(stack, level, player);
	}
}