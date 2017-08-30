package com.mraof.minestuck.world.lands.title;

import net.minecraft.block.material.Material;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.RabbitSpawner;
import com.mraof.minestuck.world.lands.decorator.structure.RabbitHoleDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectRabbits extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "rabbits";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"rabbit", "bunny"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
	}
	
	@Override
	protected void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.decorators.add(new RabbitSpawner(6, BiomeMinestuck.mediumNormal));
		chunkProvider.decorators.add(new RabbitSpawner(3, BiomeMinestuck.mediumRough));
		chunkProvider.decorators.add(new RabbitHoleDecorator(BiomeMinestuck.mediumNormal));
		chunkProvider.decorators.add(new RabbitHoleDecorator(BiomeMinestuck.mediumRough));
		chunkProvider.sortDecorators();
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;
	}
	
}