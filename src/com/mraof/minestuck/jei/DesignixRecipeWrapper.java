package com.mraof.minestuck.jei;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mraof on 2017 January 23 at 6:53 AM.
 */
class DesignixRecipeWrapper extends PunchCardRecipeWrapper
{
    DesignixRecipeWrapper(List<ItemStack> first, List<ItemStack> second, ItemStack result)
    {
        super(makeInputs(first, second), result);
    }

    private static List<List<ItemStack>> makeInputs(List<ItemStack> first, List<ItemStack> second)
    {
        ArrayList<List<ItemStack>> inputs = new ArrayList<List<ItemStack>>();
        first.add(AlchemyRecipeHandler.createCard(first.get(0), false));
        inputs.add(first);
        inputs.add(second);
        return inputs;
    }
}
