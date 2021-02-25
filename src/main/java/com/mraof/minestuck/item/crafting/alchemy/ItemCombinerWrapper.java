package com.mraof.minestuck.item.crafting.alchemy;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ItemCombinerWrapper extends RecipeWrapper implements ItemCombiner
{
	private final CombinationMode mode;
	
	public ItemCombinerWrapper(IItemHandlerModifiable inv, CombinationMode mode)
	{
		super(inv);
		this.mode = mode;
	}
	
	@Override
	public CombinationMode getMode()
	{
		return mode;
	}
}