package com.mraof.minestuck.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class IrradiatingFallbackRecipe extends IrradiatingRecipe
{
	protected final RecipeType<?> fallbackType;
	
	public IrradiatingFallbackRecipe(ResourceLocation idIn, RecipeType<?> fallbackType)
	{
		super(idIn, "", CookingBookCategory.MISC, Ingredient.EMPTY, ItemStack.EMPTY, 0, 20);
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
				.filter(recipe -> recipe.getType() == this.fallbackType)
				.flatMap(recipe -> {
					if(recipe instanceof AbstractCookingRecipe cookingRecipe)
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
		@Override
		public IrradiatingFallbackRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			RecipeType<?> fallbackType = deserializeRecipeType(json);
			return new IrradiatingFallbackRecipe(recipeId, fallbackType);
		}
		
		@Override
		public IrradiatingFallbackRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			ResourceLocation typeName = buffer.readResourceLocation();
			RecipeType<?> fallbackType = ForgeRegistries.RECIPE_TYPES.getValue(typeName);
			if(fallbackType == null)
				throw new IllegalArgumentException("Can not deserialize unknown Recipe type: " + typeName);
			return new IrradiatingFallbackRecipe(recipeId, fallbackType);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, IrradiatingFallbackRecipe recipe)
		{
			buffer.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.RECIPE_TYPES.getKey(recipe.fallbackType)));
		}
	}
	
	private static RecipeType<?> deserializeRecipeType(JsonObject json)
	{
		String typeName = GsonHelper.getAsString(json, "fallback_type");
		RecipeType<?> fallbackType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(typeName));
		if(fallbackType == null)
			throw new JsonSyntaxException("Unknown recipe type '" + typeName + "'");
		return fallbackType;
	}
}