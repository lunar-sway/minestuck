package com.mraof.minestuck.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class AlchemiterRecipeHandler implements IRecipeHandler<JEIAlchemiterWrapper> {
	@Nonnull
	@Override
	public Class<JEIAlchemiterWrapper> getRecipeClass() {
		return JEIAlchemiterWrapper.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return "minestuck.alchemiter";
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull JEIAlchemiterWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull JEIAlchemiterWrapper recipe) {
		return recipe.getOutputs().size() > 0;
	}
}