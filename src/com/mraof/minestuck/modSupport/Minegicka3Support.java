package com.mraof.minestuck.modSupport;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.alchemy.AlchemyCostRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.item.Items;

public class Minegicka3Support extends ModSupport
{
	
	@Override
	public void registerRecipes() throws Exception
	{//TODO Get rid of this reflection and use registry names instead! This is something that should've happened already
		Item thingy = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("thingy").get(null)));
		Item thingy2 = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("thingyGood").get(null)));
		Item thingy3 = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("thingySuper").get(null)));
		
		Item stick = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("stick").get(null)));
		Item stick2 = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("stickGood").get(null)));
		Item stick3 = ((Item) (Class.forName("com.williameze.minegicka3.ModBase").getField("stickSuper").get(null)));
		
		AlchemyCostRegistry.addGristConversion(thingy, new GristSet(new GristType[]{GristType.RUST, GristType.GOLD}, new int[]{16, 16}));
		
		CombinationRegistry.addCombination(thingy, Items.STICK, CombinationRegistry.Mode.MODE_AND, new ItemStack(stick));
		CombinationRegistry.addCombination(thingy2, Items.STICK, CombinationRegistry.Mode.MODE_AND, new ItemStack(stick2));
		CombinationRegistry.addCombination(thingy3, Items.STICK, CombinationRegistry.Mode.MODE_AND, new ItemStack(stick3));
		
	}
	
	@Override
	public void registerDynamicRecipes() throws Exception
	{
		Debug.debug("Adding minegicka 3 recipes...");
		Map<Integer, Object> recipes = ((Map<Integer, Object>) (Class.forName("com.williameze.minegicka3.mechanics.ClickCraft").getField("recipes").get(null)));
		
		recipes: for(Entry<Integer, Object> entry : (Set<Entry<Integer, Object>>) recipes.entrySet())
		{
			List<Entry<ItemStack, Integer>> input = (List<Entry<ItemStack, Integer>>) entry.getValue().getClass().getField("input").get(entry.getValue());
			ItemStack output = (ItemStack) entry.getValue().getClass().getField("output").get(entry.getValue());
			
			if(AlchemyCostRegistry.getGristConversion(output) == null)
			{
				GristSet cost = new GristSet();
				for(Entry<ItemStack, Integer> ingredient : input)
				{
					GristSet set = AlchemyCostRegistry.getGristConversion(ingredient.getKey());
					if(set != null)
					{
						set = set.scaleGrist(ingredient.getValue());
						cost.addGrist(set);
					}
					else continue recipes;
				}
				if(!cost.isEmpty())
					AlchemyCostRegistry.addGristConversion(output.getItem(), cost);
			}
		}
	}
	
}
