package com.mraof.minestuck.item.crafting;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Optional;

@MethodsReturnNonnullByDefault
public class IrradiatingRecipe extends AbstractCookingRecipe
{
	public IrradiatingRecipe(String groupIn, CookingBookCategory category, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn)
	{
		super(MSRecipeTypes.IRRADIATING_TYPE.get(), groupIn, category, ingredientIn, resultIn, experienceIn, cookTimeIn);
	}
	
	@Override
	public ItemStack getToastSymbol()
	{
		return new ItemStack(MSBlocks.URANIUM_COOKER.get());
	}
	
	@Override
	public boolean isSpecial()	//Makes sure that the recipe is not unlockable (because recipe book categories are hardcoded to vanilla categories)
	{
		return true;
	}
	
	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.IRRADIATING.get();
	}
	
	public Optional<? extends AbstractCookingRecipe> getCookingRecipe(Container container, Level level)
	{
		return Optional.of(this);
	}
	
	public boolean isFallback()
	{
		return false;
	}
}
