package com.mraof.minestuck.world.lands.title;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;

public class LandAspectLight extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Light";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"light", "brightness"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.dayCycle = 1;
		
		if(chunkProvider.decorators != null)
		{
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Blocks.glowstone, 1, 0), 1, 4, 2));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.glowstone_dust, 1, 0), 2, 5, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Blocks.torch, 1, 0), 2, 10, 5));
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return aspect.getDayCycleMode() != 2 && (aspect.getWeatherType() == -1 || (aspect.getWeatherType() & 2) == 0);
	}
	
}