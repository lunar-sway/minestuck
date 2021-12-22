package com.mraof.minestuck.data.recipe;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ContainerGristCostBuilder
{
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	private Integer priority = null;
	
	public static ContainerGristCostBuilder of(ITag<Item> tag)
	{
		ResourceLocation tagId = TagCollectionManager.getInstance().getItems().getIdOrThrow(tag);
		return new ContainerGristCostBuilder(new ResourceLocation(tagId.getNamespace(), tagId.getPath()+"_tag"), Ingredient.of(tag));
	}
	
	public static ContainerGristCostBuilder of(IItemProvider item)
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
		recipeSaver.accept(new Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath()), ingredient, new ImmutableGristSet(costBuilder), priority));
	}
	
	public static class Result extends GristCostRecipeBuilder.Result
	{
		public Result(ResourceLocation id, Ingredient ingredient, GristSet cost, Integer priority)
		{
			super(id, ingredient, cost, priority);
		}
		
		@Override
		public IRecipeSerializer<?> getType()
		{
			return MSRecipeTypes.CONTAINER_GRIST_COST;
		}
	}
}