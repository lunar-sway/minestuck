package com.mraof.minestuck.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AncientThumbDrive extends Item
{
	public AncientThumbDrive(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
	{
		if(Screen.hasShiftDown())
			tooltipComponents.add(Component.translatable(getDescriptionId() + ".desc"));
		else
			tooltipComponents.add(Component.translatable(getDescriptionId() + ".press_shift"));
	}
}


