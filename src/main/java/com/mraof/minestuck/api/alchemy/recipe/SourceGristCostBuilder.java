package com.mraof.minestuck.api.alchemy.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.api.alchemy.DefaultImmutableGristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.data.recipe.AdvancementFreeRecipe;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.alchemy.recipe.generator.SourceGristCost;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Used to datagen a grist cost that sources the cost from different items with some modification.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SourceGristCostBuilder
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Nullable
	private final ResourceLocation defaultName;
	private final Ingredient ingredient;
	private final ImmutableMap.Builder<GristType, Long> costBuilder = ImmutableMap.builder();
	private final List<String> sources = new ArrayList<>();
	private float multiplier = 1;
	@Nullable
	private Integer priority = null;
	
	public static SourceGristCostBuilder of(TagKey<Item> tag)
	{
		return new SourceGristCostBuilder(tag.location().withSuffix("_tag"), Ingredient.of(tag));
	}
	
	public static SourceGristCostBuilder of(ItemLike item)
	{
		return new SourceGristCostBuilder(ForgeRegistries.ITEMS.getKey(item.asItem()), Ingredient.of(item));
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
		sources.add(SourceGristCost.tagString(source));
		return this;
	}
	
	
	public SourceGristCostBuilder source(Item source)
	{
		sources.add(SourceGristCost.itemString(source));
		return this;
	}
	
	public SourceGristCostBuilder multiplier(float multiplier)
	{
		this.multiplier = multiplier;
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
		recipeSaver.accept(new Result(id.withPrefix("grist_costs/"), ingredient, priority, sources, multiplier, new DefaultImmutableGristSet(costBuilder)));
	}
	
	private record Result(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority,
						  List<String> sources, float multiplier, ImmutableGristSet cost) implements AdvancementFreeRecipe
	{
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
			jsonObject.add("ingredient", ingredient.toJson());
			
			if(priority != null)
				jsonObject.addProperty("priority", priority);
			
			JsonArray sourceArray = new JsonArray();
			sources.forEach(sourceArray::add);
			jsonObject.add("sources", sourceArray);
			
			if(multiplier != 1)
				jsonObject.addProperty("multiplier", multiplier);
			
			jsonObject.add("grist_cost", ImmutableGristSet.MAP_CODEC.encodeStart(JsonOps.INSTANCE, cost).getOrThrow(false, LOGGER::error));
		}
		
		@Override
		public ResourceLocation getId()
		{
			return id;
		}
		@Override
		public RecipeSerializer<?> getType()
		{
			return MSRecipeTypes.SOURCE_GRIST_COST.get();
		}
	}
}
