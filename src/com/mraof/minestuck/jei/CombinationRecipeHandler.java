package com.mraof.minestuck.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class CombinationRecipeHandler implements IRecipeHandler<JEICombinationWrapper> {
	@Override
	public Class<JEICombinationWrapper> getRecipeClass() {
		return JEICombinationWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "minestuck.combination";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(JEICombinationWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(JEICombinationWrapper recipe) {
		return recipe.getInputs().size() > 1 && recipe.getOutputs().size() > 0;
	}
}