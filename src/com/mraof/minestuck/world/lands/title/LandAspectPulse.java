package com.mraof.minestuck.world.lands.title;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.math.Vec3d;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectPulse extends TitleLandAspect
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
		chunkProvider.oceanChance = Math.max(chunkProvider.oceanChance, 0.2F);
		
		if(chunkProvider.decorators != null)
		{
			chunkProvider.oceanBlock = MinestuckBlocks.blockBlood.getDefaultState();
			chunkProvider.riverBlock = MinestuckBlocks.blockBlood.getDefaultState();
			
			/*ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(MinestuckItems.minestuckBucket, 1, 1), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.repeater, 1, 0), 1, 1, 1));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.beef, 1, 0), 1, 3, 4));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.mutton, 1, 0), 1, 3, 4));*/
		}
		
		chunkProvider.mergeFogColor(new Vec3d(0.8, 0, 0), 0.8F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getOceanBlock().getMaterial() != Material.lava;	//Lava is likely a too important part of the terrain aspect to be replaced
	}
	
}