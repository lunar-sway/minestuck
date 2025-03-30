package com.mraof.minestuck.data.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

import static com.mraof.minestuck.data.recipe.MinestuckRecipeProvider.has;

public final class CommonRecipes
{
	public static RecipeBuilder stairsRecipe(ItemLike stairsBlock, ItemLike sourceBlock)
	{
		return stairsRecipe(stairsBlock, sourceBlock, "has_" + id(sourceBlock).getPath());
	}
	
	public static RecipeBuilder stairsRecipe(ItemLike stairsBlock, ItemLike sourceBlock, String criterionName)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairsBlock, 4)
				.define('#', sourceBlock)
				.pattern("#  ")
				.pattern("## ")
				.pattern("###")
				.unlockedBy(criterionName, has(sourceBlock));
	}
	
	public static RecipeBuilder slabRecipe(ItemLike slabBlock, ItemLike sourceBlock)
	{
		return slabRecipe(slabBlock, sourceBlock, "has_" + id(sourceBlock).getPath());
	}
	
	public static RecipeBuilder slabRecipe(ItemLike slabBlock, ItemLike sourceBlock, String criterionName)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slabBlock, 6)
				.define('#', sourceBlock)
				.pattern("###")
				.unlockedBy(criterionName, has(sourceBlock));
	}
	
	public static RecipeBuilder wallRecipe(ItemLike wallBlock, ItemLike sourceBlock)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 6)
				.define('#', sourceBlock)
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static RecipeBuilder pressurePlateRecipe(ItemLike pressurePlateBlock, ItemLike sourceBlock)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pressurePlateBlock)
				.define('#', sourceBlock)
				.pattern("##")
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static RecipeBuilder buttonRecipe(ItemLike buttonBlock, ItemLike sourceBlock)
	{
		return ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, buttonBlock).requires(sourceBlock)
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static RecipeBuilder planksRecipe(ItemLike planksBlock, ItemLike sourceBlock)
	{
		return ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planksBlock, 4).requires(sourceBlock)
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static RecipeBuilder woodRecipe(ItemLike woodBlock, ItemLike sourceBlock)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,
				woodBlock, 3).define('#', sourceBlock).pattern("##").pattern("##")
				.group("bark").unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static RecipeBuilder bookshelfRecipe(ItemLike bookshelfBlock, ItemLike sourceBlock, ItemLike sourceBlockSlab)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, bookshelfBlock)
				.define('b', Items.BOOK).define('p',sourceBlock).define('s',sourceBlockSlab)
				.pattern("psp").pattern("bbb").pattern("psp")
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static RecipeBuilder ladderRecipe(ItemLike ladderBlock, ItemLike sourceBlock)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ladderBlock, 4)
				.define('s', Items.STICK).define('p', sourceBlock)
				.pattern("s s").pattern("sps").pattern("s s")
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static RecipeBuilder signRecipe(ItemLike signBlock, ItemLike sourceBlock)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, signBlock, 3)
				.define('s', Items.STICK).define('p', sourceBlock)
				.pattern("ppp").pattern("ppp").pattern(" s ")
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	public static RecipeBuilder hangingSignRecipe(ItemLike hangingSignBlock, ItemLike sourceBlock)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, hangingSignBlock, 6)
				.define('c', Items.CHAIN).define('w', sourceBlock)
				.pattern("c c").pattern("www").pattern("www")
				.unlockedBy("has_" + id(sourceBlock).getPath(), has(sourceBlock));
	}
	
	private static ResourceLocation id(ItemLike item)
	{
		return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem()));
	}
}
