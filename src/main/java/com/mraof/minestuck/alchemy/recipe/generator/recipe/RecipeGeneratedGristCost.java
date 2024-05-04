package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class RecipeGeneratedGristCost implements GristCostRecipe
{
	@Nullable
	private RecipeGeneratedCostHandler handler;
	
	public RecipeGeneratedGristCost()
	{
		this(null);
	}
	
	private RecipeGeneratedGristCost(@Nullable RecipeGeneratedCostHandler handler)
	{
		this.handler = handler;
	}
	
	void setHandler(RecipeGeneratedCostHandler handler)
	{
		this.handler = handler;
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown)
	{
		return GristCostRecipe.scaleToCountAndDurability(getCost(input.getItem()), input, shouldRoundDown);
	}
	
	@Override
	public boolean matches(Container inv, Level level)
	{
		return getCost(inv.getItem(0).getItem()) != null;
	}
	
	@Override
	public int getPriority()
	{
		return -999999;	//Do not use Integer.MIN_VALUE as priority due to the risk of causing an underflow/overflow
	}
	
	@Nullable
	private GristSet getCost(Item item)
	{
		if(handler != null)
			return handler.getMap().get(item);
		else return null;
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer, ResourceLocation recipeId)
	{
		if(handler != null)
			handler.addAsProvider(consumer);
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		if(handler != null)
			return handler.createJeiCosts();
		else return Collections.emptyList();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.RECIPE_GRIST_COST.get();
	}
	
	public static class Serializer implements RecipeSerializer<RecipeGeneratedGristCost>
	{
		private static final Codec<RecipeGeneratedGristCost> CODEC = Codec.unit(RecipeGeneratedGristCost::new);
		
		@Override
		public Codec<RecipeGeneratedGristCost> codec()
		{
			return CODEC;
		}
		
		@Override
		public RecipeGeneratedGristCost fromNetwork(FriendlyByteBuf buffer)
		{
			return new RecipeGeneratedGristCost(RecipeGeneratedCostHandler.read(buffer));
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, RecipeGeneratedGristCost recipe)
		{
			if(recipe.handler != null)
				recipe.handler.write(buffer);
		}
	}
}