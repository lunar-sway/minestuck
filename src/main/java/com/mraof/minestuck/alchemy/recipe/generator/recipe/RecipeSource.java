package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public sealed interface RecipeSource
{
	Codec<RecipeSource> DISPATCH_CODEC = Type.CODEC.dispatch(RecipeSource::type, type -> type.sourceCodec.get());
	
	Collection<Recipe<?>> findRecipes(RecipeManager recipeManager);
	
	Type type();
	
	enum Type implements StringRepresentable
	{
		RECIPE(() -> SingleRecipe.CODEC),
		RECIPE_SERIALIZER(() -> BySerializer.CODEC),
		RECIPE_TYPE(() -> ByType.CODEC);
		
		static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		
		private final Supplier<Codec<? extends RecipeSource>> sourceCodec;
		
		Type(Supplier<Codec<? extends RecipeSource>> sourceCodec)
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
		static final Codec<SingleRecipe> CODEC = ResourceLocation.CODEC.fieldOf("recipe")
				.xmap(SingleRecipe::new, SingleRecipe::recipe).codec();
		
		@Override
		public Collection<Recipe<?>> findRecipes(RecipeManager recipeManager)
		{
			Optional<? extends Recipe<?>> recipe = recipeManager.byKey(this.recipe);
			return recipe.map(Collections::<Recipe<?>>singleton).orElse(Collections.emptySet());
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
		static final Codec<BySerializer> CODEC = ForgeRegistries.RECIPE_SERIALIZERS.getCodec().fieldOf("serializer")
				.xmap(BySerializer::new, BySerializer::serializer).codec();
		
		@Override
		public Collection<Recipe<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(iRecipe -> iRecipe.getSerializer() == serializer).toList();
		}
		
		@Override
		public Type type()
		{
			return Type.RECIPE_SERIALIZER;
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[serializer=" + ForgeRegistries.RECIPE_SERIALIZERS.getKey(serializer) + "]";
		}
	}
	
	record ByType(RecipeType<?> recipeType) implements RecipeSource
	{
		static final Codec<ByType> CODEC = ForgeRegistries.RECIPE_TYPES.getCodec().fieldOf("recipe_type")
				.xmap(ByType::new, ByType::recipeType).codec();
		
		@Override
		public List<Recipe<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(iRecipe -> iRecipe.getType() == recipeType).toList();
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
