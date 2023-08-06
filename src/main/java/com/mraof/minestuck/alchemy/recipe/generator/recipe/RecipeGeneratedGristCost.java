package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipeGeneratedGristCost extends GristCostRecipe
{
	@Nullable
	private RecipeGeneratedCostHandler handler;
	
	private RecipeGeneratedGristCost(ResourceLocation id, @Nullable RecipeGeneratedCostHandler handler)
	{
		super(id, Ingredient.EMPTY, -999999);	//Do not use Integer.MIN_VALUE as priority due to the risk of causing an underflow/overflow
		this.handler = handler;
	}
	
	void setHandler(RecipeGeneratedCostHandler handler)
	{
		this.handler = handler;
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown, @Nullable Level level)
	{
		return scaleToCountAndDurability(getCost(input.getItem()), input, shouldRoundDown);
	}
	
	@Override
	public boolean matches(Container inv, Level level)
	{
		return getCost(inv.getItem(0).getItem()) != null;
	}
	
	@Nullable
	private GristSet getCost(Item item)
	{
		if(handler != null)
			return handler.getMap().get(item);
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