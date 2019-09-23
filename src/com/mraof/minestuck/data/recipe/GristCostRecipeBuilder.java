package com.mraof.minestuck.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class GristCostRecipeBuilder
{
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private ImmutableMap.Builder<GristType, Integer> costBuilder = ImmutableMap.builder();
	private Integer wildcard = null;
	private Integer priority = null;
	
	public static GristCostRecipeBuilder of(Tag<Item> tag)
	{
		return new GristCostRecipeBuilder(new ResourceLocation(tag.getId().getNamespace(), tag.getId().getPath()+"_tag"), Ingredient.fromTag(tag));
	}
	
	public static GristCostRecipeBuilder of(IItemProvider item)
	{
		return new GristCostRecipeBuilder(item.asItem().getRegistryName(), Ingredient.fromItems(item));
	}
	
	public static GristCostRecipeBuilder of(Ingredient ingredient)
	{
		return new GristCostRecipeBuilder(ingredient);
	}
	
	protected GristCostRecipeBuilder(Ingredient ingredient)
	{
		this(null, ingredient);
	}
	
	protected GristCostRecipeBuilder(ResourceLocation defaultName, Ingredient ingredient)
	{
		this.defaultName = defaultName;
		this.ingredient = ingredient;
	}
	
	public GristCostRecipeBuilder grist(GristType type, int amount)
	{
		if(costBuilder == null)
			throw new IllegalStateException("Can't add a grist cost to recipe if the recipe is set to a special type!");
		costBuilder.put(type, amount);
		return this;
	}
	
	public GristCostRecipeBuilder makeUnavailable()
	{
		costBuilder = null;
		return this;
	}
	
	public GristCostRecipeBuilder wildcard(int wildcardCost)
	{
		wildcard = wildcardCost;
		costBuilder = null;
		return this;
	}
	
	public GristCostRecipeBuilder priority(int priority)
	{
		this.priority = priority;
		return this;
	}
	
	public void build(Consumer<IFinishedRecipe> recipeSaver)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ingredient.getMatchingStacks()[0].getItem().getRegistryName());
		build(recipeSaver, name);
	}
	
	public void buildFor(Consumer<IFinishedRecipe> recipeSaver, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ingredient.getMatchingStacks()[0].getItem().getRegistryName());
		build(recipeSaver, new ResourceLocation(modId, name.getPath()));
	}
	
	public void build(Consumer<IFinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath()), ingredient, costBuilder != null ? GristSet.immutable(costBuilder.build()) : null, wildcard, priority));
	}
	
	public static class Result implements IFinishedRecipe
	{
		public final ResourceLocation id;
		public final Ingredient ingredient;
		public final GristSet cost;
		public final Integer wildcard;
		public final Integer priority;
		
		public Result(ResourceLocation id, Ingredient ingredient, GristSet cost, Integer wildcard, Integer priority)
		{
			this.id = id;
			this.ingredient = ingredient;
			this.cost = cost;
			this.wildcard = wildcard;
			this.priority = priority;
		}
		
		@Override
		public void serialize(JsonObject jsonObject)
		{
			jsonObject.add("ingredient", ingredient.serialize());
			if(cost != null)
				jsonObject.add("grist_cost", cost.serialize());
			else if(wildcard != null)
				jsonObject.addProperty("grist_cost", wildcard);
			if(priority != null)
				jsonObject.addProperty("priority", priority);
		}
		
		@Override
		public ResourceLocation getID()
		{
			return id;
		}
		
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return MSRecipeTypes.GRIST_COST;
		}
		
		@Nullable
		@Override
		public JsonObject getAdvancementJson()
		{
			return null;
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementID()
		{
			return new ResourceLocation("");
		}
	}
}