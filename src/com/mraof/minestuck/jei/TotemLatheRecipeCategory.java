package com.mraof.minestuck.jei;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mraof on 2017 January 23 at 6:50 AM.
 */
public class TotemLatheRecipeCategory extends BlankRecipeCategory<TotemLatheRecipeWrapper> implements IRecipeHandler<TotemLatheRecipeWrapper>
{
    private IDrawable background;

    TotemLatheRecipeCategory(IGuiHelper guiHelper)
    {
        ResourceLocation totemLatheBackground = new ResourceLocation("minestuck:textures/gui/lathe.png");
        background = guiHelper.createDrawable(totemLatheBackground, 25, 24, 130, 36);
    }

    @Override
    public String getUid()
    {
        return "totemLathe";
    }

    @Override
    public String getTitle()
    {
        return I18n.format("tile.sburbMachine.totemLathe.name");
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, TotemLatheRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        stackGroup.init(0, true, 0, 0);
        stackGroup.init(1, true, 0, 18);
        stackGroup.init(2, true, 36, 9);
        stackGroup.init(3, false, 107, 9);
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<ItemStack> first = new ArrayList<ItemStack>();
        for(ItemStack stack : inputs.get(0))
        {
            first.add(AlchemyRecipeHandler.createCard(stack, true));
        }
        stackGroup.set(0, first);

        List<ItemStack> second = new ArrayList<ItemStack>();
        for(ItemStack stack : inputs.get(1))
        {
            second.add(AlchemyRecipeHandler.createCard(stack, true));
        }
        stackGroup.set(1, second);
        stackGroup.set(2, inputs.get(2));
        List<ItemStack> outputs = new ArrayList<ItemStack>(ingredients.getOutputs(ItemStack.class));
        outputs.add(AlchemyRecipeHandler.createEncodedItem(outputs.get(0), false));
        stackGroup.set(3, outputs);
    }

    /**
     * Returns the class of the Recipe handled by this IRecipeHandler.
     */
    @Override
    public Class<TotemLatheRecipeWrapper> getRecipeClass()
    {
        return TotemLatheRecipeWrapper.class;
    }

    @Override
    public String getRecipeCategoryUid()
    {
        return "totemLathe";
    }

    @Override
    public String getRecipeCategoryUid(TotemLatheRecipeWrapper recipe)
    {
        return "totemLathe";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(TotemLatheRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(TotemLatheRecipeWrapper recipe)
    {
        return true;
    }
}
