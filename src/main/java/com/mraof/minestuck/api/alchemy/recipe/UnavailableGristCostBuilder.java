package com.mraof.minestuck.api.alchemy.recipe;

import com.mraof.minestuck.alchemy.recipe.UnavailableGristCost;
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
 * Used to datagen a grist cost that makes the ingredient unalchemizable.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public final class UnavailableGristCostBuilder
{
	@Nullable
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	@Nullable
	private Integer priority = null;
	
	public static UnavailableGristCostBuilder of(TagKey<Item> tag)
	{
		return new UnavailableGristCostBuilder(tag.location().withSuffix("_tag"), Ingredient.of(tag));
	}
	
	public static UnavailableGristCostBuilder of(ItemLike item)
	{
		return new UnavailableGristCostBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()), Ingredient.of(item));
	}
	
	public static UnavailableGristCostBuilder of(Ingredient ingredient)
	{
		return new UnavailableGristCostBuilder(null, ingredient);
	}
	
	private UnavailableGristCostBuilder(@Nullable ResourceLocation defaultName, Ingredient ingredient)
	{
		this.defaultName = defaultName;
		this.ingredient = ingredient;
	}
	
	public UnavailableGristCostBuilder priority(int priority)
	{
		this.priority = priority;
		return this;
	}
	
	public void build(RecipeOutput rerecipeOutputipeSaver)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : BuiltInRegistries.ITEM.getKey(ingredient.getItems()[0].getItem()));
		build(rerecipeOutputipeSaver, name);
	}
	
	public void buildFor(RecipeOutput recipeOutput, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : BuiltInRegistries.ITEM.getKey(ingredient.getItems()[0].getItem()));
		build(recipeOutput, new ResourceLocation(modId, name.getPath()));
	}
	
	public void build(RecipeOutput recipeOutput, ResourceLocation id)
	{
		recipeOutput.accept(id.withPrefix("grist_costs/"), new UnavailableGristCost(ingredient, Optional.ofNullable(priority)), null);
	}
}
