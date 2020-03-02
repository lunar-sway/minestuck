package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class GeneratedGristCost extends GristCostRecipe implements GeneratedCostProvider
{
	private ImmutableGristSet cachedCost = null;
	private boolean hasGeneratedCost = false;
	
	protected GeneratedGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
	}
	
	protected GeneratedGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, GristSet cost)
	{
		super(id, ingredient, priority);
		cachedCost = cost != null ? cost.asImmutable() : null;
		hasGeneratedCost = true;
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, World world)
	{
		return scaleToCountAndDurability(cachedCost, input, shouldRoundDown);
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn)
	{
		return cachedCost != null && super.matches(inv, worldIn);
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		for(ItemStack stack : ingredient.getMatchingStacks())
			consumer.accept(stack.getItem(), this);
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(World world)
	{
		if(cachedCost != null)
			return Collections.singletonList(new JeiGristCost.Set(ingredient, cachedCost));
		else return Collections.emptyList();
	}
	
	@Override
	public final GristCostResult generate(Item item, GristCostResult lastCost, GenerationContext context)
	{
		if(lastCost != null)
			return lastCost;
		else if(context.shouldUseCache() && hasGeneratedCost)
			return GristCostResult.ofOrNull(cachedCost);
		else
		{
			GristSet cost = generateCost(context);
			if(context.isPrimary())
			{
				hasGeneratedCost = true;
				if(cost != null)
					cachedCost = cost.asImmutable();
			}
			return GristCostResult.ofOrNull(cost);
		}
	}
	
	protected abstract GristSet generateCost(GenerationContext context);
	
	protected final GristSet getCachedCost()
	{
		return cachedCost;
	}
	
	public static abstract class GeneratedCostSerializer<T extends GeneratedGristCost> extends AbstractSerializer<T>
	{
		@Override
		protected T read(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority)
		{
			boolean hasCost = buffer.readBoolean();
			GristSet cost = hasCost ? GristSet.read(buffer) : null;
			
			return create(recipeId, buffer, ingredient, priority, cost);
		}
		
		@Override
		public void write(PacketBuffer buffer, T recipe)
		{
			super.write(buffer, recipe);
			if(recipe.getCachedCost() != null)
			{
				buffer.writeBoolean(true);
				recipe.getCachedCost().write(buffer);
			} else buffer.writeBoolean(false);
		}
		
		protected abstract T create(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority, GristSet cost);
	}
}