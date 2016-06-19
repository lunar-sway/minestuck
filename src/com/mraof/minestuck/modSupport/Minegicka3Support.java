package com.mraof.minestuck.modSupport;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public class Minegicka3Support extends ModSupport
{
	
	public void registerRecipes() throws Exception
	{
		Item thingy = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("thingy").get(null)));
		Item thingy2 = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("thingyGood").get(null)));
		Item thingy3 = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("thingySuper").get(null)));
		
		Item stick = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("stick").get(null)));
		Item stick2 = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("stickGood").get(null)));
		Item stick3 = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("stickSuper").get(null)));
		
		GristRegistry.addGristConversion(new ItemStack(thingy), false, new GristSet(new GristType[]{GristType.Rust, GristType.Gold}, new int[]{16, 16}));
		
		CombinationRegistry.addCombination(new ItemStack(thingy), new ItemStack(Items.STICK), CombinationRegistry.MODE_AND, new ItemStack(stick));
		CombinationRegistry.addCombination(new ItemStack(thingy2), new ItemStack(Items.STICK), CombinationRegistry.MODE_AND, new ItemStack(stick2));
		CombinationRegistry.addCombination(new ItemStack(thingy3), new ItemStack(Items.STICK), CombinationRegistry.MODE_AND, new ItemStack(stick3));
		
	}
	
	public void registerDynamicRecipes() throws Exception
	{
		Debug.debug("Adding minegicka 3 recipes...");
		Map<Integer, Object> recipes = ((Map<Integer, Object>) (Class.forName("com.williameze.minegicka3.mechanics.ClickCraft").getField("recipes").get(null)));
		
		recipes: for(Entry<Integer, Object> entry : (Set<Entry<Integer, Object>>) recipes.entrySet())
		{
			List<Entry<ItemStack, Integer>> input = (List<Entry<ItemStack, Integer>>) entry.getValue().getClass().getField("input").get(entry.getValue());
			ItemStack output = (ItemStack) entry.getValue().getClass().getField("output").get(entry.getValue());
			
			if(GristRegistry.getGristConversion(output) == null)
			{
				GristSet cost = new GristSet();
				for(Entry<ItemStack, Integer> ingredient : input)
				{
					GristSet set = GristRegistry.getGristConversion(ingredient.getKey());
					if(set != null)
					{
						set = set.scaleGrist(ingredient.getValue());
						cost.addGrist(set);
					}
					else continue recipes;
				}
				if(!cost.isEmpty())
					GristRegistry.addGristConversion(output, cost);
			}
		}
	}
	
}
