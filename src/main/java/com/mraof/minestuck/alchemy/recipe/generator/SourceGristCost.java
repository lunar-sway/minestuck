package com.mraof.minestuck.alchemy.recipe.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.IGristSet;
import com.mraof.minestuck.alchemy.IImmutableGristSet;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SourceGristCost extends GeneratedGristCost
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final List<Source> sources;
	private final float multiplier;
	private final IImmutableGristSet addedCost;
	
	private SourceGristCost(ResourceLocation id, Ingredient ingredient, List<Source> sources, float multiplier, IGristSet addedCost, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.sources = sources;
		this.multiplier = multiplier;
		this.addedCost = addedCost.asImmutable();
	}
	
	private SourceGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, @Nullable IGristSet cost)
	{
		super(id, ingredient, priority, cost);
		this.sources = null;
		this.multiplier = 0;
		this.addedCost = null;
	}
	
	@Nullable
	@Override
	protected GristSet generateCost(GenerationContext context)
	{
		GristSet costSum = new GristSet();
		for(Source source : sources)
		{
			IGristSet sourceCost = source.getCostFor(context);
			if(sourceCost != null)
				costSum.addGrist(sourceCost);
			else return null;
		}
		
		return costSum.scale(multiplier, false).addGrist(addedCost);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.SOURCE_GRIST_COST.get();
	}
	
	public static class Serializer extends GeneratedCostSerializer<SourceGristCost>
	{
		@Override
		protected SourceGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, @Nullable Integer priority)
		{
			IGristSet cost = IImmutableGristSet.MAP_CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "grist_cost"))
					.getOrThrow(false, LOGGER::error);
			float multiplier = json.has("multiplier") ? GsonHelper.getAsFloat(json, "multiplier") : 1;
			
			JsonArray jsonList = GsonHelper.getAsJsonArray(json, "sources");
			List<Source> sources = new ArrayList<>();
			jsonList.forEach(element -> sources.add(parseSource(GsonHelper.convertToString(element, "source"))));
			
			return new SourceGristCost(recipeId, ingredient, sources, multiplier, cost, priority);
		}
		
		@Override
		protected SourceGristCost create(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority, @Nullable IGristSet cost)
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
		@Nullable
		IGristSet getCostFor(GenerationContext context);
	}
	
	private static class ItemSource implements Source
	{
		final Item item;
		
		private ItemSource(ResourceLocation name)
		{
			this.item = ForgeRegistries.ITEMS.getValue(name);
		}
		
		@Override
		public IGristSet getCostFor(GenerationContext context)
		{
			return context.lookupCostFor(item);
		}
	}
	
	public static String itemString(Item item)
	{
		return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();
	}
	
	private static class TagSource implements Source
	{
		final TagKey<Item> tag;
		
		private TagSource(ResourceLocation name)
		{
			this.tag = TagKey.create(Registry.ITEM_REGISTRY, name);
		}
		
		@Override
		public IGristSet getCostFor(GenerationContext context)
		{
			IGristSet maxCost = null;
			for(Item item : Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(this.tag))
			{
				IGristSet cost = context.lookupCostFor(item);
				
				if(cost != null && (maxCost == null || cost.getValue() > maxCost.getValue()))
					maxCost = cost;
			}
			return maxCost;
		}
	}
	
	public static String tagString(TagKey<Item> tag)
	{
		return "#" + tag.location();
	}
}