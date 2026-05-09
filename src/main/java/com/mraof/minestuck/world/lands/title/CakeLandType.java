package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

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
		return new String[]{CAKE, DESSERTS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock(StructureBlockRegistry.STRUCTURE_WOOL_2, Blocks.ORANGE_WOOL);
		registry.setBlock(StructureBlockRegistry.CARPET, Blocks.MAGENTA_CARPET);
	}
	
	@Override
	public void addExtensions(HolderLookup.Provider provider, StructureBlockRegistry blocks)
	{
		HolderLookup.RegistryLookup<PlacedFeature> features = provider.lookupOrThrow(Registries.PLACED_FEATURE);
		
		addFeatureExtension(features, GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.CAKE_PEDESTAL, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		addFeatureExtension(features, GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.LARGE_CAKE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		addFeatureExtension(features, GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.HEART_TREE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.inline(MSFeatures.CAKE.get(),
						new ProbabilityFeatureConfiguration(biomeSet.getTemperature() / 2),
						CountPlacement.of(UniformInt.of(0, 1)), InSquarePlacement.spread(),
						PlacementUtils.HEIGHTMAP_TOP_SOLID, BlockPredicateFilter.forPredicate(BlockPredicate.noFluid()), BiomeFilter.biome()),
				LandBiomeType.any());
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_CAKE.get();
	}
}