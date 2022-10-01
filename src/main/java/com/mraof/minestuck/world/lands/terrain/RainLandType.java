package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.TurtleVillagePieces;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import java.util.Random;

public class RainLandType extends TerrainLandType
{
	public static final String RAIN = "minestuck.rain";
	public static final String ISLANDS = "minestuck.islands";
	public static final String SKY = "minestuck.sky";
	
	//TODO:
	//Pink stone brick temples		Monsters in these temples tend to guard living trees, Magic Beans, and Fertile Soil.
	//Light Cloud Dungeons
	//Custom dungeon loot
	//Definitely nothing underwater
	//Giant beanstalks? Maybe some Paper Mario reference here
	
	
	public RainLandType()
	{
		super(new Builder(MSEntityTypes.TURTLE).unavailable().names(RAIN, ISLANDS, SKY)
				.fogColor(0.9, 0.8, 0.6).skyColor(0.3, 0.5, 0.98)
				.biomeSet(MSBiomes.HIGH_HUMID_LAND).category(Biome.BiomeCategory.BEACH).music(() -> MSSoundEvents.MUSIC_RAIN));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("ground", MSBlocks.PINK_STONE);
		registry.setBlock("surface", MSBlocks.CHALK);
		registry.setBlock("upper", MSBlocks.CHALK);
		registry.setBlock("ocean", Blocks.WATER);
		registry.setBlock("structure_primary", MSBlocks.PINK_STONE_BRICKS);
		registry.setBlock("structure_primary_cracked", MSBlocks.CRACKED_PINK_STONE_BRICKS);
		registry.setBlock("structure_primary_mossy", MSBlocks.MOSSY_PINK_STONE_BRICKS);
		registry.setBlock("structure_primary_column", MSBlocks.PINK_STONE_COLUMN);
		registry.setBlock("structure_primary_decorative", MSBlocks.CHISELED_PINK_STONE_BRICKS);
		registry.setBlock("structure_primary_stairs",MSBlocks.PINK_STONE_BRICK_STAIRS);
		registry.setBlock("structure_secondary", MSBlocks.POLISHED_PINK_STONE);
		registry.setBlock("structure_secondary_stairs", MSBlocks.CHALK_BRICK_STAIRS);
		registry.setBlock("structure_secondary_decorative", MSBlocks.CHISELED_PINK_STONE_BRICKS);
		registry.setBlock("structure_planks", MSBlocks.DEAD_PLANKS);
		registry.setBlock("structure_planks_slab", MSBlocks.DEAD_PLANKS_SLAB);
		registry.setBlock("bush", Blocks.DEAD_BUSH);
		registry.setBlock("structure_wool_1", Blocks.YELLOW_WOOL);
		registry.setBlock("structure_wool_3", Blocks.MAGENTA_WOOL);
		registry.setBlock("cruxite_ore", MSBlocks.PINK_STONE_CRUXITE_ORE);
		registry.setBlock("uranium_ore", MSBlocks.PINK_STONE_URANIUM_ORE);

	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = 0.2F;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, LandBiomeSet biomeSet)
	{
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.DEAD_TREE.getHolder().orElseThrow());
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.EXTRA_DEAD_TREE.getHolder().orElseThrow());
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.PINK_STONE_COAL_ORE.get().defaultBlockState(), 17),
				CountPlacement.of(13), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.PINK_STONE_LAPIS_ORE.get().defaultBlockState(), 7),
				CountPlacement.of(4), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.PINK_STONE_GOLD_ORE.get().defaultBlockState(), 9),
				CountPlacement.of(4), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.PINK_STONE_DIAMOND_ORE.get().defaultBlockState(), 6),
				CountPlacement.of(3), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24)), BiomeFilter.biome()));
		
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		TurtleVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		TurtleVillagePieces.addPieces(register, random);
	}
}