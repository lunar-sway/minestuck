package com.mraof.minestuck.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * Created by mraof on 2017 January 23 at 2:38 AM.
 */
public class AlchemiterRecipeCategory extends BlankRecipeCategory<AlchemiterRecipeWrapper> implements IRecipeHandler<AlchemiterRecipeWrapper>
{
    private IDrawable background;

    AlchemiterRecipeCategory(IGuiHelper guiHelper)
    {
        ResourceLocation alchemiterBackground = new ResourceLocation("minestuck:textures/gui/alchemiter.png");
        background = guiHelper.createDrawable(alchemiterBackground, 8, 15, 160, 56);
    }

    @Override
    public String getUid()
    {
        return "alchemiter";
    }

    @Override
    public String getTitle()
    {
        return I18n.format("tile.sburbMachine.alchemiter.name");
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlchemiterRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        recipeLayout.getItemStacks().init(0, true, 18, 4);
        recipeLayout.getItemStacks().init(1, false, 126, 4);
        recipeLayout.getItemStacks().set(ingredients);
    }

    /**
     * Returns the class of the Recipe handled by this IRecipeHandler.
     */
    @Override
    public Class<AlchemiterRecipeWrapper> getRecipeClass()
    {
        return AlchemiterRecipeWrapper.class;
    }

    @Override
    public String getRecipeCategoryUid()
    {
        return "alchemiter";
    }

    @Override
    public String getRecipeCategoryUid(AlchemiterRecipeWrapper recipe)
    {
        return "alchemiter";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(AlchemiterRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(AlchemiterRecipeWrapper recipe)
    {
        return true;
    }
}
