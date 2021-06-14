package com.mraof.minestuck.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import com.mraof.minestuck.item.crafting.alchemy.generator.SourceGristCost;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SourceGristCostBuilder
{
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	private final List<String> sources = new ArrayList<>();
	private float multiplier = 1;
	private Integer priority = null;
	
	public static SourceGristCostBuilder of(ITag<Item> tag)
	{
		ResourceLocation tagId = TagCollectionManager.getInstance().getItems().getIdOrThrow(tag);
		return new SourceGristCostBuilder(new ResourceLocation(tagId.getNamespace(), tagId.getPath()+"_tag"), Ingredient.of(tag));
	}
	
	public static SourceGristCostBuilder of(IItemProvider item)
	{
		return new SourceGristCostBuilder(item.asItem().getRegistryName(), Ingredient.of(item));
	}
	
	public static SourceGristCostBuilder of(Ingredient ingredient)
	{
		return new SourceGristCostBuilder(ingredient);
	}
	
	protected SourceGristCostBuilder(Ingredient ingredient)
	{
		this(null, ingredient);
	}
	
	protected SourceGristCostBuilder(ResourceLocation defaultName, Ingredient ingredient)
	{
		this.defaultName = defaultName;
		this.ingredient = ingredient;
	}
	
	public SourceGristCostBuilder grist(Supplier<GristType> type, long amount)
	{
		return grist(type.get(), amount);
	}
	
	public SourceGristCostBuilder grist(GristType type, long amount)
	{
		costBuilder.put(type, amount);
		return this;
	}
	
	public SourceGristCostBuilder priority(int priority)
	{
		this.priority = priority;
		return this;
	}
	
	public SourceGristCostBuilder source(ITag<Item> source)
	{
		sources.add(SourceGristCost.tagString(source));
		return this;
	}
	
	
	public SourceGristCostBuilder source(Item source)
	{
		sources.add(SourceGristCost.itemString(source));
		return this;
	}
	
	public SourceGristCostBuilder multiplier(float multiplier)
	{
		this.multiplier = multiplier;
		return this;
	}
	
	public void build(Consumer<IFinishedRecipe> recipeSaver)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ingredient.getItems()[0].getItem().getRegistryName());
		build(recipeSaver, name);
	}
	
	public void buildFor(Consumer<IFinishedRecipe> recipeSaver, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ingredient.getItems()[0].getItem().getRegistryName());
		build(recipeSaver, new ResourceLocation(modId, name.getPath()));
	}
	
	public void build(Consumer<IFinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath()), ingredient, sources, multiplier, new ImmutableGristSet(costBuilder), priority));
	}
	
	public static class Result extends GristCostRecipeBuilder.Result
	{
		private final List<String> sources;
		private final float multiplier;
		
		public Result(ResourceLocation id, Ingredient ingredient, List<String> sources, float multiplier, GristSet cost, Integer priority)
		{
			super(id, ingredient, cost, priority);
			this.sources = sources;
			this.multiplier = multiplier;
		}
		
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
			super.serializeRecipeData(jsonObject);
			JsonArray sourceArray = new JsonArray();
			sources.forEach(sourceArray::add);
			jsonObject.add("sources", sourceArray);
			if(multiplier != 1)
				jsonObject.addProperty("multiplier", multiplier);
		}
		
		@Override
		public IRecipeSerializer<?> getType()
		{
			return MSRecipeTypes.SOURCE_GRIST_COST;
		}
	}
}