package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.structure.SmallLibraryDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Vec3d;

public class LandAspectThought extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "thought";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"thought"};
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.8, 0.3, 0.8), 0.8F);
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.oceanChance = Math.max(chunkProvider.oceanChance, 0.2F);
		
		/*chunkProvider.blockRegistry.setBlockState("ocean", MinestuckBlocks.blockBrainJuice.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("river", MinestuckBlocks.blockBrainJuice.getDefaultState());*/
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.LIME_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.LIME_CARPET.getDefaultState());
		
		chunkProvider.decorators.add(new SmallLibraryDecorator(BiomeMinestuck.mediumNormal));
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
