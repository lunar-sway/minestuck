package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;

public interface LandBiomeGenBuilder
{
	void addFeature(GenerationStep.Decoration step, PlacedFeature feature, LandBiomeType... types);
	
	default void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature, LandBiomeType... types)
	{
		addFeature(step, feature, null, types);
	}
	
	default void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature, @Nullable FeatureModifier modifier, LandBiomeType... types)
	{
		addFeature(step, feature.unwrapKey().orElseThrow(), modifier, types);
	}
	
	default void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature, LandBiomeType... types)
	{
		addFeature(step, feature, null, types);
	}
	
	void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature, @Nullable FeatureModifier modifier, LandBiomeType... types);
	
	void addCarver(GenerationStep.Carving step, ResourceKey<ConfiguredWorldCarver<?>> carver, LandBiomeType... types);
	
	void addCarver(GenerationStep.Carving step, ConfiguredWorldCarver<?> carver, LandBiomeType... types);
}
