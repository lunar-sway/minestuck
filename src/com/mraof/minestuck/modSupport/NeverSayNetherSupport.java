package com.mraof.minestuck.modSupport;

import com.mraof.minestuck.alchemy.AlchemyCostRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NeverSayNetherSupport extends ModSupport
{
	
	@Override
	public void registerRecipes() throws Exception
	{
		Item dust = ((Item) (Class.forName("com.debbie.nsn.items.ModItems").getField("daedalean_dustItem").get(null)));
		Item quartz = ((Item) (Class.forName("com.debbie.nsn.items.ModItems").getField("daedalean_quartzItem").get(null)));
		Block ore = ((Block) (Class.forName("com.debbie.nsn.blocks.ModBlocks").getField("daedalean_oreBlock").get(null)));
		
		AlchemyCostRegistry.addGristConversion(new ItemStack(dust), new GristSet(GristType.Build, 1));
		AlchemyCostRegistry.addGristConversion(new ItemStack(quartz), new GristSet(new GristType[]{GristType.Build, GristType.Quartz}, new int[]{1, 1}));
		AlchemyCostRegistry.addGristConversion(ore, new GristSet(GristType.Build, 5));
	}
	
}
