package com.mraof.minestuck.data.recipe;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

import static com.mraof.minestuck.data.recipe.MinestuckRecipeProvider.has;

public final class CommonRecipes
{
	public static ShapedRecipeBuilder stairsShapedRecipe(Supplier<? extends ItemLike> stairsBlock, Supplier<? extends ItemLike> sourceBlock)
	{
		return stairsShapedRecipe(stairsBlock.get(), sourceBlock.get());
	}
	
	public static ShapedRecipeBuilder stairsShapedRecipe(Supplier<? extends ItemLike> stairsBlock, Supplier<? extends ItemLike> sourceBlock, String criterionName)
	{
		return stairsShapedRecipe(stairsBlock.get(), sourceBlock.get(), criterionName);
	}
	
	public static ShapedRecipeBuilder stairsShapedRecipe(ItemLike stairsBlock, ItemLike sourceBlock)
	{
		ResourceLocation blockId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(sourceBlock.asItem()));
		String blockName = blockId.getPath();
		return stairsShapedRecipe(stairsBlock, sourceBlock, "has_" + blockName);
	}
	
	public static ShapedRecipeBuilder stairsShapedRecipe(ItemLike stairsBlock, ItemLike sourceBlock, String criterionName)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairsBlock, 4)
				.define('#', sourceBlock)
				.pattern("#  ")
				.pattern("## ")
				.pattern("###")
				.unlockedBy(criterionName, has(sourceBlock));
	}
}
