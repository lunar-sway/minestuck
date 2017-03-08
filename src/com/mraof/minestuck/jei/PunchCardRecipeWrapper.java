package com.mraof.minestuck.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by mraof on 2017 January 23 at 4:37 PM.
 */
public abstract class PunchCardRecipeWrapper extends BlankRecipeWrapper
{
    private final List<List<ItemStack>> inputs;
    private final ItemStack result;

    PunchCardRecipeWrapper(List<List<ItemStack>> inputs, ItemStack result)
    {
        this.inputs = inputs;
        this.result = result;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, result);
    }
}
