package com.mraof.minestuck.data.recipe;

import com.google.common.collect.ImmutableMap;
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

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ContainerGristCostBuilder
{
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	private Integer priority = null;
	
	public static ContainerGristCostBuilder of(TagKey<Item> tag)
	{
		ResourceLocation tagId = tag.location();
		return new ContainerGristCostBuilder(new ResourceLocation(tagId.getNamespace(), tagId.getPath()+"_tag"), Ingredient.of(tag));
	}
	
	public static ContainerGristCostBuilder of(ItemLike item)
	{
		return new ContainerGristCostBuilder(item.asItem().getRegistryName(), Ingredient.of(item));
	}
	
	public static ContainerGristCostBuilder of(Ingredient ingredient)
	{
		return new ContainerGristCostBuilder(ingredient);
	}
	
	protected ContainerGristCostBuilder(Ingredient ingredient)
	{
		this(null, ingredient);
	}
	
	protected ContainerGristCostBuilder(ResourceLocation defaultName, Ingredient ingredient)
	{
		this.defaultName = defaultName;
		this.ingredient = ingredient;
	}
	
	public ContainerGristCostBuilder grist(Supplier<GristType> type, long amount)
	{
		return grist(type.get(), amount);
	}
	
	public ContainerGristCostBuilder grist(GristType type, long amount)
	{
		costBuilder.put(type, amount);
		return this;
	}
	
	public ContainerGristCostBuilder priority(int priority)
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
	
	public static class Result extends GristCostRecipeBuilder.Result
	{
		public Result(ResourceLocation id, Ingredient ingredient, GristSet cost, Integer priority)
		{
			super(id, ingredient, cost, priority);
		}
		
		@Override
		public RecipeSerializer<?> getType()
		{
			return MSRecipeTypes.CONTAINER_GRIST_COST.get();
		}
	}
}