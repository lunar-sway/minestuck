package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
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
	public boolean matches(SingleRecipeInput inv, Level level)
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
		private static final MapCodec<RecipeGeneratedGristCost> CODEC = MapCodec.unit(RecipeGeneratedGristCost::new);
		private static final StreamCodec<RegistryFriendlyByteBuf, RecipeGeneratedGristCost> STREAM_CODEC = StreamCodec.of(
				(encode, recipe) ->
				{
					if(recipe.handler != null)
						recipe.handler.write(encode);
				},
				(decode) -> new RecipeGeneratedGristCost(RecipeGeneratedCostHandler.read(decode))
		);
		
		@Override
		public MapCodec<RecipeGeneratedGristCost> codec()
		{
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeGeneratedGristCost> streamCodec()
		{
			return STREAM_CODEC;
		}
	}
}