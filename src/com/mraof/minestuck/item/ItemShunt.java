package com.mraof.minestuck.item;

import java.util.List;

import javax.annotation.Nullable;

import com.mraof.minestuck.alchemy.AlchemyRecipes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemShunt extends Item
{
	
	public ItemShunt(Properties properties)
	{
		super(properties);
		this.addPropertyOverride(CaptchaCardItem.CONTENT_NAME, CaptchaCardItem.CONTENT);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(AlchemyRecipes.hasDecodedItem(stack))
		{
			ItemStack content = AlchemyRecipes.getDecodedItem(stack);
			
			if (!content.isEmpty())
				tooltip.add(new TextComponentString("(").appendSibling(content.getDisplayName()).appendText(")"));
			else
				tooltip.add(new TextComponentString("(").appendSibling(new TextComponentTranslation("item.shunt.invalid")).appendText(")"));
		} else
			tooltip.add(new TextComponentString("(").appendSibling(new TextComponentTranslation("item.shunt.empty")).appendText(")"));
	}
}