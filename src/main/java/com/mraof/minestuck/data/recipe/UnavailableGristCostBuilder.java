package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
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

public class UnavailableGristCostBuilder
{
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private Integer priority = null;
	
	public static UnavailableGristCostBuilder of(TagKey<Item> tag)
	{
		ResourceLocation tagId = tag.location();
		return new UnavailableGristCostBuilder(new ResourceLocation(tagId.getNamespace(), tagId.getPath()+"_tag"), Ingredient.of(tag));
	}
	
	public static UnavailableGristCostBuilder of(ItemLike item)
	{
		return new UnavailableGristCostBuilder(item.asItem().getRegistryName(), Ingredient.of(item));
	}
	
	public static UnavailableGristCostBuilder of(Ingredient ingredient)
	{
		return new UnavailableGristCostBuilder(ingredient);
	}
	
	protected UnavailableGristCostBuilder(Ingredient ingredient)
	{
		this(null, ingredient);
	}
	
	protected UnavailableGristCostBuilder(ResourceLocation defaultName, Ingredient ingredient)
	{
		this.defaultName = defaultName;
		this.ingredient = ingredient;
	}
	
	public UnavailableGristCostBuilder priority(int priority)
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
		recipeSaver.accept(new Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath()), ingredient, priority));
	}
	
	public static class Result implements FinishedRecipe
	{
		public final ResourceLocation id;
		public final Ingredient ingredient;
		public final Integer priority;
		
		public Result(ResourceLocation id, Ingredient ingredient, Integer priority)
		{
			this.id = id;
			this.ingredient = ingredient;
			this.priority = priority;
		}
		
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
			jsonObject.add("ingredient", ingredient.toJson());
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
			return MSRecipeTypes.UNAVAILABLE_GRIST_COST.get();
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