package com.mraof.minestuck.modSupport;

import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ExtraUtilitiesSupport extends ModSupport
{
	
	public void registerRecipes() throws Exception
	{
		if(Class.forName("com.rwtema.extrautils.ExtraUtils").getField("bedrockiumEnabled").getBoolean(null))
		{
			Item bedrockium = ((Item) Class.forName("com.rwtema.extrautils.ExtraUtils").getField("bedrockium").get(null));
			GristRegistry.addGristConversion(new ItemStack(bedrockium), new GristSet(GristType.Zillium, 1));
		}
		
		if(Class.forName("com.rwtema.extrautils.ExtraUtils").getField("enderLilyEnabled").getBoolean(null))
		{
			Block enderLily = ((Block) Class.forName("com.rwtema.extrautils.ExtraUtils").getField("enderLily").get(null));
			GristRegistry.addGristConversion(new ItemStack(enderLily), new GristSet(new GristType[]{GristType.Uranium, GristType.Iodine}, new int[]{24, 6}));
			CombinationRegistry.addCombination(new ItemStack(Items.wheat_seeds), new ItemStack(Items.ender_pearl), CombinationRegistry.MODE_OR,  new ItemStack(enderLily));
			CombinationRegistry.addCombination(new ItemStack(Items.wheat_seeds), new ItemStack(Items.ender_eye), CombinationRegistry.MODE_OR,  new ItemStack(enderLily));	//Might as well do this too
		}
		
		if(Class.forName("com.rwtema.extrautils.ExtraUtils").getField("transferPipeEnabled").getBoolean(null))
		{
			Block transferPipe = ((Block) Class.forName("com.rwtema.extrautils.ExtraUtils").getField("transferPipe").get(null));
			GristRegistry.addGristConversion(new ItemStack(transferPipe), new GristSet(new GristType[]{GristType.Build, GristType.Garnet}, new int[]{1, 1}));
		}
	}
	
}
