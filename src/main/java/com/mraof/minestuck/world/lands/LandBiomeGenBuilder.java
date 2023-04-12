package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.RegistryObject;

public interface LandBiomeGenBuilder
{
	void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature, LandBiomeType... types);
	
	default void addFeature(GenerationStep.Decoration step, RegistryObject<PlacedFeature> feature, LandBiomeType... types)
	{
		addFeature(step, feature.getHolder().orElseThrow(), types);
	}
	
	default void addModified(GenerationStep.Decoration step, Holder<PlacedFeature> feature, FeatureModifier modifier, LandBiomeType... types)
	{
		addFeature(step, modifier.map(feature), types);
	}
	
	default void addModified(GenerationStep.Decoration step, RegistryObject<PlacedFeature> feature, FeatureModifier modifier, LandBiomeType... types)
	{
		addModified(step, feature.getHolder().orElseThrow(), modifier, types);
	}
	
	void addCarver(GenerationStep.Carving step, Holder<? extends ConfiguredWorldCarver<?>> carver, LandBiomeType... types);
}
