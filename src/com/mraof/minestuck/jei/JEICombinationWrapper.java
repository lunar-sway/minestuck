package com.mraof.minestuck.jei;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class JEICombinationWrapper extends BlankRecipeWrapper {
	@Nonnull
	private final ItemStack output;
	@Nonnull
	private final List<ItemStack> inputs;
	@Nonnull
	private final boolean mode;

	public JEICombinationWrapper(@Nonnull ItemStack output, @Nonnull List<ItemStack> inputs, @Nonnull boolean mode) {
		this.output = output;
		this.inputs = inputs;
		this.mode = mode;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return Collections.singletonList(output);
	}

	@Override
	public List<ItemStack> getInputs() {
		return inputs;
	}

	@Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
		minecraft.fontRendererObj.drawString(mode ? "&&" : "||", 21, 7, 0x8B8B8B);
    }
}