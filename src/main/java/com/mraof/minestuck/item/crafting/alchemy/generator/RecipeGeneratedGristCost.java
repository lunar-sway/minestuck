package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

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
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, World world)
	{
		return getCost(input.getItem());
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn)
	{
		return getCost(inv.getStackInSlot(0).getItem()) != null;
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
	public List<JeiGristCost> getJeiCosts(World world)
	{
		if(handler != null)
			return handler.createJeiCosts();
		else return Collections.emptyList();
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.RECIPE_GRIST_COST;
	}
	
	@Override
	public boolean isDynamic()
	{
		return true;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeGeneratedGristCost>
	{
		@Override
		public RecipeGeneratedGristCost read(ResourceLocation recipeId, JsonObject json)
		{
			return new RecipeGeneratedGristCost(recipeId, null);
		}
		
		@Override
		public RecipeGeneratedGristCost read(ResourceLocation recipeId, PacketBuffer buffer)
		{
			return new RecipeGeneratedGristCost(recipeId, RecipeGeneratedCostHandler.read(buffer));
		}
		
		@Override
		public void write(PacketBuffer buffer, RecipeGeneratedGristCost recipe)
		{
			if(recipe.handler != null)
				recipe.handler.write(buffer);
		}
	}
}