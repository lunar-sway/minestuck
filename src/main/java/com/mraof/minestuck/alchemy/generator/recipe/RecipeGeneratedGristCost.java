package com.mraof.minestuck.alchemy.generator.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.alchemy.GristCostRecipe;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.generator.GeneratedCostProvider;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class RecipeGeneratedGristCost extends GristCostRecipe
{
	private RecipeGeneratedCostHandler handler;
	
	private RecipeGeneratedGristCost(ResourceLocation id, RecipeGeneratedCostHandler handler)
	{
		super(id, null, -999999);	//Do not use Integer.MIN_VALUE as priority due to the risk of causing an underflow/overflow
		this.handler = handler;
	}
	
	void setHandler(RecipeGeneratedCostHandler handler)
	{
		this.handler = handler;
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, Level level)
	{
		return scaleToCountAndDurability(getCost(input.getItem()), input, shouldRoundDown);
	}
	
	@Override
	public boolean matches(Container inv, Level level)
	{
		return getCost(inv.getItem(0).getItem()) != null;
	}
	
	private GristSet getCost(Item item)
	{
		if(handler != null)
			return handler.getGristCost(item);
		else return null;
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		if(handler != null)
			handler.addAsProvider(consumer);
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return NonNullList.create();
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		if(handler != null)
			return handler.createJeiCosts();
		else return Collections.emptyList();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.RECIPE_GRIST_COST.get();
	}
	
	@Override
	public boolean isSpecial()
	{
		return true;
	}
	
	public static class Serializer implements RecipeSerializer<RecipeGeneratedGristCost>
	{
		@Override
		public RecipeGeneratedGristCost fromJson(ResourceLocation recipeId, JsonObject json)
		{
			return new RecipeGeneratedGristCost(recipeId, null);
		}
		
		@Override
		public RecipeGeneratedGristCost fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			return new RecipeGeneratedGristCost(recipeId, RecipeGeneratedCostHandler.read(buffer));
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, RecipeGeneratedGristCost recipe)
		{
			if(recipe.handler != null)
				recipe.handler.write(buffer);
		}
	}
}