package com.mraof.minestuck.alchemy.recipe.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ContainerGristCost implements GristCostRecipe
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Ingredient ingredient;
	private final ImmutableGristSet addedCost;
	@Nullable
	private final Integer priority;
	private final GeneratedGristCostCache cache = new GeneratedGristCostCache();
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public ContainerGristCost(Ingredient ingredient, ImmutableGristSet addedCost, Optional<Integer> priority)
	{
		this.ingredient = ingredient;
		this.addedCost = addedCost;
		this.priority = priority.orElse(null);
	}
	
	@Nullable
	private GristSet generateCost(GeneratorCallback callback, ImmutableGristSet addedCost, ResourceLocation recipeId)
	{
		ItemStack container = ingredient.getItems().length > 0 ? ingredient.getItems()[0].getCraftingRemainingItem() : ItemStack.EMPTY;
		if(!container.isEmpty())
		{
			GristSet cost = callback.lookupCostFor(container);
			if(cost != null)
			{
				return cost.mutableCopy().add(addedCost);
			} else
			{
				if(callback.shouldSaveResult())
					LOGGER.warn("Got null grist cost when looking up container item {} for container grist cost {}. No grist cost is set for this recipe.", BuiltInRegistries.ITEM.getKey(container.getItem()), recipeId);
			}
		} else
		{
			if(callback.shouldSaveResult())
				LOGGER.warn("No container item found for ingredient to container grist cost {}. Assuming that the container cost is zero.", recipeId);
			return addedCost;
		}
		
		return null;
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
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer, ResourceLocation recipeId)
	{
		GristCostRecipe.addCostProviderForIngredient(consumer, this.ingredient,
				this.cache.generatedProvider(callback -> this.generateCost(callback, addedCost, recipeId)));
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
		private static final Codec<ContainerGristCost> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
						ImmutableGristSet.MAP_CODEC.fieldOf("grist_cost").forGetter(recipe -> recipe.addedCost),
						ExtraCodecs.strictOptionalField(Codec.INT, "priority").forGetter(recipe -> Optional.ofNullable(recipe.priority))
				).apply(instance, ContainerGristCost::new));
		
		@Override
		public Codec<ContainerGristCost> codec()
		{
			return CODEC;
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, ContainerGristCost recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
			recipe.cache.toNetwork(buffer);
		}
		
		@Override
		public ContainerGristCost fromNetwork(FriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			
			var recipe = new ContainerGristCost(ingredient, GristSet.EMPTY, Optional.of(priority));
			recipe.cache.fromNetwork(buffer);
			return recipe;
		}
	}
}