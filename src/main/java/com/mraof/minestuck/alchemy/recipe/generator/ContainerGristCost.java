package com.mraof.minestuck.alchemy.recipe.generator;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ContainerGristCost implements GristCostRecipe
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final ResourceLocation id;
	private final Ingredient ingredient;
	@Nullable
	private final Integer priority;
	private final GeneratedGristCostCache cache;
	
	public ContainerGristCost(ResourceLocation id, Ingredient ingredient, ImmutableGristSet addedCost, @Nullable Integer priority)
	{
		this.id = id;
		this.ingredient = ingredient;
		this.priority = priority;
		this.cache = new GeneratedGristCostCache(context -> this.generateCost(context, addedCost));
	}
	
	private ContainerGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, GeneratedGristCostCache cache)
	{
		this.id = id;
		this.ingredient = ingredient;
		this.priority = priority;
		this.cache = cache;
	}
	
	@Nullable
	private GristSet generateCost(GenerationContext context, ImmutableGristSet addedCost)
	{
		ItemStack container = ingredient.getItems().length > 0 ? ingredient.getItems()[0].getCraftingRemainingItem() : ItemStack.EMPTY;
		if(!container.isEmpty())
		{
			GristSet cost = context.lookupCostFor(container);
			if(cost != null)
			{
				return cost.mutableCopy().add(addedCost);
			} else
			{
				if(context.isPrimary())
					LOGGER.warn("Got null grist cost when looking up container item {} for container grist cost {}. No grist cost is set for this recipe.", ForgeRegistries.ITEMS.getKey(container.getItem()), id);
			}
		} else
		{
			if(context.isPrimary())
				LOGGER.warn("No container item found for ingredient to container grist cost {}. Assuming that the container cost is zero.", id);
			return addedCost;
		}
		
		return null;
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
	
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown)
	{
		return GristCostRecipe.scaleToCountAndDurability(cache.getCachedCost(), input, shouldRoundDown);
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		GristCostRecipe.addCostProviderForIngredient(consumer, this.ingredient, this.cache);
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
		return MSRecipeTypes.CONTAINER_GRIST_COST.get();
	}
	
	public static class Serializer implements RecipeSerializer<ContainerGristCost>
	{
		@Override
		public ContainerGristCost fromJson(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
			Integer priority = json.has("priority") ? GsonHelper.getAsInt(json, "priority") : null;
			
			ImmutableGristSet cost = ImmutableGristSet.MAP_CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "grist_cost"))
					.getOrThrow(false, LOGGER::error);
			
			return new ContainerGristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, ContainerGristCost recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
			recipe.cache.toNetwork(buffer);
		}
		
		@Nullable
		@Override
		public ContainerGristCost fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			GeneratedGristCostCache cache = GeneratedGristCostCache.fromNetwork(buffer);
			
			return new ContainerGristCost(recipeId, ingredient, priority, cache);
		}
	}
}