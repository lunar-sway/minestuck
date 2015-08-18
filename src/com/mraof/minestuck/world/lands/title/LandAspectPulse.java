package com.mraof.minestuck.world.lands.title;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;

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
		chunkProvider.oceanChance = Math.max(chunkProvider.oceanChance, 0.2F);
		
		if(chunkProvider.decorators != null)
		{
			chunkProvider.oceanBlock = Minestuck.blockBlood.getDefaultState();
			chunkProvider.riverBlock = Minestuck.blockBlood.getDefaultState();
			
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Minestuck.minestuckBucket, 1, 1), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.repeater, 1, 0), 1, 1, 1));
		}
		
		chunkProvider.mergeFogColor(new Vec3(0.8, 0, 0), 0.8F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return aspect.getOceanBlock().getBlock().getMaterial() != Material.lava;	//Lava is likely a too important part of the terrain aspect to be replaced
	}
	
}