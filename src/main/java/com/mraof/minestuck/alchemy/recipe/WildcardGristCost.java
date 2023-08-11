package com.mraof.minestuck.alchemy.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristSet;
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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class WildcardGristCost implements GristCostRecipe
{
	private final ResourceLocation id;
	private final Ingredient ingredient;
	private final Integer priority;
	private final long wildcardCost;
	
	public WildcardGristCost(ResourceLocation id, Ingredient ingredient, long wildcardCost, @Nullable Integer priority)
	{
		this.id = id;
		this.ingredient = ingredient;
		this.priority = priority;
		this.wildcardCost = wildcardCost;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return this.id;
	}
	
	@Override
	public boolean matches(Container inv, Level level)
	{
		return ingredient.test(inv.getItem(0));
	}
	
	@Override
	public int getPriority()
	{
		return Objects.requireNonNullElseGet(this.priority, () -> GristCostRecipe.defaultPriority(this.ingredient));
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown)
	{
		return wildcardType != null ? GristCostRecipe.scaleToCountAndDurability(new GristAmount(wildcardType, wildcardCost), input, shouldRoundDown) : null;
	}
	
	@Override
	public boolean canPickWildcard()
	{
		return true;
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		GristCostRecipe.addSimpleCostProvider(consumer, this, this.ingredient);
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		return Collections.singletonList(new JeiGristCost.Wildcard(ingredient, wildcardCost));
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.WILDCARD_GRIST_COST.get();
	}
	
	public static class Serializer implements RecipeSerializer<WildcardGristCost>
	{
		@Override
		public WildcardGristCost fromJson(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
			Integer priority = json.has("priority") ? GsonHelper.getAsInt(json, "priority") : null;
			
			long wildcardCost1 = GsonHelper.getAsLong(json, "grist_cost");
			
			return new WildcardGristCost(recipeId, ingredient, wildcardCost1, priority);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, WildcardGristCost recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
			buffer.writeLong(recipe.wildcardCost);
		}
		
		@Nullable
		@Override
		public WildcardGristCost fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			long wildcardCost = buffer.readLong();
			
			return new WildcardGristCost(recipeId, ingredient, wildcardCost, priority);
		}
	}
}
