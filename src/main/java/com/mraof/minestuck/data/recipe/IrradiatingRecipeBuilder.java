package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class IrradiatingRecipeBuilder implements RecipeBuilder
{
	private final Item result;
	private final Ingredient ingredient;
	private final float experience;
	private final int cookingTime;
	
	public IrradiatingRecipeBuilder(ItemLike result, Ingredient ingredient, float experience, int cookingTime)
	{
		this.result = result.asItem();
		this.ingredient = ingredient;
		this.experience = experience;
		this.cookingTime = cookingTime;
	}
	
	public static IrradiatingRecipeBuilder irradiating(Ingredient ingredient, ItemLike result, float experience, int cookingTime)
	{
		return new IrradiatingRecipeBuilder(result, ingredient, experience, cookingTime);
	}
	
	@Override
	public RecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RecipeBuilder group(@Nullable String group)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Item getResult()
	{
		return result;
	}
	
	@Override
	public void save(RecipeOutput output, ResourceLocation recipeId)
	{
		output.accept(new Result(recipeId, this.ingredient, this.result, this.experience, this.cookingTime));
	}
	
	private record Result(ResourceLocation id, Ingredient ingredient, Item result, float experience, int cookingTime) implements FinishedRecipe
	{
		@Override
		public void serializeRecipeData(JsonObject json)
		{
			json.addProperty("category", CookingBookCategory.MISC.getSerializedName());
			json.add("ingredient", this.ingredient.toJson(false));
			json.addProperty("result", BuiltInRegistries.ITEM.getKey(this.result).toString());
			json.addProperty("experience", this.experience);
			json.addProperty("cookingtime", this.cookingTime);
		}
		
		@Override
		public ResourceLocation id()
		{
			return this.id;
		}
		
		@Override
		public RecipeSerializer<?> type()
		{
			return MSRecipeTypes.IRRADIATING.get();
		}
		
		@Nullable
		@Override
		public AdvancementHolder advancement()
		{
			return null;
		}
	}
}
