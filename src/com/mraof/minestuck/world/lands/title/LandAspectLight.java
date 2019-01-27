package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.PillarDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;

public class LandAspectLight extends TitleLandAspect
{
	
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("slime", MinestuckBlocks.glowyGoop.getDefaultState());
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
	public void prepareWorldProvider(WorldProviderLands worldProvider)
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
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.CARPET.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
		chunkProvider.blockRegistry.setBlockState("torch", Blocks.TORCH.getDefaultState());
		
		chunkProvider.decorators.add(new PillarDecorator("light_block", 0.5F, false, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumOcean));
		chunkProvider.decorators.add(new PillarDecorator("light_block", 3, true, BiomeMinestuck.mediumRough));
		
		chunkProvider.sortDecorators();
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getSkylightBase() > 1/2F && (aspect.getWeatherType() == -1 || (aspect.getWeatherType() & 2) == 0);
	}
	
}