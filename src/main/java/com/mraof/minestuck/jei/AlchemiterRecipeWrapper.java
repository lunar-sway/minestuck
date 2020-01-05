package com.mraof.minestuck.jei;
/*
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by mraof on 2017 January 23 at 2:41 AM.
 * /
public class AlchemiterRecipeWrapper implements IRecipeWrapper
{
    private final ItemStack stack;
    private final GristSet grist;

    AlchemiterRecipeWrapper(ItemStack stack, GristSet grist)
    {
        this.stack = stack;
        this.grist = grist;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInput(ItemStack.class, AlchemyRecipes.createEncodedItem(stack, false));
        ingredients.setInputs(GristAmount.class, grist.getArray());
        ingredients.setOutput(ItemStack.class, stack);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        GuiUtil.drawGristBoard(grist, GuiUtil.GristboardMode.ALCHEMITER, 1, 30, minecraft.fontRenderer);
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        return GuiUtil.getGristboardTooltip(grist, mouseX, mouseY, 1, 30, Minecraft.getMinecraft().fontRenderer);
    }

    @Override
    public String toString()
    {
        return "AlchemiterRecipeWrapper{" +
                "stack=" + stack +
                ", grist=" + grist +
                '}';
    }
}
*/