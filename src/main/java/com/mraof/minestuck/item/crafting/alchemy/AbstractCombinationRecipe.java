package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiCombination;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCombinationRecipe implements Recipe<ItemCombiner>
{
	private final ResourceLocation id;
	
	protected AbstractCombinationRecipe(ResourceLocation id)
	{
		this.id = id;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return width * height >= 2;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
	@Override
	public RecipeType<?> getType()
	{
		return MSRecipeTypes.COMBINATION_TYPE;
	}
	
	public List<JeiCombination> getJeiCombinations()
	{
		return Collections.emptyList();
	}
}