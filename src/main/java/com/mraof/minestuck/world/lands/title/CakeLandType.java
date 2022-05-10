package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class CakeLandType extends TitleLandType
{
	public static final String CAKE = "minestuck.cake";
	public static final String DESSERTS = "minestuck.desserts";
	
	public CakeLandType()
	{
		super(EnumAspect.HEART);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {CAKE, DESSERTS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.ORANGE_WOOL.defaultBlockState());
		registry.setBlockState("carpet", Blocks.MAGENTA_CARPET.defaultBlockState());
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type != LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.CAKE_PEDESTAL.configured(IFeatureConfig.NONE).chance(100));
		}
		
		builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.CAKE.configured(new ProbabilityConfig(baseBiome.getBaseTemperature()/2))
				.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).countRandom(5));
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_CAKE;
	}
}