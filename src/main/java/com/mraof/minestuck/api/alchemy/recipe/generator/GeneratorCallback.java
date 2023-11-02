package com.mraof.minestuck.api.alchemy.recipe.generator;

import com.mraof.minestuck.api.alchemy.GristSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;

public interface GeneratorCallback
{
	boolean shouldUseSavedResult();
	
	boolean shouldSaveResult();
	
	@Nullable
	GristSet lookupCostFor(Ingredient ingredient);
	
	@Nullable
	default GristSet lookupCostFor(ItemStack stack)
	{
		return this.lookupCostFor(stack.getItem());
	}
	
	@Nullable
	GristSet lookupCostFor(Item item);
}
