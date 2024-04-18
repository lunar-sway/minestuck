package com.mraof.minestuck.api.alchemy.recipe.combination;

import com.mraof.minestuck.alchemy.recipe.RegularCombinationRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Used to datagen a regular combination recipe.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CombinationRecipeBuilder
{
	private final ItemStack output;
	private Ingredient input1, input2;
	private CombinationMode mode;
	private String suffix = "";
	
	private CombinationRecipeBuilder(ItemStack output)
	{
		this.output = Objects.requireNonNull(output);
	}
	
	public static CombinationRecipeBuilder of(ItemLike output)
	{
		return of(new ItemStack(output.asItem()));
	}
	
	public static CombinationRecipeBuilder of(ItemStack output)
	{
		return new CombinationRecipeBuilder(output);
	}
	
	public CombinationRecipeBuilder input(TagKey<Item> tag)
	{
		return input(Ingredient.of(tag));
	}
	
	public CombinationRecipeBuilder input(ItemLike item)
	{
		return input(Ingredient.of(item));
	}
	
	public CombinationRecipeBuilder input(Ingredient ingredient)
	{
		if(input1 == null)
			input1 = Objects.requireNonNull(ingredient);
		else if(input2 == null)
			input2 = Objects.requireNonNull(ingredient);
		else throw new IllegalStateException("Can't set more than two inputs");
		return this;
	}
	
	public CombinationRecipeBuilder namedInput(TagKey<Item> tag)
	{
		input(Ingredient.of(tag));
		return namedSource(tag.location().getPath());
	}
	
	public CombinationRecipeBuilder namedInput(ItemLike item)
	{
		input(Ingredient.of(item));
		return namedSource(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem())).getPath());
	}
	
	public CombinationRecipeBuilder namedSource(String str)
	{
		if(suffix.isEmpty())
			suffix = "_from_" + str;
		else suffix = suffix + "_and_" + str;
		return this;
	}
	
	public CombinationRecipeBuilder and()
	{
		return mode(CombinationMode.AND);
	}
	
	public CombinationRecipeBuilder or()
	{
		return mode(CombinationMode.OR);
	}
	
	public CombinationRecipeBuilder mode(CombinationMode mode)
	{
		if(this.mode == null)
			this.mode = mode;
		else throw new IllegalStateException("Can't set mode twice");
		return this;
	}
	
	public void build(RecipeOutput recipeOutput)
	{
		ResourceLocation name = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(output.getItem()));
		build(recipeOutput, new ResourceLocation(name.getNamespace(), name.getPath() + suffix));
	}
	
	public void buildFor(RecipeOutput recipeOutput, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(output.getItem()));
		build(recipeOutput, new ResourceLocation(modId, name.getPath() + suffix));
	}
	
	public void build(RecipeOutput recipeOutput, ResourceLocation id)
	{
		recipeOutput.accept(id.withPrefix("combinations/"), new RegularCombinationRecipe(input1, input2, mode, output), null);
	}
}
