package com.mraof.minestuck.alchemy.recipe.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SourceGristCost implements GristCostRecipe
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final ResourceLocation id;
	private final Ingredient ingredient;
	private final List<Source> sources;
	private final float multiplier;
	private final ImmutableGristSet addedCost;
	@Nullable
	private final Integer priority;
	private final GeneratedGristCostCache cache = new GeneratedGristCostCache();
	
	private SourceGristCost(ResourceLocation id, Ingredient ingredient, List<Source> sources, float multiplier, ImmutableGristSet addedCost, @Nullable Integer priority)
	{
		this.id = id;
		this.ingredient = ingredient;
		this.sources = sources;
		this.multiplier = multiplier;
		this.addedCost = addedCost;
		this.priority = priority;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}
	
	@Override
	public boolean matches(Container inv, Level level)
	{
		return cache.getCachedCost() != null && ingredient.test(inv.getItem(0));
	}
	
	@Override
	public int getPriority()
	{
		return Objects.requireNonNullElseGet(this.priority, () -> GristCostRecipe.defaultPriority(this.ingredient));
	}
	
	@Nullable
	private GristSet generateCost(GeneratorCallback callback, List<Source> sources, float multiplier, ImmutableGristSet addedCost)
	{
		MutableGristSet costSum = MutableGristSet.newDefault();
		for(Source source : sources)
		{
			GristSet sourceCost = source.getCostFor(callback);
			if(sourceCost != null)
				costSum.add(sourceCost);
			else return null;
		}
		
		return costSum.scale(multiplier, false).add(addedCost);
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown)
	{
		return GristCostRecipe.scaleToCountAndDurability(cache.getCachedCost(), input, shouldRoundDown);
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		GristCostRecipe.addCostProviderForIngredient(consumer, this.ingredient,
				this.cache.generatedProvider(callback -> generateCost(callback, sources, multiplier, addedCost)));
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		if(cache.getCachedCost() != null)
			return Collections.singletonList(new JeiGristCost.Set(ingredient, cache.getCachedCost()));
		else return Collections.emptyList();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.SOURCE_GRIST_COST.get();
	}
	
	public static class Serializer implements RecipeSerializer<SourceGristCost>
	{
		@Override
		public SourceGristCost fromJson(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
			Integer priority = json.has("priority") ? GsonHelper.getAsInt(json, "priority") : null;
			
			ImmutableGristSet cost = ImmutableGristSet.MAP_CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "grist_cost"))
					.getOrThrow(false, LOGGER::error);
			float multiplier = json.has("multiplier") ? GsonHelper.getAsFloat(json, "multiplier") : 1;
			JsonArray jsonList = GsonHelper.getAsJsonArray(json, "sources");
			List<Source> sources = new ArrayList<>();
			jsonList.forEach(element -> sources.add(parseSource(GsonHelper.convertToString(element, "source"))));
			
			return new SourceGristCost(recipeId, ingredient, sources, multiplier, cost, priority);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, SourceGristCost recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
			recipe.cache.toNetwork(buffer);
		}
		
		@Nullable
		@Override
		public SourceGristCost fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			
			var recipe = new SourceGristCost(recipeId, ingredient, Collections.emptyList(), 1, GristSet.EMPTY, priority);
			recipe.cache.fromNetwork(buffer);
			
			return recipe;
		}
	}
	
	private static Source parseSource(String name)
	{
		if(name.startsWith("#"))
			return new TagSource(new ResourceLocation(name.substring(1)));
		else return new ItemSource(new ResourceLocation(name));
	}
	
	private interface Source
	{
		@Nullable
		GristSet getCostFor(GeneratorCallback callback);
	}
	
	private static class ItemSource implements Source
	{
		final Item item;
		
		private ItemSource(ResourceLocation name)
		{
			this.item = ForgeRegistries.ITEMS.getValue(name);
		}
		
		@Override
		public GristSet getCostFor(GeneratorCallback callback)
		{
			return callback.lookupCostFor(item);
		}
	}
	
	public static String itemString(Item item)
	{
		return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();
	}
	
	private static class TagSource implements Source
	{
		final TagKey<Item> tag;
		
		private TagSource(ResourceLocation name)
		{
			this.tag = TagKey.create(Registries.ITEM, name);
		}
		
		@Override
		public GristSet getCostFor(GeneratorCallback callback)
		{
			GristSet maxCost = null;
			for(Item item : Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(this.tag))
			{
				GristSet cost = callback.lookupCostFor(item);
				
				if(cost != null && (maxCost == null || cost.getValue() > maxCost.getValue()))
					maxCost = cost;
			}
			return maxCost;
		}
	}
	
	public static String tagString(TagKey<Item> tag)
	{
		return "#" + tag.location();
	}
}