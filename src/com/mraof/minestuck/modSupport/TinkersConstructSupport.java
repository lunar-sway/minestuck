package com.mraof.minestuck.modSupport;

import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.alchemy.GristRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TinkersConstructSupport extends ModSupport
{
	
	@Override
	public void registerRecipes() throws Exception
	{
		Item edible = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", "edible"));
		Block grout = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("tconstruct", "soil"));
		
		GristRegistry.addGristConversion(new ItemStack(edible, 1, 1), true, new GristSet(new GristType[] {GristType.Caulk, GristType.Amethyst}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(edible, 1, 2), true, new GristSet(new GristType[] {GristType.Caulk, GristType.Amethyst, GristType.Garnet}, new int[] {4, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(edible, 1, 4), true, new GristSet(new GristType[] {GristType.Caulk, GristType.Amber, GristType.Garnet}, new int[] {4, 2, 2}));
		
		
		CombinationRegistry.addCombination(new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.SLIME_BALL), CombinationRegistry.Mode.MODE_OR, true, false, new ItemStack(edible, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Items.DYE, 1, 5), new ItemStack(Items.SLIME_BALL), CombinationRegistry.Mode.MODE_OR, true, false, new ItemStack(edible, 1, 2));
		CombinationRegistry.addCombination(new ItemStack(Items.DYE, 1, 14), new ItemStack(Items.SLIME_BALL), CombinationRegistry.Mode.MODE_OR, true, false, new ItemStack(edible, 1, 4));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), CombinationRegistry.Mode.MODE_OR, new ItemStack(grout, 1, 0));
	}
	
}
