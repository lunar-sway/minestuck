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
		//Ore bushes are gone
		Block oreBush1 = ((Block) Class.forName("tconstruct.world.TinkerWorld").getField("oreBerry").get(null));
		Block oreBush2 = ((Block) Class.forName("tconstruct.world.TinkerWorld").getField("oreBerrySecond").get(null));
		
		Item edible = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tocnstruct", "edible"));
		Block grout = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("tconstruct", "soil"));
		
		String[] items1 = {"ingotIron", "ingotGold", "ingotCopper", "ingotTin"};
		
		GristRegistry.addGristConversion(new ItemStack(oreBush1, 1, 0), new GristSet(new GristType[]{GristType.Rust, GristType.Build, GristType.Amber}, new int[]{16, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush1, 1, 1), new GristSet(new GristType[]{GristType.Gold, GristType.Build, GristType.Amber}, new int[]{16, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush1, 1, 2), new GristSet(new GristType[]{GristType.Rust, GristType.Cobalt, GristType.Build, GristType.Amber}, new int[]{16, 3, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush1, 1, 3), new GristSet(new GristType[]{GristType.Rust, GristType.Caulk, GristType.Build, GristType.Amber}, new int[]{16, 8, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush2, 1, 0), new GristSet(new GristType[]{GristType.Rust, GristType.Chalk, GristType.Build, GristType.Amber}, new int[]{16, 6, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush2, 1, 1), new GristSet(new GristType[]{GristType.Uranium, GristType.Quartz, GristType.Diamond, GristType.Ruby, GristType.Build, GristType.Amber}, new int[]{8, 1, 4, 4, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(edible, 1, 1), true, new GristSet(new GristType[] {GristType.Caulk, GristType.Amethyst}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(edible, 1, 1), true, new GristSet(new GristType[] {GristType.Caulk, GristType.Amethyst, GristType.Garnet}, new int[] {4, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(edible, 1, 1), true, new GristSet(new GristType[] {GristType.Caulk, GristType.Amber, GristType.Garnet}, new int[] {4, 2, 2}));
		
		for(int i = 0; i < items1.length; i++)
			CombinationRegistry.addCombination("treeLeaves", items1[i], CombinationRegistry.Mode.MODE_AND, new ItemStack(oreBush1, 1, i));
		
		CombinationRegistry.addCombination(new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.SLIME_BALL), CombinationRegistry.Mode.MODE_OR, true, false, new ItemStack(edible, 1, 1));
		CombinationRegistry.addCombination(new ItemStack(Items.DYE, 1, 5), new ItemStack(Items.SLIME_BALL), CombinationRegistry.Mode.MODE_OR, true, false, new ItemStack(edible, 1, 2));
		CombinationRegistry.addCombination(new ItemStack(Items.DYE, 1, 14), new ItemStack(Items.SLIME_BALL), CombinationRegistry.Mode.MODE_OR, true, false, new ItemStack(edible, 1, 4));
		CombinationRegistry.addCombination(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), CombinationRegistry.Mode.MODE_OR, new ItemStack(grout, 1, 0));
		CombinationRegistry.addCombination("treeLeaves", "ingotAluminium", CombinationRegistry.Mode.MODE_AND, new ItemStack(oreBush2, 1, 0));
		CombinationRegistry.addCombination("treeLeaves", Items.EXPERIENCE_BOTTLE, 0, CombinationRegistry.Mode.MODE_AND, new ItemStack(oreBush2, 1, 1));
	}
	
}
