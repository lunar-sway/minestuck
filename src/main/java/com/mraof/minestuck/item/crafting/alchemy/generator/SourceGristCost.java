package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SourceGristCost extends GeneratedGristCost
{
	private final List<Source> sources;
	private final float multiplier;
	private final ImmutableGristSet addedCost;
	
	private SourceGristCost(ResourceLocation id, Ingredient ingredient, List<Source> sources, float multiplier, GristSet addedCost, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.sources = sources;
		this.multiplier = multiplier;
		this.addedCost = addedCost.asImmutable();
	}
	
	private SourceGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, GristSet cost)
	{
		super(id, ingredient, priority, cost);
		this.sources = null;
		this.multiplier = 0;
		this.addedCost = null;
	}
	
	@Override
	protected GristSet generateCost(GenerationContext context)
	{
		GristSet costSum = new GristSet();
		for(Source source : sources)
		{
			GristSet sourceCost = source.getCostFor(context);
			if(sourceCost != null)
				costSum.addGrist(sourceCost);
			else return null;
		}
		
		return costSum.scale(multiplier, false).addGrist(addedCost);
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.SOURCE_GRIST_COST;
	}
	
	public static class Serializer extends GeneratedCostSerializer<SourceGristCost>
	{
		@Override
		protected SourceGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			GristSet cost = GristSet.deserialize(JSONUtils.getAsJsonObject(json, "grist_cost"));
			float multiplier = json.has("multiplier") ? JSONUtils.getAsFloat(json, "multiplier") : 1;
			
			JsonArray jsonList = JSONUtils.getAsJsonArray(json, "sources");
			List<Source> sources = new ArrayList<>();
			jsonList.forEach(element -> sources.add(parseSource(JSONUtils.convertToString(element, "source"))));
			
			return new SourceGristCost(recipeId, ingredient, sources, multiplier, cost, priority);
		}
		
		@Override
		protected SourceGristCost create(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority, GristSet cost)
		{
			return new SourceGristCost(recipeId, ingredient, priority, cost);
		}
	}
	
	private static Source parseSource(String name)
	{
		if(name.startsWith("#"))
			return new TagSource(new ResourceLocation(name.substring(1)));
		else return new ItemSource(new ResourceLocation(name));
	}
	
	private interface Source
	{
		GristSet getCostFor(GenerationContext context);
	}
	
	private static class ItemSource implements Source
	{
		final Item item;
		
		private ItemSource(ResourceLocation name)
		{
			this.item = ForgeRegistries.ITEMS.getValue(name);
		}
		
		@Override
		public GristSet getCostFor(GenerationContext context)
		{
			return context.lookupCostFor(item);
		}
	}
	
	public static String itemString(Item item)
	{
		return Objects.requireNonNull(item.getRegistryName()).toString();
	}
	
	private static class TagSource implements Source
	{
		final ITag<Item> tag;
		
		private TagSource(ResourceLocation name)
		{
			this.tag = TagCollectionManager.getInstance().getItems().getTag(name);
		}
		
		@Override
		public GristSet getCostFor(GenerationContext context)
		{
			GristSet maxCost = null;
			for(Item item : tag.getValues())
			{
				GristSet cost = context.lookupCostFor(item);
				
				if(cost != null && (maxCost == null || cost.getValue() > maxCost.getValue()))
					maxCost = cost;
			}
			return maxCost;
		}
	}
	
	public static String tagString(ITag<Item> tag)
	{
		return "#" + TagCollectionManager.getInstance().getItems().getIdOrThrow(tag).toString();
	}
}