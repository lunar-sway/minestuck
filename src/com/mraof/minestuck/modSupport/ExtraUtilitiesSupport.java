package com.mraof.minestuck.modSupport;

import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.alchemy.AlchemyCostRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ExtraUtilitiesSupport extends ModSupport
{
	
	@Override
	public void registerRecipes()
	{
		Item enderLily = ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2", "enderlilly"));
		
		if(enderLily != null)
		{
			AlchemyCostRegistry.addGristConversion(enderLily, new GristSet(new GristType[]{GristType.URANIUM, GristType.IODINE}, new int[]{24, 6}));
			CombinationRegistry.addCombination(Items.WHEAT_SEEDS, Items.ENDER_PEARL, CombinationRegistry.Mode.MODE_OR,  new ItemStack(enderLily));
			CombinationRegistry.addCombination(Items.WHEAT_SEEDS, Items.ENDER_EYE, CombinationRegistry.Mode.MODE_OR,  new ItemStack(enderLily));	//Might as well do this too
		}
	}
}
