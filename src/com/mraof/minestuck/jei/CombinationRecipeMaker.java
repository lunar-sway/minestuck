package com.mraof.minestuck.jei;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.mraof.minestuck.util.CombinationRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CombinationRecipeMaker {
	@Nonnull
	public static List<JEICombinationWrapper> getRecipes() {
		Hashtable<List<Object>, ItemStack> cRecipes = CombinationRegistry.getAllConversions();
		Set<List<Object>> cRecipeKeys = cRecipes.keySet();
		ArrayList<JEICombinationWrapper> recipes = new ArrayList<JEICombinationWrapper>();
		for(List<Object> entry : cRecipeKeys){
			ItemStack inputA = null;
			ItemStack inputB = null;
			if (entry.get(0) instanceof Item) {
				inputA = new ItemStack((Item) entry.get(0));
				inputA.setItemDamage((Integer)entry.get(1));
				if (entry.get(2) instanceof Item) {
					inputB = new ItemStack((Item) entry.get(2));
					inputB.setItemDamage((Integer)entry.get(3));
					List<ItemStack> pairing = new ArrayList();
					pairing.add(inputA);
					pairing.add(inputB);
					JEICombinationWrapper recipe = new JEICombinationWrapper(CombinationRegistry.getCombination(inputA,inputB,(Boolean)entry.get(4)), pairing, (Boolean) entry.get(4));
					recipes.add(recipe);
				}
				else if (entry.get(2) instanceof String) {
					List<ItemStack> inputsB = OreDictionary.getOres((String) entry.get(2));
					for(ItemStack iB : inputsB) {
						List<ItemStack> pairing = new ArrayList();
						pairing.add(inputA);
						pairing.add(iB);
						JEICombinationWrapper recipe = new JEICombinationWrapper(CombinationRegistry.getCombination(inputA,iB,(Boolean)entry.get(4)), pairing, (Boolean) entry.get(4));
						recipes.add(recipe);
					}
				}
			}
			else if (entry.get(0) instanceof String) {
				List<ItemStack> inputsA = OreDictionary.getOres((String) entry.get(0));
				for(ItemStack iA : inputsA) {
					if (entry.get(2) instanceof Item) {
						inputB = new ItemStack((Item) entry.get(2));
						inputB.setItemDamage((Integer)entry.get(3));
						List<ItemStack> pairing = new ArrayList();
						pairing.add(iA);
						pairing.add(inputB);
						JEICombinationWrapper recipe = new JEICombinationWrapper(CombinationRegistry.getCombination(iA,inputB,(Boolean)entry.get(4)), pairing, (Boolean) entry.get(4));
						recipes.add(recipe);
					}
					else if (entry.get(2) instanceof String) {
						List<ItemStack> inputsB = OreDictionary.getOres((String) entry.get(2));
						for(ItemStack iB : inputsB) {
							List<ItemStack> pairing = new ArrayList();
							pairing.add(iA);
							pairing.add(iB);
							JEICombinationWrapper recipe = new JEICombinationWrapper(CombinationRegistry.getCombination(iA,iB,(Boolean)entry.get(4)), pairing, (Boolean) entry.get(4));
							recipes.add(recipe);
						}
					}
				}
			}
		}
		return recipes;
	}
}