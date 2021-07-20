package com.mraof.minestuck.world.lands.title;
 import com.mraof.minestuck.entity.EntityFrog;
 import com.mraof.minestuck.world.biome.BiomeMinestuck;
 import com.mraof.minestuck.world.lands.decorator.LilypadDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
 import net.minecraft.world.biome.Biome;

public class LandAspectFrogs extends TitleLandAspect
{
	@Override
	public String getPrimaryName()
	 	{
	 		return "frogs";
	 	}

	@Override
	public String[] getNames()
		{
			return new String[]{"frog"};
		}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GREEN));
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.CARPET.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIME));

		chunkProvider.ambientMobsList.add(new Biome.SpawnListEntry(EntityFrog.class, 1, 1, 1));

		chunkProvider.decorators.add(new LilypadDecorator(10, BiomeMinestuck.mediumOcean));
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