package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

public class ThunderLandAspect extends TitleLandAspect
{
	public ThunderLandAspect()
	{
		super(EnumAspect.DOOM);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"thunder", "lightning", "storm"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.BLUE_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.GREEN_CARPET.getDefaultState());
		
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.1, 0.1, 0.2), 0.5F);
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.rainType = Biome.RainType.RAIN; //TODO Add feature to make an eternal thunderstorm
		settings.downfall += 0.1F;
	}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.oceanChance = Math.min(Math.max(0.5F, chunkProvider.oceanChance), chunkProvider.oceanChance*1.2F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getTemperature() >= 0.2;
	}
	
}
