package com.mraof.minestuck.entity.consort;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record BoondollarPriceRecipe(Ingredient ingredient, IntProvider priceRange)
{
	public static final Codec<BoondollarPriceRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(BoondollarPriceRecipe::ingredient),
			IntProvider.CODEC.fieldOf("range").forGetter(BoondollarPriceRecipe::priceRange)
	).apply(instance, BoondollarPriceRecipe::new));
	
	public int generatePrice(RandomSource random)
	{
		return priceRange.sample(random);
	}
	
	public boolean appliesTo(ItemStack stack)
	{
		return ingredient.test(stack);
	}
}
