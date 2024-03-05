package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public sealed interface RecipeSource
{
	Collection<Recipe<?>> findRecipes(RecipeManager recipeManager);
	
	record SingleRecipe(ResourceLocation recipe) implements RecipeSource
	{
		@Override
		public Collection<Recipe<?>> findRecipes(RecipeManager recipeManager)
		{
			Optional<? extends Recipe<?>> recipe = recipeManager.byKey(this.recipe);
			return recipe.map(Collections::<Recipe<?>>singleton).orElse(Collections.emptySet());
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[recipe=" + recipe + "]";
		}
	}
	
	record BySerializer(RecipeSerializer<?> serializer) implements RecipeSource
	{
		@Override
		public Collection<Recipe<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(iRecipe -> iRecipe.getSerializer() == serializer).toList();
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[serializer=" + ForgeRegistries.RECIPE_SERIALIZERS.getKey(serializer) + "]";
		}
	}
	
	record ByType(RecipeType<?> recipeType) implements RecipeSource
	{
		@Override
		public List<Recipe<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(iRecipe -> iRecipe.getType() == recipeType).toList();
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[type=" + recipeType + "]";
		}
	}
}
