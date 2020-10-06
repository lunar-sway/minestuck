package com.mraof.minestuck.item;

import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ShuntItem extends Item
{
	public ShuntItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(AlchemyHelper.hasDecodedItem(stack))
		{
			ItemStack content = AlchemyHelper.getDecodedItem(stack);
			
			if (!content.isEmpty())
				tooltip.add(new StringTextComponent("(").append(content.getDisplayName()).appendString(")"));
			else
				tooltip.add(new StringTextComponent("(").append(new TranslationTextComponent(getTranslationKey()+".invalid")).appendString(")"));
		} else
			tooltip.add(new StringTextComponent("(").append(new TranslationTextComponent(getTranslationKey()+".empty")).appendString(")"));
	}
}