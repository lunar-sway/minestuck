package com.mraof.minestuck.api.alchemy.recipe.combination;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record CombinationInput(ItemStack input1, ItemStack input2, CombinationMode mode) implements RecipeInput
{
	@Override
	public ItemStack getItem(int index)
	{
		return switch(index) {
			case 0 -> this.input1;
			case 1 -> this.input2;
			default -> throw new IndexOutOfBoundsException("No item for index " + index);
		};
	}
	
	@Override
	public int size()
	{
		return 2;
	}
}
