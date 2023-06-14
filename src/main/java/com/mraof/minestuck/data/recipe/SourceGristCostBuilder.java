package com.mraof.minestuck.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.alchemy.recipe.generator.SourceGristCost;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

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
	
	public static SourceGristCostBuilder of(TagKey<Item> tag)
	{
		ResourceLocation tagId = tag.location();
		return new SourceGristCostBuilder(new ResourceLocation(tagId.getNamespace(), tagId.getPath()+"_tag"), Ingredient.of(tag));
	}
	
	public static SourceGristCostBuilder of(ItemLike item)
	{
		return new SourceGristCostBuilder(ForgeRegistries.ITEMS.getKey(item.asItem()), Ingredient.of(item));
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
	
	public SourceGristCostBuilder source(TagKey<Item> source)
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
	
	public void build(Consumer<FinishedRecipe> recipeSaver)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ForgeRegistries.ITEMS.getKey(ingredient.getItems()[0].getItem()));
		build(recipeSaver, name);
	}
	
	public void buildFor(Consumer<FinishedRecipe> recipeSaver, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ForgeRegistries.ITEMS.getKey(ingredient.getItems()[0].getItem()));
		build(recipeSaver, new ResourceLocation(modId, name.getPath()));
	}
	
	public void build(Consumer<FinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath()), ingredient, sources, multiplier, new DefaultImmutableGristSet(costBuilder), priority));
	}
	
	public static class Result extends GristCostRecipeBuilder.Result
	{
		private final List<String> sources;
		private final float multiplier;
		
		public Result(ResourceLocation id, Ingredient ingredient, List<String> sources, float multiplier, ImmutableGristSet cost, Integer priority)
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
		public RecipeSerializer<?> getType()
		{
			return MSRecipeTypes.SOURCE_GRIST_COST.get();
		}
	}
}