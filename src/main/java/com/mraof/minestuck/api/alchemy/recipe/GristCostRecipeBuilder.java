package com.mraof.minestuck.api.alchemy.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.data.recipe.AdvancementFreeRecipe;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.DefaultImmutableGristSet;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Used to datagen a regular grist cost.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GristCostRecipeBuilder
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Nullable
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	@Nullable
	private Integer priority = null;
	
	public static GristCostRecipeBuilder of(TagKey<Item> tag)
	{
		return new GristCostRecipeBuilder(tag.location().withSuffix("_tag"), Ingredient.of(tag));
	}
	
	public static GristCostRecipeBuilder of(ItemLike item)
	{
		return new GristCostRecipeBuilder(ForgeRegistries.ITEMS.getKey(item.asItem()), Ingredient.of(item));
	}
	
	public static GristCostRecipeBuilder of(Ingredient ingredient)
	{
		return new GristCostRecipeBuilder(null, ingredient);
	}
	
	private GristCostRecipeBuilder(@Nullable ResourceLocation defaultName, Ingredient ingredient)
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
		recipeSaver.accept(new Result(id.withPrefix("grist_costs/"), ingredient, new DefaultImmutableGristSet(costBuilder), priority));
	}
	
	private record Result(ResourceLocation id, Ingredient ingredient, ImmutableGristSet cost, @Nullable Integer priority) implements AdvancementFreeRecipe
	{
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
			jsonObject.add("ingredient", ingredient.toJson());
			jsonObject.add("grist_cost", ImmutableGristSet.MAP_CODEC.encodeStart(JsonOps.INSTANCE, cost).getOrThrow(false, LOGGER::error));
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
	}
}
