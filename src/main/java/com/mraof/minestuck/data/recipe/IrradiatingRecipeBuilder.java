package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.item.crafting.IrradiatingRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class IrradiatingRecipeBuilder implements RecipeBuilder
{
	private final Item result;
	private final Ingredient ingredient;
	private final float experience;
	private final int cookingTime;
	
	public IrradiatingRecipeBuilder(ItemLike result, Ingredient ingredient, float experience, int cookingTime)
	{
		this.result = result.asItem();
		this.ingredient = ingredient;
		this.experience = experience;
		this.cookingTime = cookingTime;
	}
	
	public static IrradiatingRecipeBuilder irradiating(Ingredient ingredient, ItemLike result, float experience, int cookingTime)
	{
		return new IrradiatingRecipeBuilder(result, ingredient, experience, cookingTime);
	}
	
	@Override
	public RecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RecipeBuilder group(@Nullable String group)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Item getResult()
	{
		return result;
	}
	
	@Override
	public void save(RecipeOutput output, ResourceLocation recipeId)
	{
		output.accept(recipeId, new IrradiatingRecipe("", CookingBookCategory.MISC, this.ingredient, this.result.getDefaultInstance(), this.experience, this.cookingTime), null);
	}
}
