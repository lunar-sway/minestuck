package com.mraof.minestuck.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CombinationRecipeCategory implements IRecipeCategory {
	private static final int recipeInputAIndex = 0;
	private static final int recipeInputBIndex = 1;
	private static final int recipeOutputIndex = 2;

	@Nonnull
	private final IDrawable background = JEIPluginManager.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation("minestuck", "textures/gui/Combination.png"), 43, 25, 94, 42);
	@Nonnull
	private final String localizedName = "Punch Card Alchemy";

	@Override
	public String getUid() {
		return "minestuck.combination";
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
		
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		if (recipeWrapper instanceof JEICombinationWrapper) {
			JEICombinationWrapper recipe = (JEICombinationWrapper) recipeWrapper;
			recipeLayout.getItemStacks().init(recipeInputAIndex, true, 0, 0);
			recipeLayout.getItemStacks().set(recipeInputAIndex, recipe.getInputs().get(0));
			recipeLayout.getItemStacks().init(recipeInputBIndex, true, 0, 24);
			recipeLayout.getItemStacks().set(recipeInputBIndex, recipe.getInputs().get(1));
			recipeLayout.getItemStacks().init(recipeOutputIndex, true, 72,11);
			recipeLayout.getItemStacks().set(recipeOutputIndex, recipe.getOutputs());
		}
	}
}