package com.mraof.minestuck.jei;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ColorCollector;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by mraof on 2017 January 23 at 2:38 AM.
 */
public class AlchemiterRecipeCategory implements IRecipeCategory<AlchemiterRecipeWrapper>
{
    private IDrawable background;

    AlchemiterRecipeCategory(IGuiHelper guiHelper)
    {
        ResourceLocation alchemiterBackground = new ResourceLocation("minestuck:textures/gui/alchemiter.png");
        background = guiHelper.createDrawable(alchemiterBackground, 8, 15, 160, 56);
    }
	
	@Override
	public String getModName()
	{
		return Minestuck.MOD_NAME;
	}
	
	@Override
    public String getUid()
    {
        return "minestuck.alchemiter";
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
        ItemStack inputDowel = ingredients.getInputs(ItemStack.class).get(0).get(0).copy();
        inputDowel.setItemDamage(ColorCollector.playerColor + 1);
        recipeLayout.getItemStacks().set(0, inputDowel);
        recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));
    }
}
