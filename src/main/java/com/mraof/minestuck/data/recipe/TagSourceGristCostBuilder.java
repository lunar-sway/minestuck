package com.mraof.minestuck.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TagSourceGristCostBuilder
{
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	private Tag<Item> source;
	private float multiplier = 1;
	private Integer priority = null;
	
	public static TagSourceGristCostBuilder of(Tag<Item> tag)
	{
		return new TagSourceGristCostBuilder(new ResourceLocation(tag.getId().getNamespace(), tag.getId().getPath()+"_tag"), Ingredient.fromTag(tag));
	}
	
	public static TagSourceGristCostBuilder of(IItemProvider item)
	{
		return new TagSourceGristCostBuilder(item.asItem().getRegistryName(), Ingredient.fromItems(item));
	}
	
	public static TagSourceGristCostBuilder of(Ingredient ingredient)
	{
		return new TagSourceGristCostBuilder(ingredient);
	}
	
	protected TagSourceGristCostBuilder(Ingredient ingredient)
	{
		this(null, ingredient);
	}
	
	protected TagSourceGristCostBuilder(ResourceLocation defaultName, Ingredient ingredient)
	{
		this.defaultName = defaultName;
		this.ingredient = ingredient;
	}
	
	public TagSourceGristCostBuilder grist(Supplier<GristType> type, long amount)
	{
		return grist(type.get(), amount);
	}
	
	public TagSourceGristCostBuilder grist(GristType type, long amount)
	{
		costBuilder.put(type, amount);
		return this;
	}
	
	public TagSourceGristCostBuilder priority(int priority)
	{
		this.priority = priority;
		return this;
	}
	
	public TagSourceGristCostBuilder source(Tag<Item> source)
	{
		this.source = source;
		return this;
	}
	
	public TagSourceGristCostBuilder multiplier(float multiplier)
	{
		this.multiplier = multiplier;
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
		recipeSaver.accept(new Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath()), ingredient, source, multiplier, new ImmutableGristSet(costBuilder), priority));
	}
	
	public static class Result extends GristCostRecipeBuilder.Result
	{
		private final Tag<Item> source;
		private final float multiplier;
		
		public Result(ResourceLocation id, Ingredient ingredient, Tag<Item> source, float multiplier, GristSet cost, Integer priority)
		{
			super(id, ingredient, cost, priority);
			this.source = Objects.requireNonNull(source, "Source tag must not be null");
			this.multiplier = multiplier;
		}
		
		@Override
		public void serialize(JsonObject jsonObject)
		{
			super.serialize(jsonObject);
			jsonObject.addProperty("source", source.getId().toString());
			if(multiplier != 1)
				jsonObject.addProperty("multiplier", multiplier);
		}
		
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return MSRecipeTypes.TAG_SOURCE_GRIST_COST;
		}
	}
}