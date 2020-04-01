package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiCombination;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCombinationRecipe implements IRecipe<ItemCombiner>
{
	private final ResourceLocation id;
	
	protected AbstractCombinationRecipe(ResourceLocation id)
	{
		this.id = id;
	}
	
	@Override
	public boolean canFit(int width, int height)
	{
		return width * height >= 2;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
	@Override
	public IRecipeType<?> getType()
	{
		return MSRecipeTypes.COMBINATION_TYPE;
	}
	
	public List<JeiCombination> getJeiCombinations()
	{
		return Collections.emptyList();
	}
}