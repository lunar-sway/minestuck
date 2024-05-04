package com.mraof.minestuck.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
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
	public boolean matches(Container inventory, Level level)
	{
		return true;	//Only need to do a recipe search once in the getCookingRecipe()
	}
	
	@Override
	public Optional<? extends AbstractCookingRecipe> getCookingRecipe(Container container, Level level)
	{
		RecipeManager recipeManager = level.getRecipeManager();
		return getFallbackRecipes(recipeManager)
				.filter(recipe -> recipe.matches(container, level))
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
		private static final Codec<IrradiatingFallbackRecipe> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						BuiltInRegistries.RECIPE_TYPE.byNameCodec().fieldOf("fallback_type").forGetter(recipe -> recipe.fallbackType)
				).apply(instance, IrradiatingFallbackRecipe::new));
		
		@Override
		public Codec<IrradiatingFallbackRecipe> codec()
		{
			return CODEC;
		}
		
		@Override
		public IrradiatingFallbackRecipe fromNetwork(FriendlyByteBuf buffer)
		{
			ResourceLocation typeName = buffer.readResourceLocation();
			RecipeType<?> fallbackType = BuiltInRegistries.RECIPE_TYPE.get(typeName);
			if(fallbackType == null)
				throw new IllegalArgumentException("Can not deserialize unknown Recipe type: " + typeName);
			return new IrradiatingFallbackRecipe(fallbackType);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, IrradiatingFallbackRecipe recipe)
		{
			buffer.writeResourceLocation(Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(recipe.fallbackType)));
		}
	}
}