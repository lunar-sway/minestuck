package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
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
		public static final Codec<SingleRecipe> CODEC = ResourceLocation.CODEC.fieldOf("recipe")
				.xmap(SingleRecipe::new, SingleRecipe::recipe).codec();
		
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
		public static final Codec<BySerializer> CODEC = ForgeRegistries.RECIPE_SERIALIZERS.getCodec().fieldOf("serializer")
				.xmap(BySerializer::new, BySerializer::serializer).codec();
		
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
		public static final Codec<ByType> CODEC = ForgeRegistries.RECIPE_TYPES.getCodec().fieldOf("recipe_type")
				.xmap(ByType::new, ByType::recipeType).codec();
		
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
