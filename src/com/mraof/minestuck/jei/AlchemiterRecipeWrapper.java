package com.mraof.minestuck.jei;

import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristSet;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by mraof on 2017 January 23 at 2:41 AM.
 */
public class AlchemiterRecipeWrapper extends BlankRecipeWrapper
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
        ingredients.setInput(ItemStack.class, AlchemyRecipeHandler.createEncodedItem(stack, false));
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
