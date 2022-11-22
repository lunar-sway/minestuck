package com.mraof.minestuck.item;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ShuntItem extends Item
{
	public ShuntItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn)
	{
		if(AlchemyHelper.hasDecodedItem(stack))
		{
			ItemStack content = AlchemyHelper.getDecodedItem(stack);
			
			if (!content.isEmpty())
				tooltip.add(new TextComponent("(").append(content.getHoverName()).append(")"));
			else
				tooltip.add(new TextComponent("(").append(new TranslatableComponent(getDescriptionId()+".invalid")).append(")"));
		} else
			tooltip.add(new TextComponent("(").append(new TranslatableComponent(getDescriptionId()+".empty")).append(")"));
	}
}