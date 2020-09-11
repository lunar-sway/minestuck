package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mraof.minestuck.MinestuckConfig.*;

public class OreGeneration
{
	public static void setupOverworldOreGeneration()
	{
		for(Biome biome : ForgeRegistries.BIOMES)
		{
			if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD))
			{
				if(generateCruxiteOre.get())
				{
					biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FillerBlockType.NATURAL_STONE, MSBlocks.STONE_CRUXITE_ORE.getDefaultState(), baseCruxiteVeinSize)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(cruxiteVeinsPerChunk, cruxiteStratumMin, cruxiteStratumMin, cruxiteStratumMax))));
				}
				if(generateUraniumOre.get())
				{
					biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FillerBlockType.NATURAL_STONE, MSBlocks.STONE_URANIUM_ORE.getDefaultState(), baseUraniumVeinSize)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(uraniumVeinsPerChunk, uraniumStratumMin, uraniumStratumMin, uraniumStratumMax))));
				}
			}
		}
	}
}