package com.mraof.minestuck.world.lands.title;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectPulse extends TitleAspect
{

	@Override
	public String getPrimaryName()
	{
		return "Pulse";
	}

	@Override
	public String[] getNames()
	{
		return new String[]{"pulse", "blood"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.oceanBlock = Minestuck.blockBlood.getDefaultState();
			chunkProvider.riverBlock = Minestuck.blockBlood.getDefaultState();
			
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Minestuck.minestuckBucket, 1, 1), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.repeater, 1, 0), 1, 1, 1));
		}
	}

}
