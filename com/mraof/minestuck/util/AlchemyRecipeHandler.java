package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AlchemyRecipeHandler {
	
	public static void registerVanillaRecipes() {
		
		//Set up Alchemiter recipes
		GristRegistry.addGristConversion(new ItemStack(Block.wood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8})); 
		GristRegistry.addGristConversion(new ItemStack(Block.sapling), false, new GristSet(new GristType[] {GristType.Build}, new int[] {16})); 

		//Set up Punch Designex recipes
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
		
		GristRegistry.addGristConversion(new ItemStack(Minestuck.blockStorage, 1, 1), true, new GristSet(new GristType[] {GristType.Build}, new int[] {2})); 
		
	}
	
	public static void registerModRecipes() 
	{
		try 
		{
			if(Loader.isModLoaded("IronChest"))
			{
				Block ironChest = ((Block) (Class.forName("cpw.mods.ironchest.IronChest").getField("ironChestBlock").get(null)));
				GristRegistry.addGristConversion(ironChest.blockID, 0, true, new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {16, 128}));
				CombinationRegistry.addCombination(new ItemStack(Block.chest), new ItemStack(Item.ingotIron), true, new ItemStack(ironChest, 1, 0));
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
}
