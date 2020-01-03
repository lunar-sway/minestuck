package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.*;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mraof.minestuck.MinestuckConfig.*;

public class OreGeneration
{
	public static void setupOverworldOreGeneration()
	{
		{
			{
				for(Biome biome : ForgeRegistries.BIOMES)
				{
					if(disableCruxite == false) {
						biome.addFeature(Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(FillerBlockType.NATURAL_STONE, MSBlocks.STONE_CRUXITE_ORE.getDefaultState(), baseCruxiteVeinSize), Placement.COUNT_RANGE, new CountRangeConfig(cruxiteVeinsPerChunk, cruxiteStratumMin, cruxiteStratumMin, cruxiteStratumMax)));
					}
					if(disableUranium == false)
					{
						biome.addFeature(Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(FillerBlockType.NATURAL_STONE, MSBlocks.STONE_URANIUM_ORE.getDefaultState(), baseUraniumVeinSize), Placement.COUNT_RANGE, new CountRangeConfig(uraniumVeinsPerChunk, uraniumStratumMin, uraniumStratumMin, uraniumStratumMax)));
					}
				}
			}
		}
	}
}