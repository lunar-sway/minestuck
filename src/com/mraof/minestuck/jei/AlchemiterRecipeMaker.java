package com.mraof.minestuck.jei;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class AlchemiterRecipeMaker {
	@Nonnull
	public static List<JEIAlchemiterWrapper> getRecipes() {
		Hashtable<List<Object>, GristSet> gristRecipes = GristRegistry.getAllConversions();
		Set<List<Object>> gristRecipeKeys = gristRecipes.keySet();
		ArrayList<JEIAlchemiterWrapper> recipes = new ArrayList<JEIAlchemiterWrapper>();
		for(List<Object> entry : gristRecipeKeys){
			if (entry.get(0) instanceof Item) {
				ItemStack output = new ItemStack((Item) entry.get(0));
				output.setItemDamage((Integer)entry.get(1));
				GristSet gristCost = gristRecipes.get(entry);
				JEIAlchemiterWrapper recipe = new JEIAlchemiterWrapper(output,gristCost);
				recipes.add(recipe);
			}
			else if (entry.get(0) instanceof String) {
				List<ItemStack> outputs = OreDictionary.getOres((String) entry.get(0));
				for(ItemStack output : outputs) {
					GristSet gristCost = gristRecipes.get(entry);
					JEIAlchemiterWrapper recipe = new JEIAlchemiterWrapper(output,gristCost);
					recipes.add(recipe);
				}
			}
		}
		return recipes;
	}
}