package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.BlockColored;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;

public class LandAspectThunder extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "thunder";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"thunder", "lightning", "storm"};
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.weatherType = 4;
		chunkProvider.rainfall += 0.1F;
		
		chunkProvider.mergeFogColor(new Vec3d(0.1, 0.1, 0.2), 0.5F);
		
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE));
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.CARPET.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GREEN));
		chunkProvider.oceanChance = Math.min(Math.max(0.5F, chunkProvider.oceanChance), chunkProvider.oceanChance*1.2F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getTemperature() >= 0.2;
	}
	
}
