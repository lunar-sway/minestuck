package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class LandBiomeGenVerifier
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void verify()
	{
		LandBiomeGenBuilder builder = new LandBiomeGenBuilder()
		{
			@Override
			public void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature, @Nullable FeatureModifier modifier, LandBiomeType... types)
			{
				if(types.length == 0)
					throw new IllegalArgumentException("Missing land biome types!");
			}
			@Override
			public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature, @Nullable FeatureModifier modifier, LandBiomeType... types)
			{
				if(types.length == 0)
					throw new IllegalArgumentException("Missing land biome types!");
			}
			@Override
			public void addCarver(GenerationStep.Carving step, ResourceKey<ConfiguredWorldCarver<?>> carver, LandBiomeType... types)
			{
				if(types.length == 0)
					throw new IllegalArgumentException("Missing land biome types!");
			}
			@Override
			public void addCarver(GenerationStep.Carving step, Holder<ConfiguredWorldCarver<?>> carver, LandBiomeType... types)
			{
				if(types.length == 0)
					throw new IllegalArgumentException("Missing land biome types!");
			}
		};
		
		for(TerrainLandType landType : LandTypes.TERRAIN_REGISTRY)
		{
			try
			{
				landType.addBiomeGeneration(builder, StructureBlockRegistry.getOrDefault(null));
			} catch(RuntimeException e)
			{
				LOGGER.error("Detected issue with terrain land type {}:", landType, e);
			}
		}
		for(TitleLandType landType : LandTypes.TITLE_REGISTRY)
		{
			try
			{
				landType.addBiomeGeneration(builder, StructureBlockRegistry.getOrDefault(null), MSBiomes.DEFAULT_LAND);
			} catch(RuntimeException e)
			{
				LOGGER.error("Detected issue with title land type {}:", landType, e);
			}
		}
		
		LOGGER.info("Finished verifying land type biome generation");
	}
}
