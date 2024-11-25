package com.mraof.minestuck.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IrradiatingFallbackRecipe extends IrradiatingRecipe
{
	protected final RecipeType<?> fallbackType;
	
	public IrradiatingFallbackRecipe(RecipeType<?> fallbackType)
	{
		super("", CookingBookCategory.MISC, Ingredient.EMPTY, ItemStack.EMPTY, 0, 20);
		this.fallbackType = fallbackType;
	}
	
	@Override
	public boolean matches(SingleRecipeInput input, Level level)
	{
		return true;	//Only need to do a recipe search once in the getCookingRecipe()
	}
	
	@Override
	public Optional<? extends AbstractCookingRecipe> getCookingRecipe(SingleRecipeInput input, Level level)
	{
		RecipeManager recipeManager = level.getRecipeManager();
		return getFallbackRecipes(recipeManager)
				.filter(recipe -> recipe.matches(input, level))
				.findFirst();
	}
	
	private Stream<AbstractCookingRecipe> getFallbackRecipes(RecipeManager recipeManager)
	{
		return recipeManager.getRecipes().stream()
				.filter(recipe -> recipe.value().getType() == this.fallbackType)
				.flatMap(recipe -> {
					if(recipe.value() instanceof AbstractCookingRecipe cookingRecipe)
						return Stream.of(cookingRecipe);
					else
						return Stream.empty();
				})
				.filter(o -> !o.isSpecial());
	}
	
	@Override
	public boolean isFallback()
	{
		return true;
	}
	
	@Override
	public boolean isSpecial()
	{
		return true;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.IRRADIATING_FALLBACK.get();
	}
	
	public static class Serializer implements RecipeSerializer<IrradiatingFallbackRecipe>
	{
		private static final MapCodec<IrradiatingFallbackRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
				instance.group(
						BuiltInRegistries.RECIPE_TYPE.byNameCodec().fieldOf("fallback_type").forGetter(recipe -> recipe.fallbackType)
				).apply(instance, IrradiatingFallbackRecipe::new));
		private static final StreamCodec<RegistryFriendlyByteBuf, IrradiatingFallbackRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
		
		@Override
		public MapCodec<IrradiatingFallbackRecipe> codec()
		{
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, IrradiatingFallbackRecipe> streamCodec()
		{
			return STREAM_CODEC;
		}
		
		private static IrradiatingFallbackRecipe fromNetwork(FriendlyByteBuf buffer)
		{
			ResourceLocation typeName = buffer.readResourceLocation();
			RecipeType<?> fallbackType = BuiltInRegistries.RECIPE_TYPE.get(typeName);
			if(fallbackType == null)
				throw new IllegalArgumentException("Can not deserialize unknown Recipe type: " + typeName);
			return new IrradiatingFallbackRecipe(fallbackType);
		}
		
		private static void toNetwork(FriendlyByteBuf buffer, IrradiatingFallbackRecipe recipe)
		{
			buffer.writeResourceLocation(Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(recipe.fallbackType)));
		}
	}
}
