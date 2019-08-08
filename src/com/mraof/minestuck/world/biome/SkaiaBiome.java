package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.world.gen.SkaiaSurfaceBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import java.util.Random;

public class SkaiaBiome extends Biome
{
	protected ConfiguredSurfaceBuilder<?> surfaceBuilder;	//TODO Move this to an abstract biome class
	
	protected SkaiaBiome()
	{
		super(new Biome.Builder().surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.AIR_CONFIG).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(4159204).waterFogColor(329011));
		
	}
	
	protected void init()
	{
		this.surfaceBuilder = new ConfiguredSurfaceBuilder<>(SkaiaSurfaceBuilder.SKAIA, SurfaceBuilder.AIR_CONFIG);
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(ModEntityTypes.DERSITE_PAWN, 2, 1, 10));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(ModEntityTypes.DERSITE_BISHOP, 1, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(ModEntityTypes.DERSITE_ROOK, 1, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(ModEntityTypes.PROSPITIAN_PAWN, 2, 1, 10));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(ModEntityTypes.PROSPITIAN_BISHOP, 1, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(ModEntityTypes.PROSPITIAN_ROOK, 1, 1, 1));
	}
	
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