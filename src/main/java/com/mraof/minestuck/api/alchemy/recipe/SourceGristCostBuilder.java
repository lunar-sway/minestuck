package com.mraof.minestuck.api.alchemy.recipe;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.alchemy.recipe.generator.SourceGristCost;
import com.mraof.minestuck.api.alchemy.DefaultImmutableGristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Used to datagen a grist cost that sources the cost from different items with some modification.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SourceGristCostBuilder
{
	@Nullable
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	private final List<SourceGristCost.Source> sources = new ArrayList<>();
	private float multiplier = 1;
	@Nullable
	private Integer priority = null;
	
	public static SourceGristCostBuilder of(TagKey<Item> tag)
	{
		return new SourceGristCostBuilder(tag.location().withSuffix("_tag"), Ingredient.of(tag));
	}
	
	public static SourceGristCostBuilder of(ItemLike item)
	{
		return new SourceGristCostBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()), Ingredient.of(item));
	}
	
	public static SourceGristCostBuilder of(Ingredient ingredient)
	{
		return new SourceGristCostBuilder(null, ingredient);
	}
	
	private SourceGristCostBuilder(@Nullable ResourceLocation defaultName, Ingredient ingredient)
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
		sources.add(new SourceGristCost.TagSource(source));
		return this;
	}
	
	
	public SourceGristCostBuilder source(Item source)
	{
		sources.add(new SourceGristCost.ItemSource(source));
		return this;
	}
	
	public SourceGristCostBuilder multiplier(float multiplier)
	{
		this.multiplier = multiplier;
		return this;
	}
	
	public void build(RecipeOutput recipeOutput)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : BuiltInRegistries.ITEM.getKey(ingredient.getItems()[0].getItem()));
		build(recipeOutput, name);
	}
	
	public void buildFor(RecipeOutput recipeOutput, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : BuiltInRegistries.ITEM.getKey(ingredient.getItems()[0].getItem()));
		build(recipeOutput, new ResourceLocation(modId, name.getPath()));
	}
	
	public void build(RecipeOutput recipeOutput, ResourceLocation id)
	{
		recipeOutput.accept(id.withPrefix("grist_costs/"), new SourceGristCost(ingredient, sources, multiplier, new DefaultImmutableGristSet(costBuilder), Optional.ofNullable(priority)), null);
	}
}
