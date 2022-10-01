package com.mraof.minestuck.world.gen.feature;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class FeatureModifier
{
	public static Function<PlacedFeature, PlacedFeature> withTargets(List<BlockState> targets)
	{
		return configMap(new ConfigMapper()
		{
			@SuppressWarnings("unchecked")
			@Override
			public <T extends FeatureConfiguration> T map(T config)
			{
				if(config instanceof DiskConfiguration diskConfig)
					return (T) new DiskConfiguration(diskConfig.state(), diskConfig.radius(), diskConfig.halfHeight(), targets);
				return config;
			}
		});
	}
	
	public static Function<PlacedFeature, PlacedFeature> withState(BlockState state)
	{
		return configMap(new ConfigMapper()
		{
			@SuppressWarnings("unchecked")
			@Override
			public <T extends FeatureConfiguration> T map(T config)
			{
				if(config.getClass() == BlockStateConfiguration.class)
					return (T) new BlockStateConfiguration(state);
				if(config instanceof DiskConfiguration diskConfig)
					return (T) new DiskConfiguration(state, diskConfig.radius(), diskConfig.halfHeight(), diskConfig.targets());
				return config;
			}
		});
	}
	
	private static Function<PlacedFeature, PlacedFeature> configMap(ConfigMapper mapper)
	{
		return placed -> mapConfigured(placed.feature().value(), mapper)
				.map(newConfigured -> new PlacedFeature(Holder.direct(newConfigured), placed.placement()))
				.orElse(placed);
	}
	
	private static <FC extends FeatureConfiguration, F extends Feature<FC>> Optional<ConfiguredFeature<FC, F>> mapConfigured(ConfiguredFeature<FC, F> configured, ConfigMapper mapper)
	{
		FC oldConfig = configured.config();
		FC newConfig = mapper.map(oldConfig);
		if(oldConfig == newConfig)
			return Optional.empty();
		else
			return Optional.of(new ConfiguredFeature<>(configured.feature(), newConfig));
	}
	
	private interface ConfigMapper
	{
		<T extends FeatureConfiguration> T map(T config);
	}
}
