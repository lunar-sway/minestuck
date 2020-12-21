package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ItemSourceGristCost extends GeneratedGristCost
{
	private final Item source;
	private final float multiplier;
	private final ImmutableGristSet addedCost;
	
	private ItemSourceGristCost(ResourceLocation id, Ingredient ingredient, Item source, float multiplier, GristSet addedCost, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.source = source;
		this.multiplier = multiplier;
		this.addedCost = addedCost.asImmutable();
	}
	
	private ItemSourceGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, GristSet cost)
	{
		super(id, ingredient, priority, cost);
		this.source = null;
		this.multiplier = 0;
		this.addedCost = null;
	}
	
	@Override
	protected GristSet generateCost(GenerationContext context)
	{
		GristSet cost = context.lookupCostFor(source);
		
		if(cost != null)
			return cost.copy().scale(multiplier, false).addGrist(addedCost);
		else return null;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.TAG_SOURCE_GRIST_COST;
	}
	
	public static class Serializer extends GeneratedCostSerializer<ItemSourceGristCost>
	{
		@Override
		protected ItemSourceGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			GristSet cost = GristSet.deserialize(JSONUtils.getJsonObject(json, "grist_cost"));
			float multiplier = json.has("multiplier") ? JSONUtils.getFloat(json, "multiplier") : 1;
			ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getString(json, "source"));
			Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
			return new ItemSourceGristCost(recipeId, ingredient, item, multiplier, cost, priority);
		}
		
		@Override
		protected ItemSourceGristCost create(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority, GristSet cost)
		{
			return new ItemSourceGristCost(recipeId, ingredient, priority, cost);
		}
	}
}