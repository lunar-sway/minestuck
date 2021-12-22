package com.mraof.minestuck.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;

public class RainbowSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig>
{
	
	public RainbowSurfaceBuilder(Codec<SurfaceBuilderConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public void apply(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config)
	{
		config = makeConfig(x - z, config);
		SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, config);
	}
	
	private SurfaceBuilderConfig makeConfig(int index, SurfaceBuilderConfig original)
	{
		index %= 8;
		if(index < 0)
			index += 8;
		
		BlockState newTop = coloredState(original.getTopMaterial(), index);
		BlockState newUnder = coloredState(original.getUnderMaterial(), index);
		BlockState newUnderwater = coloredState(original.getUnderwaterMaterial(), index);
		
		if(newTop != null || newUnder != null || newUnderwater != null)
			return new SurfaceBuilderConfig(newTop != null ? newTop : original.getTopMaterial(), newUnder != null ? newUnder : original.getUnderMaterial(), newUnderwater != null ? newUnderwater : original.getUnderwaterMaterial());
		
		return original;
	}
	
	private BlockState coloredState(BlockState original, int index)
	{
		if(original.getBlock() == Blocks.WHITE_WOOL)
			return getWoolState(index);
		
		if(original.getBlock() == Blocks.WHITE_TERRACOTTA)
			return getTerracottaState(index);
		
		return null;
	}
	
	private BlockState getWoolState(int index)
	{
		
		switch(index)
		{
			case 0: return Blocks.RED_WOOL.defaultBlockState();
			case 1: return Blocks.ORANGE_WOOL.defaultBlockState();
			case 2: return Blocks.YELLOW_WOOL.defaultBlockState();
			case 3: return Blocks.LIME_WOOL.defaultBlockState();
			case 4: return Blocks.LIGHT_BLUE_WOOL.defaultBlockState();
			case 5: return Blocks.BLUE_WOOL.defaultBlockState();
			case 6: return Blocks.PURPLE_WOOL.defaultBlockState();
			case 7: return Blocks.MAGENTA_WOOL.defaultBlockState();
			default: throw new IllegalStateException("Should not happen");
		}
	}
	
	private BlockState getTerracottaState(int index)
	{
		switch(index)
		{
			case 0: return Blocks.RED_TERRACOTTA.defaultBlockState();
			case 1: return Blocks.ORANGE_TERRACOTTA.defaultBlockState();
			case 2: return Blocks.YELLOW_TERRACOTTA.defaultBlockState();
			case 3: return Blocks.LIME_TERRACOTTA.defaultBlockState();
			case 4: return Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState();
			case 5: return Blocks.BLUE_TERRACOTTA.defaultBlockState();
			case 6: return Blocks.PURPLE_TERRACOTTA.defaultBlockState();
			case 7: return Blocks.MAGENTA_TERRACOTTA.defaultBlockState();
			default: throw new IllegalStateException("Should not happen");
		}
	}
}