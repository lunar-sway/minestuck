package com.mraof.minestuck.alchemy.recipe.generator;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.alchemy.recipe.SimpleGristCost;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiGristCost;
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
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ContainerGristCost extends SimpleGristCost
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final GeneratedGristCostCache cache;
	
	public ContainerGristCost(ResourceLocation id, Ingredient ingredient, ImmutableGristSet addedCost, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.cache = new GeneratedGristCostCache(context -> this.generateCost(context, addedCost));
	}
	
	private ContainerGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, GeneratedGristCostCache cache)
	{
		super(id, ingredient, priority);
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
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.CONTAINER_GRIST_COST.get();
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown, @Nullable Level level)
	{
		return GristCostRecipe.scaleToCountAndDurability(cache.getCachedCost(), input, shouldRoundDown);
	}
	
	@Override
	public boolean matches(Container inv, Level level)
	{
		return cache.getCachedCost() != null && super.matches(inv, level);
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
	
	public static class Serializer extends AbstractSerializer<ContainerGristCost>
	{
		@Override
		protected ContainerGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, @Nullable Integer priority)
		{
			ImmutableGristSet cost = ImmutableGristSet.MAP_CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "grist_cost"))
					.getOrThrow(false, LOGGER::error);
			return new ContainerGristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		protected ContainerGristCost read(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority)
		{
			GeneratedGristCostCache cache = GeneratedGristCostCache.read(buffer);
			
			return new ContainerGristCost(recipeId, ingredient, priority, cache);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, ContainerGristCost recipe)
		{
			super.toNetwork(buffer, recipe);
			recipe.cache.write(buffer);
		}
	}
}