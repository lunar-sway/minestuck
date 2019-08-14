package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.lands.LandDimension;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
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
	
	@Override
	public boolean doesWaterFreeze(IWorldReader worldIn, BlockPos water, boolean mustBeAtEdge)
	{
		if(!(this instanceof LandWrapperBiome) && worldIn.getDimension() instanceof LandDimension)
		{
			return ((LandDimension) worldIn.getDimension()).getWrapperBiome(this).doesWaterFreeze(worldIn, water, mustBeAtEdge);
		}
		return super.doesWaterFreeze(worldIn, water, mustBeAtEdge);
	}
	
	@Override
	public boolean doesSnowGenerate(IWorldReader worldIn, BlockPos pos)
	{
		if(!(this instanceof LandWrapperBiome) && worldIn.getDimension() instanceof LandDimension)
		{
			return ((LandDimension) worldIn.getDimension()).getWrapperBiome(this).doesSnowGenerate(worldIn, pos);
		}
		return super.doesSnowGenerate(worldIn, pos);
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