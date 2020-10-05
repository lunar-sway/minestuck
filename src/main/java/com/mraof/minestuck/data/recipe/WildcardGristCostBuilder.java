package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class WildcardGristCostBuilder
{
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private long cost = 0;
	private Integer priority = null;
	
	public static WildcardGristCostBuilder of(ITag<Item> tag)
	{
		ResourceLocation tagId = TagCollectionManager.getManager().getItemTags().getValidatedIdFromTag(tag);
		return new WildcardGristCostBuilder(new ResourceLocation(tagId.getNamespace(), tagId.getPath()+"_tag"), Ingredient.fromTag(tag));
	}
	
	public static WildcardGristCostBuilder of(IItemProvider item)
	{
		return new WildcardGristCostBuilder(item.asItem().getRegistryName(), Ingredient.fromItems(item));
	}
	
	public static WildcardGristCostBuilder of(Ingredient ingredient)
	{
		return new WildcardGristCostBuilder(ingredient);
	}
	
	protected WildcardGristCostBuilder(Ingredient ingredient)
	{
		this(null, ingredient);
	}
	
	protected WildcardGristCostBuilder(ResourceLocation defaultName, Ingredient ingredient)
	{
		this.defaultName = defaultName;
		this.ingredient = ingredient;
	}
	
	public WildcardGristCostBuilder cost(long wildcardCost)
	{
		cost = wildcardCost;
		return this;
	}
	
	public WildcardGristCostBuilder priority(int priority)
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
		if(this.cost == 0)
			throw new IllegalStateException("Must set the wildcard cost before building!");
		recipeSaver.accept(new Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath()), ingredient, cost, priority));
	}
	
	public static class Result implements IFinishedRecipe
	{
		public final ResourceLocation id;
		public final Ingredient ingredient;
		public final long cost;
		public final Integer priority;
		
		public Result(ResourceLocation id, Ingredient ingredient, long cost, Integer priority)
		{
			this.id = id;
			this.ingredient = ingredient;
			this.cost = cost;
			this.priority = priority;
		}
		
		@Override
		public void serialize(JsonObject jsonObject)
		{
			jsonObject.add("ingredient", ingredient.serialize());
			jsonObject.addProperty("grist_cost", cost);
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
			return MSRecipeTypes.WILDCARD_GRIST_COST;
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