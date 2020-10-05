package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class TagSourceGristCost extends GeneratedGristCost
{
	private final ITag<Item> source;
	private final float multiplier;
	private final ImmutableGristSet addedCost;
	
	private TagSourceGristCost(ResourceLocation id, Ingredient ingredient, ITag<Item> source, float multiplier, GristSet addedCost, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.source = source;
		this.multiplier = multiplier;
		this.addedCost = addedCost.asImmutable();
	}
	
	private TagSourceGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, GristSet cost)
	{
		super(id, ingredient, priority, cost);
		this.source = null;
		this.multiplier = 0;
		this.addedCost = null;
	}
	
	@Override
	protected GristSet generateCost(GenerationContext context)
	{
		GristSet maxCost = null;
		for(Item item : source.getAllElements())
		{
			GristSet cost = context.lookupCostFor(item);
			
			if(cost != null && (maxCost == null || cost.getValue() > maxCost.getValue()))
				maxCost = cost;
		}
		
		if(maxCost != null)
		{
			return maxCost.copy().scale(multiplier, false).addGrist(addedCost);
		} else return null;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.TAG_SOURCE_GRIST_COST;
	}
	
	public static class Serializer extends GeneratedCostSerializer<TagSourceGristCost>
	{
		@Override
		protected TagSourceGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			GristSet cost = GristSet.deserialize(JSONUtils.getJsonObject(json, "grist_cost"));
			float multiplier = json.has("multiplier") ? JSONUtils.getFloat(json, "multiplier") : 1;
			ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getString(json, "source"));
			ITag<Item> tag = ItemTags.getCollection().get(resourcelocation);
			return new TagSourceGristCost(recipeId, ingredient, tag, multiplier, cost, priority);
		}
		
		@Override
		protected TagSourceGristCost create(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority, GristSet cost)
		{
			return new TagSourceGristCost(recipeId, ingredient, priority, cost);
		}
	}
}