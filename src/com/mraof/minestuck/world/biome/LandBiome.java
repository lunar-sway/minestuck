package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.SkaiaSurfaceBuilder;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class LandBiome extends AbstractBiome
{
	public LandBiome()
	{
		super(new Biome.Builder().precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(0x3F76E4).waterFogColor(0x050533));
	}
	
	@Override
	protected void init()
	{
		this.surfaceBuilder = new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG);
	}
}