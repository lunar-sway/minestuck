package com.mraof.minestuck.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class IrradiatingFallbackRecipe extends IrradiatingRecipe
{
	protected final RecipeType<? extends AbstractCookingRecipe> fallbackType;
	
	public IrradiatingFallbackRecipe(ResourceLocation idIn, RecipeType<? extends AbstractCookingRecipe> fallbackType)
	{
		super(idIn, "", Ingredient.EMPTY, ItemStack.EMPTY, 0, 20);
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
		return MSRecipeTypes.IRRADIATING_FALLBACK;
	}
	
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<IrradiatingFallbackRecipe>
	{
		private static final ResourceLocation NAME = new ResourceLocation("minestuck", "irradiating_fallback");
		
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
			RecipeType<?> fallbackType = Registry.RECIPE_TYPE.getOptional(typeName).orElseThrow(() -> new IllegalArgumentException("Can not deserialize unknown Recipe type: " + typeName));
			return new IrradiatingFallbackRecipe(recipeId, (RecipeType<? extends AbstractCookingRecipe>) fallbackType);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, IrradiatingFallbackRecipe recipe)
		{
			buffer.writeResourceLocation(Objects.requireNonNull(Registry.RECIPE_TYPE.getKey(recipe.fallbackType)));
		}
	}
	
	private static RecipeType<? extends AbstractCookingRecipe> deserializeRecipeType(JsonObject json)
	{
		String typeName = GsonHelper.getAsString(json, "fallback_type");
		RecipeType<?> fallbackType = Registry.RECIPE_TYPE.getOptional(new ResourceLocation(typeName)).orElseThrow(() -> new JsonSyntaxException("Unknown recipe type '" + typeName + "'"));
		return (RecipeType<? extends AbstractCookingRecipe>) fallbackType;
	}
}