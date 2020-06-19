package com.mraof.minestuck.world.gen;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;
import java.util.function.Function;

public class DerseSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig>
{
	public final SurfaceBuilderConfig DERSE_CONFIG = new SurfaceBuilderConfig(MSBlocks.GOLD_TILE_LIGHT.getDefaultState(), MSBlocks.GOLD_TILE_LIGHT.getDefaultState(), MSBlocks.GOLD_TILE_LIGHT.getDefaultState());

	public DerseSurfaceBuilder(Function<Dynamic<?>, ? extends SurfaceBuilderConfig> deserializer)
	{
		super(deserializer);
	}
	
	@Override
	public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config)
	{
		SurfaceBuilder.DEFAULT.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, 0, defaultBlock, defaultFluid, seaLevel, seed, DERSE_CONFIG);
	}
}