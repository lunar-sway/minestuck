package com.mraof.minestuck.modSupport;

import com.mraof.minestuck.item.crafting.alchemy.AlchemyCostRegistry;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class NeverSayNetherSupport extends ModSupport
{
	
	@Override
	public void registerRecipes() throws Exception
	{
		Item dust = ((Item) (Class.forName("com.debbie.nsn.items.ModItems").getField("daedalean_dustItem").get(null)));
		Item quartz = ((Item) (Class.forName("com.debbie.nsn.items.ModItems").getField("daedalean_quartzItem").get(null)));
		Block ore = ((Block) (Class.forName("com.debbie.nsn.blocks.ModBlocks").getField("daedalean_oreBlock").get(null)));
		
		AlchemyCostRegistry.addGristConversion(dust, new GristSet(GristType.BUILD, 1));
		AlchemyCostRegistry.addGristConversion(quartz, new GristSet(new GristType[]{GristType.BUILD, GristType.QUARTZ}, new int[]{1, 1}));
		AlchemyCostRegistry.addGristConversion(ore, new GristSet(GristType.BUILD, 5));
	}
	
}
