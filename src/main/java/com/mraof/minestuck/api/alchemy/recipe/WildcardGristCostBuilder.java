package com.mraof.minestuck.api.alchemy.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.data.recipe.AdvancementFreeRecipe;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Consumer;

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
		return new WildcardGristCostBuilder(ForgeRegistries.ITEMS.getKey(item.asItem()), Ingredient.of(item));
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
	
	public void build(Consumer<FinishedRecipe> recipeSaver)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ForgeRegistries.ITEMS.getKey(ingredient.getItems()[0].getItem()));
		build(recipeSaver, name);
	}
	
	public void buildFor(Consumer<FinishedRecipe> recipeSaver, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(defaultName != null ? defaultName : ForgeRegistries.ITEMS.getKey(ingredient.getItems()[0].getItem()));
		build(recipeSaver, new ResourceLocation(modId, name.getPath()));
	}
	
	public void build(Consumer<FinishedRecipe> recipeSaver, ResourceLocation id)
	{
		if(this.cost == 0)
			throw new IllegalStateException("Must set the wildcard cost before building!");
		recipeSaver.accept(new Result(id.withPrefix("grist_costs/"), ingredient, cost, priority));
	}
	
	private record Result(ResourceLocation id, Ingredient ingredient, long cost, @Nullable Integer priority) implements AdvancementFreeRecipe
	{
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
			jsonObject.add("ingredient", ingredient.toJson());
			jsonObject.addProperty("grist_cost", cost);
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
			return MSRecipeTypes.WILDCARD_GRIST_COST.get();
		}
	}
}
