package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import java.util.Objects;

/**
 * Holds minestuck configured features. Also creates and registers them when appropriate.
 * See {@link MSFeatures} for minestuck features.
 * (Configured features are world-gen features that has been configured with blocks, sizes or whatever else features may be configured with)
 */
public class MSCFeatures
{
	public static MSCFeatures get()
	{
		return Objects.requireNonNull(instance);
	}
	
	public final ConfiguredFeature<BaseTreeFeatureConfig, ?> RAINBOW_TREE = register("rainbow_tree", Feature.TREE.configured(new BaseTreeFeatureConfig.Builder(
			new SimpleBlockStateProvider(MSBlocks.RAINBOW_LOG.defaultBlockState()), new SimpleBlockStateProvider(MSBlocks.RAINBOW_LEAVES.defaultBlockState()),
			new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3), new StraightTrunkPlacer(4, 2, 0),
			new TwoLayerFeature(1, 0, 1)).ignoreVines().build()));
	public final ConfiguredFeature<?, ?> END_TREE = register("end_tree", MSFeatures.END_TREE.configured(NoFeatureConfig.INSTANCE));
	
	
	
	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> feature)
	{
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Minestuck.MOD_ID, key), feature);
	}
	
	private static MSCFeatures instance;
	
	//Internal, do not call
	public static void init()
	{
		if(instance != null)
			throw new IllegalStateException("Tried to initialize minestuck configured features twice!");
		instance = new MSCFeatures();
	}
}