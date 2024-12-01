package com.mraof.minestuck.item;

import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ShuntItem extends Item
{
	public ShuntItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		EncodedItemComponent encodedItemComponent = stack.get(MSItemComponents.ENCODED_ITEM);
		if(encodedItemComponent != null)
		{
			ItemStack content = encodedItemComponent.asItemStack();
			
			if (!content.isEmpty())
				tooltip.add(Component.literal("(").append(content.getHoverName()).append(")"));
			else
				tooltip.add(Component.literal("(").append(Component.translatable(getDescriptionId()+".invalid")).append(")"));
		} else
			tooltip.add(Component.literal("(").append(Component.translatable(getDescriptionId()+".empty")).append(")"));
	}
}
