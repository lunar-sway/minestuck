package com.mraof.minestuck.world.gen.feature;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Optional;

public interface FeatureModifier
{
	Holder<PlacedFeature> map(Holder<PlacedFeature> placedFeature);
	
	static FeatureModifier withTargets(BlockPredicate targets)
	{
		return new ConfigMapper()
		{
			@SuppressWarnings("unchecked")
			@Override
			public <T extends FeatureConfiguration> Optional<T> maybeMap(T config)
			{
				if(config instanceof DiskConfiguration diskConfig)
					return Optional.of((T) new DiskConfiguration(diskConfig.stateProvider(), targets, diskConfig.radius(), diskConfig.halfHeight()));
				return maybeMapBase(config);
			}
		};
	}
	
	static FeatureModifier withState(BlockState state)
	{
		return new ConfigMapper()
		{
			@SuppressWarnings("unchecked")
			@Override
			public <T extends FeatureConfiguration> Optional<T> maybeMap(T config)
			{
				if(config.getClass() == BlockStateConfiguration.class)
					return Optional.of((T) new BlockStateConfiguration(state));
				if(config instanceof DiskConfiguration diskConfig)
					return Optional.of((T) new DiskConfiguration(RuleBasedBlockStateProvider.simple(BlockStateProvider.simple(state)),
							diskConfig.target(), diskConfig.radius(), diskConfig.halfHeight()));
				return maybeMapBase(config);
			}
		};
	}
	
	interface ConfigMapper extends FeatureModifier
	{
		<T extends FeatureConfiguration> Optional<T> maybeMap(T config);
		
		@SuppressWarnings("unchecked")
		default <T extends FeatureConfiguration> Optional<T> maybeMapBase(T config)
		{
			if(config instanceof RandomFeatureConfiguration featureConfig)
				return Optional.of((T) new RandomFeatureConfiguration(
						featureConfig.features.stream().map(weighted -> new WeightedPlacedFeature(map(weighted.feature), weighted.chance)).toList(),
						map(featureConfig.defaultFeature)));
			return Optional.empty();
		}
		
		default Optional<PlacedFeature> maybeMap(PlacedFeature placed)
		{
			return maybeMap(placed.feature().value()).map(newConfigured -> new PlacedFeature(Holder.direct(newConfigured), placed.placement()));
		}
		
		default <FC extends FeatureConfiguration, F extends Feature<FC>> Optional<ConfiguredFeature<FC, F>> maybeMap(ConfiguredFeature<FC, F> configured)
		{
			return maybeMap(configured.config()).map(newConfig -> new ConfiguredFeature<>(configured.feature(), newConfig));
		}
		
		@Override
		default Holder<PlacedFeature> map(Holder<PlacedFeature> placedFeature)
		{
			return maybeMap(placedFeature.value()).map(Holder::direct).orElse(placedFeature);
		}
	}
}
