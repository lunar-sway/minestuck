package com.mraof.minestuck.item;

import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.item.ItemStack;

/**
 * Implement this if the item should be alchemized with a color.
 * The logic for actually setting the color is stored in {@link com.mraof.minestuck.util.ColorHandler}
 */
public interface AlchemizedColored
{
	default ItemStack setColor(ItemStack stack, int color)
	{
		return ColorHandler.setStackColor(stack, color);
	}
}
