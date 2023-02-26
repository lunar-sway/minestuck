package com.mraof.minestuck.jei;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Created by mraof on 2017 January 23 at 6:50 AM.
 */
public class DesignixRecipeCategory implements IRecipeCategory<JeiCombination>
{
    private final IDrawable background, icon;

    DesignixRecipeCategory(IGuiHelper guiHelper)
    {
        ResourceLocation punchDesignixBackground = new ResourceLocation("minestuck:textures/gui/designix.png");
        background = guiHelper.createDrawable(punchDesignixBackground, 43, 25, 94, 42);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MSBlocks.PUNCH_DESIGNIX));
    }
	
	@Override
	public RecipeType<JeiCombination> getRecipeType()
	{
		return MinestuckJeiPlugin.DESIGNIX;
	}
	
	@SuppressWarnings("removal")
	@Override
	public Class<? extends JeiCombination> getRecipeClass()
	{
		return getRecipeType().getRecipeClass();
	}
	
	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid()
	{
		return getRecipeType().getUid();
	}

	@Override
	public Component getTitle()
	{
		return new TranslatableComponent(MSBlocks.PUNCH_DESIGNIX.KEYBOARD.get().getDescriptionId());
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public IDrawable getIcon()
	{
		return icon;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, JeiCombination recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getInput1());
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 25).addIngredients(recipe.getInput2());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 12).addItemStack(recipe.getOutput())
				.addItemStack(AlchemyHelper.createCard(recipe.getOutput(), true));
	}
}