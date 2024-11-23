package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.TurtleVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.function.Consumer;

import static com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry.*;

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
		super(new Builder(MSEntityTypes.TURTLE).names(RAIN, ISLANDS, SKY)
				.fogColor(0.9, 0.8, 0.6).skyColor(0.3, 0.5, 0.98)
				.biomeSet(MSBiomes.HIGH_HUMID_LAND).music(MSSoundEvents.MUSIC_RAIN));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock(GROUND, MSBlocks.PINK_STONE);
		registry.setBlock(GROUND_SLAB, MSBlocks.PINK_STONE_SLAB);
		registry.setBlock(SURFACE, MSBlocks.CHALK);
		registry.setBlock(UPPER, MSBlocks.CHALK);
		registry.setBlock(OCEAN, Blocks.WATER);
		registry.setBlock(STRUCTURE_PRIMARY, MSBlocks.PINK_STONE_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_CRACKED, MSBlocks.CRACKED_PINK_STONE_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_MOSSY, MSBlocks.MOSSY_PINK_STONE_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_COLUMN, MSBlocks.PINK_STONE_COLUMN);
		registry.setBlock(STRUCTURE_PRIMARY_DECORATIVE, MSBlocks.CHISELED_PINK_STONE_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_STAIRS,MSBlocks.PINK_STONE_BRICK_STAIRS);
		registry.setBlock(STRUCTURE_SECONDARY, MSBlocks.POLISHED_PINK_STONE);
		registry.setBlock(STRUCTURE_SECONDARY_STAIRS, MSBlocks.CHALK_BRICK_STAIRS);
		registry.setBlock(STRUCTURE_SECONDARY_DECORATIVE, MSBlocks.CHISELED_PINK_STONE_BRICKS);
		registry.setBlock(STRUCTURE_PLANKS, MSBlocks.DEAD_PLANKS);
		registry.setBlock(STRUCTURE_PLANKS_SLAB, MSBlocks.DEAD_SLAB);
		registry.setBlock(BUSH, Blocks.DEAD_BUSH);
		registry.setBlock(STRUCTURE_WOOL_1, Blocks.YELLOW_WOOL);
		registry.setBlock(STRUCTURE_WOOL_3, Blocks.MAGENTA_WOOL);
		registry.setBlock(CRUXITE_ORE, MSBlocks.PINK_STONE_CRUXITE_ORE);
		registry.setBlock(URANIUM_ORE, MSBlocks.PINK_STONE_URANIUM_ORE);

	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = 0.2F;
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.DEAD_TREE, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.EXTRA_DEAD_TREE, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.PINK_STONE_COAL_ORE.get().defaultBlockState(), 17),
						CountPlacement.of(26), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.PINK_STONE_LAPIS_ORE.get().defaultBlockState(), 7),
						CountPlacement.of(15), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.PINK_STONE_GOLD_ORE.get().defaultBlockState(), 9),
						CountPlacement.of(12), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.PINK_STONE_DIAMOND_ORE.get().defaultBlockState(), 6),
						CountPlacement.of(11), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)), BiomeFilter.biome()),
				LandBiomeType.any());
	}
	
	@Override
	public void addStructureSets(Consumer<StructureSet> consumer, HolderGetter<Structure> structureLookup)
	{
		super.addStructureSets(consumer, structureLookup);
		consumer.accept(new StructureSet(structureLookup.getOrThrow(MSStructures.PINK_TOWER), new RandomSpreadStructurePlacement(35, 10, RandomSpreadType.LINEAR, 90543602)));
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		TurtleVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, RandomSource random)
	{
		TurtleVillagePieces.addPieces(register, random);
	}
}