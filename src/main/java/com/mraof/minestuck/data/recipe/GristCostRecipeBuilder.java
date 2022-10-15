package com.mraof.minestuck.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.alchemy.ImmutableGristSet;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GristCostRecipeBuilder
{
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	private Integer priority = null;
	
	public static GristCostRecipeBuilder of(TagKey<Item> tag)
	{
		ResourceLocation tagId = tag.location();
		return new GristCostRecipeBuilder(new ResourceLocation(tagId.getNamespace(), tagId.getPath()+"_tag"), Ingredient.of(tag));
	}
	
	public static GristCostRecipeBuilder of(ItemLike item)
	{
		return new GristCostRecipeBuilder(item.asItem().getRegistryName(), Ingredient.of(item));
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
	
	public GristCostRecipeBuilder grist(Supplier<GristType> type, long amount)
	{
		return grist(type.get(), amount);
	}
	
	public GristCostRecipeBuilder grist(GristType type, long amount)
	{
		costBuilder.put(type, amount);
		return this;
	}
	
	public GristCostRecipeBuilder priority(int priority)
	{
		this.priority = priority;
		return this;
	}
	
	public void build(Consumer<FinishedRecipe> recipeSaver)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ingredient.getItems()[0].getItem().getRegistryName());
		build(recipeSaver, name);
	}
	
	public void buildFor(Consumer<FinishedRecipe> recipeSaver, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ingredient.getItems()[0].getItem().getRegistryName());
		build(recipeSaver, new ResourceLocation(modId, name.getPath()));
	}
	
	public void build(Consumer<FinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath()), ingredient, new ImmutableGristSet(costBuilder), priority));
	}
	
	public static class Result implements FinishedRecipe
	{
		public final ResourceLocation id;
		public final Ingredient ingredient;
		public final GristSet cost;
		public final Integer priority;
		
		public Result(ResourceLocation id, Ingredient ingredient, GristSet cost, Integer priority)
		{
			this.id = id;
			this.ingredient = ingredient;
			this.cost = cost;
			this.priority = priority;
		}
		
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
			jsonObject.add("ingredient", ingredient.toJson());
			jsonObject.add("grist_cost", cost.serialize());
			if(priority != null)
				jsonObject.addProperty("priority", priority);
		}
		
		@Override
		public ResourceLocation getId()
		{
			return id;
		}
		
		@Override
		public RecipeSerializer<?> getType()
		{
			return MSRecipeTypes.GRIST_COST.get();
		}
		
		@Nullable
		@Override
		public JsonObject serializeAdvancement()
		{
			return null;
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementId()
		{
			return new ResourceLocation("");
		}
	}
}