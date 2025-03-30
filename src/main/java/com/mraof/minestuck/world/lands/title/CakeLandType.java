package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

public class CakeLandType extends TitleLandType
{
	public static final String CAKE = "minestuck.cake";
	public static final String DESSERTS = "minestuck.desserts";
	
	public CakeLandType()
	{
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {CAKE, DESSERTS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("structure_wool_2", Blocks.ORANGE_WOOL);
		registry.setBlock("carpet", Blocks.MAGENTA_CARPET);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.inline(MSFeatures.CAKE.get(),
						new ProbabilityFeatureConfiguration(biomeSet.getTemperature() / 2),
						CountPlacement.of(UniformInt.of(0, 1)), InSquarePlacement.spread(),
						PlacementUtils.HEIGHTMAP_TOP_SOLID, BlockPredicateFilter.forPredicate(BlockPredicate.noFluid()), BiomeFilter.biome()),
				LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.CAKE_PEDESTAL, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.LARGE_CAKE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.HEART_TREE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_CAKE.get();
	}
}