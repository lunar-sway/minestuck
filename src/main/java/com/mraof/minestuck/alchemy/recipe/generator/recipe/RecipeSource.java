package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public sealed interface RecipeSource
{
	Codec<RecipeSource> DISPATCH_CODEC = Type.CODEC.dispatch(RecipeSource::type, type -> type.sourceCodec.get());
	
	Collection<RecipeHolder<?>> findRecipes(RecipeManager recipeManager);
	
	Type type();
	
	enum Type implements StringRepresentable
	{
		RECIPE(() -> SingleRecipe.CODEC),
		RECIPE_SERIALIZER(() -> BySerializer.CODEC),
		RECIPE_TYPE(() -> ByType.CODEC);
		
		static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		
		private final Supplier<MapCodec<? extends RecipeSource>> sourceCodec;
		
		Type(Supplier<MapCodec<? extends RecipeSource>> sourceCodec)
		{
			this.sourceCodec = sourceCodec;
		}
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
	
	record SingleRecipe(ResourceLocation recipe) implements RecipeSource
	{
		static final MapCodec<SingleRecipe> CODEC = ResourceLocation.CODEC.fieldOf("recipe")
				.xmap(SingleRecipe::new, SingleRecipe::recipe);
		
		@Override
		public Collection<RecipeHolder<?>> findRecipes(RecipeManager recipeManager)
		{
			Optional<? extends RecipeHolder<?>> recipe = recipeManager.byKey(this.recipe);
			return recipe.map(Collections::<RecipeHolder<?>>singleton).orElse(Collections.emptySet());
		}
		
		@Override
		public Type type()
		{
			return Type.RECIPE;
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[recipe=" + recipe + "]";
		}
	}
	
	record BySerializer(RecipeSerializer<?> serializer) implements RecipeSource
	{
		static final MapCodec<BySerializer> CODEC = BuiltInRegistries.RECIPE_SERIALIZER.byNameCodec().fieldOf("serializer")
				.xmap(BySerializer::new, BySerializer::serializer);
		
		@Override
		public Collection<RecipeHolder<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(recipeHolder -> recipeHolder.value().getSerializer() == serializer).toList();
		}
		
		@Override
		public Type type()
		{
			return Type.RECIPE_SERIALIZER;
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[serializer=" + BuiltInRegistries.RECIPE_SERIALIZER.getKey(serializer) + "]";
		}
	}
	
	record ByType(RecipeType<?> recipeType) implements RecipeSource
	{
		static final MapCodec<ByType> CODEC = BuiltInRegistries.RECIPE_TYPE.byNameCodec().fieldOf("recipe_type")
				.xmap(ByType::new, ByType::recipeType);
		
		@Override
		public List<RecipeHolder<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(recipeHolder -> recipeHolder.value().getType() == recipeType).toList();
		}
		
		@Override
		public Type type()
		{
			return Type.RECIPE_TYPE;
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[type=" + recipeType + "]";
		}
	}
}
