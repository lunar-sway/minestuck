package com.mraof.minestuck.item.block;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ExtraInfoBlockItem extends BlockItem
{
	public ExtraInfoBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		if(Screen.hasShiftDown())
			tooltip.add(Component.translatable(this.getDescriptionId() + ".additional_info"));
		else
			tooltip.add(Component.translatable("message.shift_for_more_info"));
	}
}
