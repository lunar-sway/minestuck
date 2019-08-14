package com.mraof.minestuck.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public abstract class LandBiome extends AbstractBiome
{
	public LandBiome(Builder biomeBuilder)
	{
		super(biomeBuilder);
	}
	
	@Override
	protected void init()
	{
		this.surfaceBuilder = new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG);
	}
	
	public static class Normal extends LandBiome
	{
		public Normal()
		{
			super(new Biome.Builder().precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.125F).scale(0.05F).temperature(0.5F).downfall(0.5F).waterColor(0x3F76E4).waterFogColor(0x050533));
		}
		
		public LandWrapperBiome createWrapper(LandBiomeHolder settings)
		{
			return new LandWrapperBiome(this, settings.rainType, settings.temperature, settings.downfall, settings.normalBiomeDepth, settings.normalBiomeScale);
		}
	}
	
	public static class Rough extends LandBiome
	{
		public Rough()
		{
			super(new Biome.Builder().precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.45F).scale(0.3F).temperature(0.5F).downfall(0.5F).waterColor(0x3F76E4).waterFogColor(0x050533));
		}
		
		public LandWrapperBiome createWrapper(LandBiomeHolder settings)
		{
			return new LandWrapperBiome(this, settings.rainType, settings.temperature, settings.downfall, settings.roughBiomeDepth, settings.roughBiomeScale);
		}
	}
	
	public static class Ocean extends LandBiome
	{
		public Ocean()
		{
			super(new Biome.Builder().precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(-1.0F).scale(0.1F).temperature(0.5F).downfall(0.5F).waterColor(0x3F76E4).waterFogColor(0x050533));
		}
		
		public LandWrapperBiome createWrapper(LandBiomeHolder settings)
		{
			return new LandWrapperBiome(this, settings.rainType, settings.temperature, settings.downfall, settings.oceanBiomeDepth, settings.oceanBiomeScale);
		}
	}
}