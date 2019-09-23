package com.mraof.minestuck.world.biome;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import java.util.Random;

public abstract class AbstractBiome extends Biome
{
	protected ConfiguredSurfaceBuilder<?> surfaceBuilder;	//Because the parent field is final, and we don't have access to our own surface builder types on biome
	
	public AbstractBiome(Builder biomeBuilder)
	{
		super(biomeBuilder.surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.AIR_CONFIG));
	}
	
	/**
	 * Init method that is intended to be called by ModBiomes during init.
	 * Because biomes generally will need objects from other registries when set up (features, surface builder types and entity types for example),
	 * some of the biome initialization will need to be done at a later stage.
	 */
	protected void init()
	{}
	
	@Override
	public void buildSurface(Random random, IChunk chunkIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed)
	{
		this.surfaceBuilder.setSeed(seed);
		this.surfaceBuilder.buildSurface(random, chunkIn, this, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed);
	}
	
	@Override
	public ConfiguredSurfaceBuilder<?> getSurfaceBuilder()
	{
		return this.surfaceBuilder;
	}
	
	@Override
	public ISurfaceBuilderConfig getSurfaceBuilderConfig()
	{
		return this.surfaceBuilder.getConfig();
	}
}