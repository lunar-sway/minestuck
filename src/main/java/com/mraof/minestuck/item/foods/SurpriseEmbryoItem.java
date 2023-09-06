package com.mraof.minestuck.item.foods;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class SurpriseEmbryoItem extends Item
{
	
	public SurpriseEmbryoItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity player)
	{
		if(player instanceof Player && !player.level().isClientSide)
		{
			ItemStack[] items = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.EGG),
					new ItemStack(Blocks.DIRT), new ItemStack(Blocks.PUMPKIN), new ItemStack(Blocks.COBBLESTONE)};
			int num = level.random.nextInt(items.length);
			if(!((Player) player).getInventory().add(items[num].copy()))
				if(!level.isClientSide)
					((Player) player).drop(items[num].copy(), false);
			
			player.sendSystemMessage(Component.translatable(getDescriptionId() + ".message", items[num].getHoverName()).withStyle(ChatFormatting.GOLD));
		}
		return super.finishUsingItem(stack, level, player);
	}
}