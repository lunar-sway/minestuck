package com.mraof.minestuck.item;

import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
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
		this.addPropertyOverride(CaptchaCardItem.CONTENT_NAME, CaptchaCardItem.CONTENT);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(AlchemyRecipes.hasDecodedItem(stack))
		{
			ItemStack content = AlchemyRecipes.getDecodedItem(stack);
			
			if (!content.isEmpty())
				tooltip.add(new StringTextComponent("(").appendSibling(content.getDisplayName()).appendText(")"));
			else
				tooltip.add(new StringTextComponent("(").appendSibling(new TranslationTextComponent(getTranslationKey()+".invalid")).appendText(")"));
		} else
			tooltip.add(new StringTextComponent("(").appendSibling(new TranslationTextComponent(getTranslationKey()+".empty")).appendText(")"));
	}
}