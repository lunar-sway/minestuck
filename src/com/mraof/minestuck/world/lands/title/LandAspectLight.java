package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.PillarDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.BlockColored;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;

public class LandAspectLight extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "light";
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
		
		chunkProvider.mergeFogColor(new Vec3d(1, 1, 0.8), 0.5F);
	}
	
	@Override
	protected void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
		chunkProvider.blockRegistry.setBlockState("torch", Blocks.TORCH.getDefaultState());
		
		chunkProvider.decorators.add(new PillarDecorator("light_block", 0.5F, false, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumOcean));
		chunkProvider.decorators.add(new PillarDecorator("light_block", 3, true, BiomeMinestuck.mediumRough));
		
		chunkProvider.sortDecorators();
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getDayCycleMode() != 2 && (aspect.getWeatherType() == -1 || (aspect.getWeatherType() & 2) == 0);
	}
	
}