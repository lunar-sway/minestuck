package com.mraof.minestuck.alchemy.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.alchemy.recipe.generator.GenerationContext;
import com.mraof.minestuck.alchemy.recipe.generator.GristCostResult;
import com.mraof.minestuck.api.alchemy.GristTypes;
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
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SimpleGristCost extends GristCostRecipe
{
	protected final Ingredient ingredient;
	@Nullable
	protected final Integer priority;
	
	public SimpleGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority)
	{
		super(id);
		this.ingredient = ingredient;
		this.priority = priority;
	}
	
	@Override
	public boolean matches(Container inv, Level level)
	{
		return ingredient.test(inv.getItem(0));
	}
	
	@Override
	public int getPriority()
	{
		if(priority == null)
			return priorityFromIngredient(ingredient);
		else return priority;
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		GeneratedCostProvider provider = new DefaultProvider();
		for(ItemStack stack : ingredient.getItems())
			consumer.accept(stack.getItem(), provider);
	}
	
	private class DefaultProvider implements GeneratedCostProvider
	{
		@Nullable
		@Override
		public GristCostResult generate(Item item, @Nullable GristCostResult lastCost, GenerationContext context)
		{
			if(lastCost == null && ingredient.test(new ItemStack(item)))
				return new GristCostResult(getGristCost(new ItemStack(item), GristTypes.BUILD.get(), false, null));
			else return null;
		}
	}
	
	//Helper class for implementing serializer classes
	public abstract static class AbstractSerializer<T extends SimpleGristCost> implements RecipeSerializer<T>
	{
		@Override
		public T fromJson(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
			Integer priority = json.has("priority") ? GsonHelper.getAsInt(json, "priority") : null;
			
			return read(recipeId, json, ingredient, priority);
		}
		
		protected abstract T read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, @Nullable Integer priority);
		
		@Nullable
		@Override
		public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			
			return read(recipeId, buffer, ingredient, priority);
		}
		
		protected abstract T read(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority);
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, T recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
		}
	}
}
