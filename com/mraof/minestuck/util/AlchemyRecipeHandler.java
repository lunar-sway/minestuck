package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AlchemyRecipeHandler {
	
	public static void registerVanillaRecipes() {
		
		//Set up Alchemiter recipes
		GristRegistry.addGristConversion(new ItemStack(Block.wood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8})); 
		GristRegistry.addGristConversion(new ItemStack(Block.sapling), false, new GristSet(new GristType[] {GristType.Build}, new int[] {16})); 

		//Set up Punch Designex recipesv
		for(int metadata = 0; metadata < BlockSapling.WOOD_TYPES.length; metadata++)
		{
			CombinationRegistry.addCombination(new ItemStack(Block.sapling, 1, metadata), new ItemStack(Block.wood), true, true, false, new ItemStack(Block.wood, 1, metadata));
			CombinationRegistry.addCombination(new ItemStack(Block.wood, 1, metadata), new ItemStack(Block.sapling), false, true, false, new ItemStack(Block.sapling, 1, metadata));
		}
	}
	
	public static void registerMinestuckRecipes() {
		
		//set up vanilla recipes
		GameRegistry.addRecipe(new ItemStack(Minestuck.blockStorage,1,0),new Object[]{ "XXX","XXX","XXX",'X',new ItemStack(Minestuck.rawCruxite, 1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.blankCard,8,0),new Object[]{ "XXX","XYX","XXX",'Y',new ItemStack(Minestuck.rawCruxite, 1),'X',new ItemStack(Item.paper,1)});
		
		
	}
	
	public static void registerModRecipes() {
		
	}
	
}
