package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

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
			builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.CAKE_PEDESTAL.getHolder().orElseThrow());
		}
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, PlacementUtils.inlinePlaced(MSFeatures.CAKE.get(),
				new ProbabilityFeatureConfiguration(baseBiome.getBaseTemperature()/2),
				CountPlacement.of(UniformInt.of(0, 5)), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_CAKE;
	}
}