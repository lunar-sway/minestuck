package com.mraof.minestuck.world.lands.title;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectThought extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Thought";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"thought"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.oceanChance = Math.max(chunkProvider.oceanChance, 0.2F);
		
		if(chunkProvider.decorators != null)
		{
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(MinestuckItems.minestuckBucket, 1, 2), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(MinestuckBlocks.coloredDirt, 1, 1), 4, 15, 4));
		}
		chunkProvider.riverBlock = MinestuckBlocks.blockBrainJuice.getDefaultState();
		chunkProvider.oceanBlock = MinestuckBlocks.blockBrainJuice.getDefaultState();
		
		chunkProvider.mergeFogColor(new Vec3(0.8, 0.3, 0.8), 0.8F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getOceanBlock().getBlock().getMaterial() != Material.lava;
	}
	
}
