package com.mraof.minestuck.jei;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

class UraniumCookerRecipeWrapper extends PunchCardRecipeWrapper
{
	static ArrayList<ItemStack> fuel;
	static {
		fuel = new ArrayList<>();
		fuel.add(new ItemStack(MinestuckItems.rawUranium));
	}
	
    private static final List<ItemStack> dowel;
    static {
        dowel = new ArrayList<>();
        dowel.add(new ItemStack(MinestuckItems.cruxiteDowel));
    }

    UraniumCookerRecipeWrapper(List<ItemStack> first, ItemStack result)
    {
        super(makeInputs(first, fuel), result);
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
