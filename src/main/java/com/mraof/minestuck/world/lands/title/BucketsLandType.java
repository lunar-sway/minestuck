package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BucketsLandType extends TitleLandType    //Yes, buckets
{
	public static final String BUCKETS = "minestuck.buckets";
	
	public BucketsLandType()
	{
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{BUCKETS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock(StructureBlockRegistry.STRUCTURE_WOOL_2, Blocks.BLUE_WOOL);
		registry.setBlock(StructureBlockRegistry.CARPET, Blocks.BLACK_CARPET);
	}
	
	@Override
	public void addExtensions(HolderLookup.Provider provider, StructureBlockRegistry blocks)
	{
		HolderLookup.RegistryLookup<PlacedFeature> features = provider.lookupOrThrow(Registries.PLACED_FEATURE);
		
		addFeatureExtension(features, GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.BUCKET, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		addFeatureExtension(features, GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPACE_TREE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_BUCKETS.get();
	}
}