package com.mraof.minestuck.jei;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mraof on 2017 January 23 at 6:53 AM.
 */
class TotemLatheRecipeWrapper extends PunchCardRecipeWrapper
{
    private static final List<ItemStack> dowel;
    static {
        dowel = new ArrayList<>();
        dowel.add(new ItemStack(MinestuckItems.cruxiteDowel));
    }

    TotemLatheRecipeWrapper(List<ItemStack> first, List<ItemStack> second, ItemStack result)
    {
        super(makeInputs(first, second), result);
    }

    private static List<List<ItemStack>> makeInputs(List<ItemStack> first, List<ItemStack> second)
    {
        ArrayList<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(first);
        inputs.add(second);
        inputs.add(dowel);
        return inputs;
    }
}
