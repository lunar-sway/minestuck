package com.mraof.minestuck.api.alchemy.recipe;

import com.mraof.minestuck.alchemy.recipe.WildcardGristCost;
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
import java.util.Objects;
import java.util.Optional;

/**
 * Used to datagen a grist cost which accepts an amount of any grist type chosen by the player.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public final class WildcardGristCostBuilder
{
	@Nullable
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private long cost = 0;
	@Nullable
	private Integer priority = null;
	
	public static WildcardGristCostBuilder of(TagKey<Item> tag)
	{
		return new WildcardGristCostBuilder(tag.location().withSuffix("_tag"), Ingredient.of(tag));
	}
	
	public static WildcardGristCostBuilder of(ItemLike item)
	{
		return new WildcardGristCostBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()), Ingredient.of(item));
	}
	
	public static WildcardGristCostBuilder of(Ingredient ingredient)
	{
		return new WildcardGristCostBuilder(null, ingredient);
	}
	
	private WildcardGristCostBuilder(@Nullable ResourceLocation defaultName, Ingredient ingredient)
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
		if(this.cost == 0)
			throw new IllegalStateException("Must set the wildcard cost before building!");
		recipeOutput.accept(id.withPrefix("grist_costs/"), new WildcardGristCost(ingredient, cost, Optional.ofNullable(priority)), null);
	}
}
