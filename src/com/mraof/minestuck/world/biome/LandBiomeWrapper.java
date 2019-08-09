package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.lands.LandDimension;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Objects;

public class LandBiomeWrapper extends LandBiome
{
	public final LandBiome staticBiome;
	
	public LandBiomeWrapper(LandBiome staticBiome)
	{
		this.staticBiome = staticBiome;
		this.setRegistryName(Objects.requireNonNull(staticBiome.getRegistryName()));
	}
	
	public void init(LandDimension dimension)
	{
		SurfaceBuilderConfig surfaceConfig = new SurfaceBuilderConfig(dimension.blockRegistry.getBlockState("surface"), dimension.blockRegistry.getBlockState("upper"), dimension.blockRegistry.getBlockState("upper"));
		this.surfaceBuilder = new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, surfaceConfig);
	}
}
