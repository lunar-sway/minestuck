package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.decorator.FrogSpawner;
import com.mraof.minestuck.world.lands.decorator.LilypadDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;

 public class LandAspectFrogs extends TitleLandAspect
{
	public LandAspectFrogs()
	{
		super(null, EnumAspect.SPACE, false);
	}
	
	@Override
	public String[] getNames()
		{
			return new String[]{"frog"};
		}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.GREEN_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.LIME_CARPET.getDefaultState());
		
		chunkProvider.decorators.add(new FrogSpawner(6, ModBiomes.mediumNormal));
		chunkProvider.decorators.add(new FrogSpawner(4, ModBiomes.mediumRough));
		chunkProvider.decorators.add(new LilypadDecorator(10, ModBiomes.mediumOcean));
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