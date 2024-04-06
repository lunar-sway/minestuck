package com.mraof.minestuck.data.recipe;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
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
		return stairsShapedRecipe(stairsBlock, sourceBlock, "has_" + id(sourceBlock).getPath());
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
	
	public static ShapedRecipeBuilder slabShapedRecipe(Supplier<? extends ItemLike> slabBlock, Supplier<? extends ItemLike> sourceBlock)
	{
		return slabShapedRecipe(slabBlock.get(), sourceBlock.get());
	}
	
	public static ShapedRecipeBuilder slabShapedRecipe(Supplier<? extends ItemLike> slabBlock, Supplier<? extends ItemLike> sourceBlock, String criterionName)
	{
		return slabShapedRecipe(slabBlock.get(), sourceBlock.get(), criterionName);
	}
	
	public static ShapedRecipeBuilder slabShapedRecipe(ItemLike slabBlock, ItemLike sourceBlock)
	{
		return slabShapedRecipe(slabBlock, sourceBlock, "has_" + id(sourceBlock).getPath());
	}
	
	public static ShapedRecipeBuilder slabShapedRecipe(ItemLike slabBlock, ItemLike sourceBlock, String criterionName)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slabBlock, 6)
				.define('#', sourceBlock)
				.pattern("###")
				.unlockedBy(criterionName, has(sourceBlock));
	}
	
	public static ShapedRecipeBuilder wallShapedRecipe(Supplier<? extends ItemLike> wallBlock, Supplier<? extends ItemLike> sourceBlock)
	{
		return wallShapedRecipe(wallBlock.get(), sourceBlock.get());
	}
	
	public static ShapedRecipeBuilder wallShapedRecipe(ItemLike wallBlock, ItemLike sourceBlock)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 6)
				.define('#', sourceBlock)
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static ShapedRecipeBuilder pressurePlateShapedRecipe(Supplier<? extends ItemLike> pressurePlateBlock, Supplier<? extends ItemLike> sourceBlock)
	{
		return pressurePlateShapedRecipe(pressurePlateBlock.get(), sourceBlock.get());
	}
	
	public static ShapedRecipeBuilder pressurePlateShapedRecipe(ItemLike pressurePlateBlock, ItemLike sourceBlock)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pressurePlateBlock)
				.define('#', sourceBlock)
				.pattern("##")
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static ShapelessRecipeBuilder buttonShapelessRecipe(Supplier<? extends ItemLike> buttonBlock, Supplier<? extends ItemLike> sourceBlock)
	{
		return buttonShapelessRecipe(buttonBlock.get(), sourceBlock.get());
	}
	
	public static ShapelessRecipeBuilder buttonShapelessRecipe(ItemLike buttonBlock, ItemLike sourceBlock)
	{
		return ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, buttonBlock).requires(sourceBlock)
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	private static ResourceLocation id(ItemLike item)
	{
		return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.asItem()));
	}
}
