package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.Objects;

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
	
	default void addFeature(GenerationStep.Decoration step, RegistryObject<PlacedFeature> feature, LandBiomeType... types)
	{
		addFeature(step, feature, null, types);
	}
	
	default void addFeature(GenerationStep.Decoration step, RegistryObject<PlacedFeature> feature, @Nullable FeatureModifier modifier, LandBiomeType... types)
	{
		addFeature(step, Objects.requireNonNull(feature.getKey()), modifier, types);
	}
	
	void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature, @Nullable FeatureModifier modifier, LandBiomeType... types);
	
	void addCarver(GenerationStep.Carving step, Holder<? extends ConfiguredWorldCarver<?>> carver, LandBiomeType... types);
}
