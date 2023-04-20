package com.mraof.minestuck.alchemy.recipe.generator;

import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.alchemy.IGristSet;
import com.mraof.minestuck.alchemy.IImmutableGristSet;
import com.mraof.minestuck.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class GeneratedGristCost extends GristCostRecipe implements GeneratedCostProvider
{
	@Nullable
	private IImmutableGristSet cachedCost = null;
	private boolean hasGeneratedCost = false;
	
	protected GeneratedGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
	}
	
	protected GeneratedGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, @Nullable GristSet cost)
	{
		super(id, ingredient, priority);
		cachedCost = cost != null ? cost.asImmutable() : null;
		hasGeneratedCost = true;
	}
	
	@Override
	public IGristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown, @Nullable Level level)
	{
		return scaleToCountAndDurability(cachedCost, input, shouldRoundDown);
	}
	
	@Override
	public boolean matches(Container inv, Level level)
	{
		return cachedCost != null && super.matches(inv, level);
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		for(ItemStack stack : ingredient.getItems())
			consumer.accept(stack.getItem(), this);
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		if(cachedCost != null)
			return Collections.singletonList(new JeiGristCost.Set(ingredient, cachedCost));
		else return Collections.emptyList();
	}
	
	@Override
	public final GristCostResult generate(Item item, @Nullable GristCostResult lastCost, GenerationContext context)
	{
		if(lastCost != null)
			return lastCost;
		else if(context.shouldUseCache() && hasGeneratedCost)
			return GristCostResult.ofOrNull(cachedCost);
		else
		{
			IGristSet cost = generateCost(context);
			if(context.isPrimary())
			{
				hasGeneratedCost = true;
				if(cost != null)
					cachedCost = cost.asImmutable();
			}
			return GristCostResult.ofOrNull(cost);
		}
	}
	
	@Nullable
	protected abstract IGristSet generateCost(GenerationContext context);
	
	@Nullable
	protected final IGristSet getCachedCost()
	{
		return cachedCost;
	}
	
	public static abstract class GeneratedCostSerializer<T extends GeneratedGristCost> extends AbstractSerializer<T>
	{
		@Override
		protected T read(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority)
		{
			boolean hasCost = buffer.readBoolean();
			GristSet cost = hasCost ? GristSet.read(buffer) : null;
			
			return create(recipeId, buffer, ingredient, priority, cost);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, T recipe)
		{
			super.toNetwork(buffer, recipe);
			if(recipe.getCachedCost() != null)
			{
				buffer.writeBoolean(true);
				GristSet.write(recipe.getCachedCost(), buffer);
			} else buffer.writeBoolean(false);
		}
		
		protected abstract T create(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority, @Nullable GristSet cost);
	}
}