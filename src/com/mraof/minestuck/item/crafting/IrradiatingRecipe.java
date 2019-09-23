package com.mraof.minestuck.item.crafting;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Optional;

public class IrradiatingRecipe extends AbstractCookingRecipe
{
	public IrradiatingRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn)
	{
		super(MSRecipeTypes.IRRADIATING_TYPE, idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);
	}
	
	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(MSBlocks.URANIUM_COOKER);
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.IRRADIATING;
	}
	
	public Optional<? extends AbstractCookingRecipe> getCookingRecipe(IInventory inventory, World world)
	{
		return Optional.of(this);
	}
	
	public boolean isFallback()
	{
		return false;
	}
}
