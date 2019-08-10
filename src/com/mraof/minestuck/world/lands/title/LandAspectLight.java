package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.decorator.PillarDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;

public class LandAspectLight extends TitleLandAspect
{
	
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("slime", MinestuckBlocks.GLOWY_GOOP.getDefaultState());
	}
	
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
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.skylightBase = 1.0F;
		worldProvider.mergeFogColor(new Vec3d(1, 1, 0.8), 0.5F);
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.ORANGE_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.ORANGE_CARPET.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("torch", Blocks.TORCH.getDefaultState());
		
		chunkProvider.decorators.add(new PillarDecorator("light_block", 0.5F, false, ModBiomes.mediumNormal, ModBiomes.mediumOcean));
		chunkProvider.decorators.add(new PillarDecorator("light_block", 3, true, ModBiomes.mediumRough));
		
		//chunkProvider.sortDecorators();
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getSkylightBase() > 1/2F && (aspect.getWeatherType() == -1 || (aspect.getWeatherType() & 2) == 0);
	}
	
}