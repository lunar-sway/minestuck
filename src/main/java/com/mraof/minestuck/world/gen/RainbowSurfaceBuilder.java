package com.mraof.minestuck.world.gen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;
import java.util.function.Function;

public class RainbowSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig>
{
	
	public RainbowSurfaceBuilder(Function<Dynamic<?>, ? extends SurfaceBuilderConfig> deserializer)
	{
		super(deserializer);
	}
	
	@Override
	public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config)
	{
		config = makeConfig(x - z, config);
		SurfaceBuilder.DEFAULT.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, config);
	}
	
	private SurfaceBuilderConfig makeConfig(int index, SurfaceBuilderConfig original)
	{
		index %= 8;
		if(index < 0)
			index += 8;
		
		BlockState newTop = coloredState(original.getTop(), index);
		BlockState newUnder = coloredState(original.getUnder(), index);
		BlockState newUnderwater = coloredState(original.getUnderWaterMaterial(), index);
		
		if(newTop != null || newUnder != null || newUnderwater != null)
			return new SurfaceBuilderConfig(newTop != null ? newTop : original.getTop(), newUnder != null ? newUnder : original.getUnder(), newUnderwater != null ? newUnderwater : original.getUnderWaterMaterial());
		
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
			case 0: return Blocks.RED_WOOL.getDefaultState();
			case 1: return Blocks.ORANGE_WOOL.getDefaultState();
			case 2: return Blocks.YELLOW_WOOL.getDefaultState();
			case 3: return Blocks.LIME_WOOL.getDefaultState();
			case 4: return Blocks.LIGHT_BLUE_WOOL.getDefaultState();
			case 5: return Blocks.BLUE_WOOL.getDefaultState();
			case 6: return Blocks.PURPLE_WOOL.getDefaultState();
			case 7: return Blocks.MAGENTA_WOOL.getDefaultState();
			default: throw new IllegalStateException("Should not happen");
		}
	}
	
	private BlockState getTerracottaState(int index)
	{
		switch(index)
		{
			case 0: return Blocks.RED_TERRACOTTA.getDefaultState();
			case 1: return Blocks.ORANGE_TERRACOTTA.getDefaultState();
			case 2: return Blocks.YELLOW_TERRACOTTA.getDefaultState();
			case 3: return Blocks.LIME_TERRACOTTA.getDefaultState();
			case 4: return Blocks.LIGHT_BLUE_TERRACOTTA.getDefaultState();
			case 5: return Blocks.BLUE_TERRACOTTA.getDefaultState();
			case 6: return Blocks.PURPLE_TERRACOTTA.getDefaultState();
			case 7: return Blocks.MAGENTA_TERRACOTTA.getDefaultState();
			default: throw new IllegalStateException("Should not happen");
		}
	}
}