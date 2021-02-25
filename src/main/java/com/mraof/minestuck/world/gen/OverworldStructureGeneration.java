package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.CountRangeConfig;
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
				biome.addFeature(Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(FillerBlockType.NATURAL_STONE, MSBlocks.STONE_CRUXITE_ORE.getDefaultState(), baseCruxiteVeinSize), Placement.COUNT_RANGE, new CountRangeConfig(cruxiteVeinsPerChunk, cruxiteStratumMin, cruxiteStratumMin, cruxiteStratumMax)));
			}
		}
	}
}