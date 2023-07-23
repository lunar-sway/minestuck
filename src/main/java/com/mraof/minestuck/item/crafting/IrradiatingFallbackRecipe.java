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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class IrradiatingFallbackRecipe extends IrradiatingRecipe
{
	protected final RecipeType<? extends AbstractCookingRecipe> fallbackType;
	
	public IrradiatingFallbackRecipe(ResourceLocation idIn, RecipeType<? extends AbstractCookingRecipe> fallbackType)
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
		return level.getRecipeManager().getRecipesFor(fallbackType, container, level).stream().filter(o -> !o.isSpecial()).findFirst();
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
			RecipeType<? extends AbstractCookingRecipe> fallbackType = deserializeRecipeType(json);
			return new IrradiatingFallbackRecipe(recipeId, fallbackType);
		}
		
		@Nullable
		@Override
		public IrradiatingFallbackRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			ResourceLocation typeName = buffer.readResourceLocation();
			RecipeType<?> fallbackType = ForgeRegistries.RECIPE_TYPES.getValue(typeName);
			if(fallbackType == null)
				throw new IllegalArgumentException("Can not deserialize unknown Recipe type: " + typeName);
			return new IrradiatingFallbackRecipe(recipeId, (RecipeType<? extends AbstractCookingRecipe>) fallbackType);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, IrradiatingFallbackRecipe recipe)
		{
			buffer.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.RECIPE_TYPES.getKey(recipe.fallbackType)));
		}
	}
	
	private static RecipeType<? extends AbstractCookingRecipe> deserializeRecipeType(JsonObject json)
	{
		String typeName = GsonHelper.getAsString(json, "fallback_type");
		RecipeType<?> fallbackType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(typeName));
		if(fallbackType == null)
			throw new JsonSyntaxException("Unknown recipe type '" + typeName + "'");
		return (RecipeType<? extends AbstractCookingRecipe>) fallbackType;
	}
}