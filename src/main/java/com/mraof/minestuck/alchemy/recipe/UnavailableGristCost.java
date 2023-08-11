package com.mraof.minestuck.alchemy.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
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
import java.util.Objects;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class UnavailableGristCost implements GristCostRecipe
{
	private final ResourceLocation id;
	private final Ingredient ingredient;
	@Nullable
	private final Integer priority;
	
	public UnavailableGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority)
	{
		this.id = id;
		this.ingredient = ingredient;
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
		return ingredient.test(inv.getItem(0));
	}
	
	@Override
	public int getPriority()
	{
		return Objects.requireNonNullElseGet(this.priority, () -> GristCostRecipe.defaultPriority(this.ingredient));
	}
	
	@Nullable
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown)
	{
		return null;
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		GristCostRecipe.addSimpleCostProvider(consumer, this, this.ingredient);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.UNAVAILABLE_GRIST_COST.get();
	}
	
	public static class Serializer implements RecipeSerializer<UnavailableGristCost>
	{
		@Override
		public UnavailableGristCost fromJson(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
			Integer priority = json.has("priority") ? GsonHelper.getAsInt(json, "priority") : null;
			
			return new UnavailableGristCost(recipeId, ingredient, priority);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, UnavailableGristCost recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
		}
		
		@Nullable
		@Override
		public UnavailableGristCost fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			
			return new UnavailableGristCost(recipeId, ingredient, priority);
		}
	}
}
