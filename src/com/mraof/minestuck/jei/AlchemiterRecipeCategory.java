package com.mraof.minestuck.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class AlchemiterRecipeCategory implements IRecipeCategory {
	private static final int recipeOutputIndex = 0;

	@Nonnull
	private final IDrawable background = JEIPluginManager.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation("minestuck", "textures/gui/alchemiter.png"), 7, 14, 162, 61);
	@Nonnull
	private final String localizedName = "Alchemiter";

	@Override
	public String getUid() {
		return "minestuck.alchemiter";
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
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		if (recipeWrapper instanceof JEIAlchemiterWrapper) {
			JEIAlchemiterWrapper recipe = (JEIAlchemiterWrapper) recipeWrapper;
			recipeLayout.getItemStacks().init(recipeOutputIndex, true, 127, 5);
			recipeLayout.getItemStacks().set(recipeOutputIndex, recipe.getOutputs());
		}
	}
}