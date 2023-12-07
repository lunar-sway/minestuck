package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public interface AdvancementFreeRecipe extends FinishedRecipe
{
	@Nullable
	@Override
	default JsonObject serializeAdvancement()
	{
		return null;
	}
	@Nullable
	@Override
	default ResourceLocation getAdvancementId()
	{
		return null;
	}
}
