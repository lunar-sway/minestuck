package com.mraof.minestuck.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class IrradiatingFallbackRecipe extends IrradiatingRecipe
{
	protected final IRecipeType<? extends AbstractCookingRecipe> fallbackType;
	
	public IrradiatingFallbackRecipe(ResourceLocation idIn, IRecipeType<? extends AbstractCookingRecipe> fallbackType)
	{
		super(idIn, "", Ingredient.EMPTY, ItemStack.EMPTY, 0, 20);
		this.fallbackType = fallbackType;
	}
	
	@Override
	public boolean matches(IInventory inventory, World world)
	{
		return true;	//Only need to do a recipe search once in the getCookingRecipe()
	}
	
	@Override
	public Optional<? extends AbstractCookingRecipe> getCookingRecipe(IInventory inventory, World world)
	{
		return world.getRecipeManager().getRecipesFor(fallbackType, inventory, world).stream().filter(o -> !o.isSpecial()).findFirst();
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
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.IRRADIATING_FALLBACK;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<IrradiatingFallbackRecipe>
	{
		private static final ResourceLocation NAME = new ResourceLocation("minestuck", "irradiating_fallback");
		
		@Override
		public IrradiatingFallbackRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			IRecipeType<? extends AbstractCookingRecipe> fallbackType = deserializeRecipeType(json);
			return new IrradiatingFallbackRecipe(recipeId, fallbackType);
		}
		
		@Nullable
		@Override
		public IrradiatingFallbackRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
		{
			ResourceLocation typeName = buffer.readResourceLocation();
			IRecipeType<?> fallbackType = Registry.RECIPE_TYPE.getOptional(typeName).orElseThrow(() -> new IllegalArgumentException("Can not deserialize unknown Recipe type: " + typeName));
			return new IrradiatingFallbackRecipe(recipeId, (IRecipeType<? extends AbstractCookingRecipe>) fallbackType);
		}
		
		@Override
		public void toNetwork(PacketBuffer buffer, IrradiatingFallbackRecipe recipe)
		{
			buffer.writeResourceLocation(Objects.requireNonNull(Registry.RECIPE_TYPE.getKey(recipe.fallbackType)));
		}
	}
	
	private static IRecipeType<? extends AbstractCookingRecipe> deserializeRecipeType(JsonObject json)
	{
		String typeName = JSONUtils.getAsString(json, "fallback_type");
		IRecipeType<?> fallbackType = Registry.RECIPE_TYPE.getOptional(new ResourceLocation(typeName)).orElseThrow(() -> new JsonSyntaxException("Unknown recipe type '" + typeName + "'"));
		return (IRecipeType<? extends AbstractCookingRecipe>) fallbackType;
	}
}