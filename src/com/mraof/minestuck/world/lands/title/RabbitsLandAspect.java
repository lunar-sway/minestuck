package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.lands.decorator.RabbitSpawner;
import com.mraof.minestuck.world.lands.decorator.structure.RabbitHoleDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;

public class RabbitsLandAspect extends TitleLandAspect
{
	public RabbitsLandAspect()
	{
		super(EnumAspect.LIFE);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"rabbit", "bunny"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.PINK_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.LIGHT_GRAY_CARPET.getDefaultState());
	}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		
		chunkProvider.decorators.add(new RabbitSpawner(6, MSBiomes.mediumNormal));
		chunkProvider.decorators.add(new RabbitSpawner(3, MSBiomes.mediumRough));
		chunkProvider.decorators.add(new RabbitHoleDecorator(MSBiomes.mediumNormal, MSBiomes.mediumRough));
		//chunkProvider.sortDecorators();
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;
	}
	
}