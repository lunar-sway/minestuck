package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mraof.minestuck.MinestuckConfig.*;

public class OverworldStructureGeneration
{
	public static void setupOverworldStructureGeneration()
	{
		for(Biome biome : ForgeRegistries.BIOMES)
		{
			if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD))
			{
				//biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.SKAIA_CASTLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
				//biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.STONE_MOUND.withConfiguration(new BlockStateFeatureConfig(Blocks.CLAY.getDefaultState())).withPlacement(Placement.TOP_SOLID_HEIGHTMAP.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
				biome.addStructure(MSFeatures.FROG_TEMPLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
				biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.FROG_TEMPLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
				//biome.addFeature(Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(FillerBlockType.NATURAL_STONE, MSBlocks.STONE_CRUXITE_ORE.getDefaultState(), baseCruxiteVeinSize), Placement.COUNT_RANGE, new CountRangeConfig(cruxiteVeinsPerChunk, cruxiteStratumMin, cruxiteStratumMin, cruxiteStratumMax)));
			}
		}
	}
}