package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class LandWrapperBiome extends LandBiome
{
	public final LandBiome staticBiome;
	
	public LandWrapperBiome(LandBiome biome, Category category, RainType rainType, float temperature, float downfall, float depth, float scale)
	{
		super(new Biome.Builder().category(category).precipitation(rainType).temperature(temperature).downfall(downfall).depth(depth).scale(scale).waterColor(0x3F76E4).waterFogColor(0x050533));
		this.staticBiome = biome;
	}
	
	public void init(LandGenSettings settings)
	{
		StructureBlockRegistry registry = settings.getBlockRegistry();
		SurfaceBuilderConfig surfaceConfig = new SurfaceBuilderConfig(registry.getBlockState("surface"), registry.getBlockState("upper"), registry.getBlockState("ocean_surface"));
		this.surfaceBuilder = new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, surfaceConfig);
		this.addSpawn(EntityClassification.CREATURE, new SpawnListEntry(settings.getLandAspects().terrain.getConsortType(), 2, 1, 10));
	}
	
	@Override
	public void addSpawn(EntityClassification classification, SpawnListEntry entry)
	{
		super.addSpawn(classification, entry);
	}
}