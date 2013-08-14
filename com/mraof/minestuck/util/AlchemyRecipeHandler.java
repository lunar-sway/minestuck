package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.lands.LandAspectFrost;
import com.mraof.minestuck.world.gen.lands.LandAspectHelper;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
		GameRegistry.addRecipe(new ItemStack(Minestuck.disk,1,0),new Object[]{ " X ","XYX"," X ",'X',new ItemStack(Minestuck.rawCruxite, 1),'Y',new ItemStack(Item.ingotIron,1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.disk,1,1),new Object[]{ "X X"," Y ","X X",'X',new ItemStack(Minestuck.rawCruxite, 1),'Y',new ItemStack(Item.ingotIron,1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.blockComputer,1,0),new Object[]{ "XXX","XYX","XXX",'Y',new ItemStack(Minestuck.blockStorage, 1, 0),'X',new ItemStack(Item.ingotIron,1)});
		
		
		//add grist conversions
		GristRegistry.addGristConversion(new ItemStack(Minestuck.blockStorage, 1, 1), true, new GristSet(new GristType[] {GristType.Build}, new int[] {2})); 
		
		//register land aspects
		LandAspectHelper.registerLandAspect(new LandAspectFrost());
		
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
	
	/**
	 * Given a punched card or a carved dowel, returns a new item that represents the encoded data.
	 * 
	 * @param card - The dowel or card with encoded data
	 * @return An item, or null if the data was invalid.
	 */
	public static ItemStack getDecodedItem(ItemStack card) {
		
		if (card == null) {return null;}
		//System.out.println("[MINESTUCK] Looking for an ID of" +  Minestuck.cruxiteDowel.itemID + ". Got an ID of " + card.itemID);
		if (card.itemID == Minestuck.cruxiteDowel.itemID) {
			//System.out.println("[MINESTUCK] Got a blank dowel as input. Returning a generic object");
			return new ItemStack(Minestuck.blockStorage,1,1); //return a Perfectly Generic Object if it's a blank dowel
		}
		
		//System.out.println("[MINESTUCK] Got a carved dowel. Returning encoded object");
		NBTTagCompound tag = card.getTagCompound();
		
		if (tag == null || Item.itemsList[tag.getInteger("contentID")] == null) {return null;}
		ItemStack newItem = new ItemStack(tag.getInteger("contentID"),1,tag.getInteger("contentMeta"));
		
		return newItem;
		
	}
	
}
