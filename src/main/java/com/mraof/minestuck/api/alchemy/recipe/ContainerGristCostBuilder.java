package com.mraof.minestuck.api.alchemy.recipe;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.alchemy.recipe.generator.ContainerGristCost;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Used to datagen a grist cost where the cost of the container item is automatically added to the recipe cost. Typically used for buckets.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ContainerGristCostBuilder
{
	@Nullable
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	@Nullable
	private Integer priority = null;
	
	public static ContainerGristCostBuilder of(TagKey<Item> tag)
	{
		return new ContainerGristCostBuilder(tag.location().withSuffix("_tag"), Ingredient.of(tag));
	}
	
	public static ContainerGristCostBuilder of(ItemLike item)
	{
		return new ContainerGristCostBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()), Ingredient.of(item));
	}
	
	public static ContainerGristCostBuilder of(Ingredient ingredient)
	{
		return new ContainerGristCostBuilder(null, ingredient);
	}
	
	private ContainerGristCostBuilder(@Nullable ResourceLocation defaultName, Ingredient ingredient)
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
		recipeOutput.accept(id.withPrefix("grist_costs/"), new ContainerGristCost(ingredient, new DefaultImmutableGristSet(costBuilder), Optional.ofNullable(priority)), null);
	}
}
