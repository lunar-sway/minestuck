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

public class ItemSourceGristCostBuilder
{
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	private Item source;
	private float multiplier = 1;
	private Integer priority = null;
	
	public static ItemSourceGristCostBuilder of(Tag<Item> tag)
	{
		return new ItemSourceGristCostBuilder(new ResourceLocation(tag.getId().getNamespace(), tag.getId().getPath()+"_tag"), Ingredient.fromTag(tag));
	}
	
	public static ItemSourceGristCostBuilder of(IItemProvider item)
	{
		return new ItemSourceGristCostBuilder(item.asItem().getRegistryName(), Ingredient.fromItems(item));
	}
	
	public static ItemSourceGristCostBuilder of(Ingredient ingredient)
	{
		return new ItemSourceGristCostBuilder(ingredient);
	}
	
	protected ItemSourceGristCostBuilder(Ingredient ingredient)
	{
		this(null, ingredient);
	}
	
	protected ItemSourceGristCostBuilder(ResourceLocation defaultName, Ingredient ingredient)
	{
		this.defaultName = defaultName;
		this.ingredient = ingredient;
	}
	
	public ItemSourceGristCostBuilder grist(Supplier<GristType> type, long amount)
	{
		return grist(type.get(), amount);
	}
	
	public ItemSourceGristCostBuilder grist(GristType type, long amount)
	{
		costBuilder.put(type, amount);
		return this;
	}
	
	public ItemSourceGristCostBuilder priority(int priority)
	{
		this.priority = priority;
		return this;
	}
	
	public ItemSourceGristCostBuilder source(IItemProvider source)
	{
		this.source = source.asItem();
		return this;
	}
	
	public ItemSourceGristCostBuilder multiplier(float multiplier)
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
		private final Item source;
		private final float multiplier;
		
		public Result(ResourceLocation id, Ingredient ingredient, Item source, float multiplier, GristSet cost, Integer priority)
		{
			super(id, ingredient, cost, priority);
			this.source = Objects.requireNonNull(source, "Source item must not be null");
			this.multiplier = multiplier;
		}
		
		@Override
		public void serialize(JsonObject jsonObject)
		{
			super.serialize(jsonObject);
			jsonObject.addProperty("source", String.valueOf(source.getRegistryName()));
			if(multiplier != 1)
				jsonObject.addProperty("multiplier", multiplier);
		}
		
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return MSRecipeTypes.ITEM_SOURCE_GRIST_COST;
		}
	}
}