package com.mraof.minestuck.alchemy.recipe.generator;

import com.mraof.minestuck.alchemy.recipe.GeneratedGristCostCache;
import com.mraof.minestuck.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.alchemy.recipe.SimpleGristCost;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
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
public abstract class GeneratedGristCost extends SimpleGristCost
{
	protected final GeneratedGristCostCache cache;
	
	protected GeneratedGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.cache = new GeneratedGristCostCache(this::generateCost);
	}
	
	protected GeneratedGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, GeneratedGristCostCache cache)
	{
		super(id, ingredient, priority);
		this.cache = cache;
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
		for(ItemStack stack : ingredient.getItems())
			consumer.accept(stack.getItem(), this.cache);
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		if(cache.getCachedCost() != null)
			return Collections.singletonList(new JeiGristCost.Set(ingredient, cache.getCachedCost()));
		else return Collections.emptyList();
	}
	
	@Nullable
	protected abstract GristSet generateCost(GenerationContext context);
	
	public static abstract class GeneratedCostSerializer<T extends GeneratedGristCost> extends AbstractSerializer<T>
	{
		@Override
		protected T read(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority)
		{
			GeneratedGristCostCache cache = GeneratedGristCostCache.read(buffer);
			
			return create(recipeId, buffer, ingredient, priority, cache);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, T recipe)
		{
			super.toNetwork(buffer, recipe);
			recipe.cache.write(buffer);
		}
		
		protected abstract T create(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority, GeneratedGristCostCache cache);
	}
}