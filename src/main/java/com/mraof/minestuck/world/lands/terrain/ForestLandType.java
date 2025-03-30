package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.IguanaVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.*;
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

public class ForestLandType extends TerrainLandType
{
	public static final String FORESTS = "minestuck.forests";
	public static final String TREES = "minestuck.trees";
	public static final String BOREAL_FORESTS = "minestuck.boreal_forests";
	public static final String TAIGAS = "minestuck.taigas";
	public static final String COLD_FORESTS = "minestuck.cold_forests";
	
	private final Variant type;
	
	public static TerrainLandType createForest()
	{
		return new ForestLandType(Variant.FOREST, new Builder(MSEntityTypes.IGUANA).names(FORESTS, TREES)
				.fogColor(0.0, 1.0, 0.6).skyColor(0.4, 0.7, 1.0)
				.music(MSSoundEvents.MUSIC_FOREST));
	}
	
	public static TerrainLandType createTaiga()
	{
		return new ForestLandType(Variant.TAIGA, new Builder(MSEntityTypes.IGUANA).names(TAIGAS, BOREAL_FORESTS, COLD_FORESTS)
				.fogColor(0.0, 1.0, 0.6).skyColor(0.4, 0.7, 1.0)
				.music(MSSoundEvents.MUSIC_TAIGA));
	}
	
	private ForestLandType(Variant variation, Builder builder)
	{
		super(builder);
		type = variation;
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("surface", type == Variant.TAIGA ? Blocks.PODZOL : Blocks.GRASS_BLOCK);
		registry.setBlock("upper", Blocks.DIRT);
		if(type == Variant.TAIGA) {
			registry.setBlock("structure_primary", Blocks.SPRUCE_WOOD);
			registry.setBlock("structure_primary_decorative", MSBlocks.FROST_WOOD);
		} else {
			registry.setBlock("structure_primary", MSBlocks.VINE_WOOD);
			registry.setBlock("structure_primary_decorative", MSBlocks.FLOWERY_VINE_WOOD);
		}
		registry.setBlock("structure_secondary", Blocks.STONE_BRICKS);
		registry.setBlock("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS);
		registry.setBlock("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS);
		registry.setBlock("village_path", Blocks.DIRT_PATH);
		registry.setBlock("bush", Blocks.FERN);
		registry.setBlock("structure_wool_1", Blocks.GREEN_WOOL);
		if(type == Variant.TAIGA) {
			registry.setBlock("structure_wool_3", Blocks.LIGHT_BLUE_WOOL);
		} else {
			registry.setBlock("structure_wool_3", Blocks.BROWN_WOOL);
		}
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.forceRain = LandProperties.ForceType.DEFAULT;
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_MOSS_CARPET_PATCH, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.MOSS_CARPET_PATCH, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_BERRY_RARE, LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.FOREST_ROCK, LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, MSPlacedFeatures.SPARSE_LUSH_CAVES_CEILING_VEGETATION, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.ROOTED_AZALEA_TREE, LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, MSPlacedFeatures.CEILING_ROOTS, LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.TREE_STUMP, LandBiomeType.NORMAL);
		
		switch(this.type)
		{
			case FOREST -> builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.FOREST_LAND_TREES, LandBiomeType.NORMAL);
			case TAIGA -> builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.TAIGA_LAND_TREES, LandBiomeType.NORMAL);
		}
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_JUNGLE_GRASS_PATCH, LandBiomeType.ROUGH);
		
		switch(this.type)
		{
			case FOREST -> builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.DENSE_FOREST_LAND_TREES, LandBiomeType.ROUGH);
			case TAIGA -> builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.DENSE_TAIGA_LAND_TREES, LandBiomeType.ROUGH);
		}
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY, LandBiomeType.OCEAN);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.ROOTED_DIRT.defaultBlockState(), 80),
				CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 20),
				CountPlacement.of(3), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17),
						CountPlacement.of(30), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_EMERALD, LandBiomeType.any());
		
		builder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE, LandBiomeType.any());
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		IguanaVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, RandomSource random)
	{
		IguanaVillagePieces.addPieces(register, random);
	}
	
	private enum Variant
	{
		FOREST,
		TAIGA
	}
}