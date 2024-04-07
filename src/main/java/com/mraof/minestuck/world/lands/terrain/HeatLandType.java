package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.NakagatorVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

public class HeatLandType extends TerrainLandType
{
	public static final String HEAT = "minestuck.heat";
	public static final String FLAME = "minestuck.flame";
	public static final String FIRE = "minestuck.fire";
	
	public HeatLandType()
	{
		super(new Builder(MSEntityTypes.NAKAGATOR).names(HEAT, FLAME, FIRE)
				.skylight(1/2F).fogColor(0.4, 0.0, 0.0)
				.biomeSet(MSBiomes.NO_RAIN_LAND).music(MSSoundEvents.MUSIC_HEAT));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("ground", MSBlocks.BLACK_STONE);
		registry.setBlock("surface", MSBlocks.IGNEOUS_STONE);
		registry.setBlock("upper", MSBlocks.PUMICE_STONE);
		registry.setBlock("ocean", MSBlocks.MOLTEN_AMBER);
		registry.setBlock("structure_primary", MSBlocks.BLACK_STONE_BRICKS);
		registry.setBlock("structure_primary_decorative", MSBlocks.CHISELED_BLACK_STONE_BRICKS);
		registry.setBlock("structure_primary_cracked", MSBlocks.CRACKED_BLACK_STONE_BRICKS);
		registry.setBlock("structure_primary_mossy", MSBlocks.MAGMATIC_BLACK_STONE_BRICKS);
		registry.setBlock("structure_primary_column", MSBlocks.BLACK_STONE_COLUMN);
		registry.setBlock("structure_primary_stairs", MSBlocks.BLACK_STONE_BRICK_STAIRS);
		registry.setBlock("structure_secondary", MSBlocks.POLISHED_IGNEOUS_BRICKS);
		registry.setBlock("structure_secondary_decorative", MSBlocks.CHISELED_IGNEOUS_STONE);
		registry.setBlock("structure_secondary_stairs", MSBlocks.POLISHED_IGNEOUS_BRICK_STAIRS);
		registry.setBlock("light_block", MSBlocks.HEAT_LAMP);
		registry.setBlock("fall_fluid", Blocks.WATER);
		registry.setBlock("structure_planks", MSBlocks.CINDERED_PLANKS);
		registry.setBlock("structure_planks_slab", MSBlocks.CINDERED_SLAB);
		registry.setBlock("village_path", MSBlocks.BLACK_SAND);
		registry.setBlock("village_fence", MSBlocks.POLISHED_IGNEOUS_BRICK_WALL);
		registry.setBlock("village_door", MSBlocks.CINDERED_DOOR);
		registry.setBlock("structure_wool_1", Blocks.YELLOW_WOOL);
		registry.setBlock("structure_wool_3", Blocks.PURPLE_WOOL);
		registry.setBlock("cruxite_ore", MSBlocks.BLACK_STONE_CRUXITE_ORE);
		registry.setBlock("uranium_ore", MSBlocks.BLACK_STONE_URANIUM_ORE);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.CINDERED_TREE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SINGED_GRASS_PATCH, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SINGED_FOLIAGE_PATCH, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.IGNEOUS_SPIKE_PATCH, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SULFUR_BUBBLE_PATCH, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.SULFUR_POOL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.CAST_IRON_BUILDING, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.CAST_IRON_PLATFORM, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		builder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MSPlacedFeatures.OCEAN_RUNDOWN, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.MAGMATIC_IGNEOUS_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.BLACK_SAND_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.PUMICE_STONE_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.FIRE_FIELD, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.EXTRA_FIRE_FIELD, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.BLACK_STONE.get().defaultBlockState(), 80),
						CountPlacement.of(6), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.BLACK_STONE_GOLD_ORE.get().defaultBlockState(), 9),
						CountPlacement.of(18), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.BLACK_STONE_REDSTONE_ORE.get().defaultBlockState(), 7),
						CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.BLACK_STONE_QUARTZ_ORE.get().defaultBlockState(), 8),
						CountPlacement.of(26), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		
		builder.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE, LandBiomeType.any());
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		NakagatorVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, RandomSource random)
	{
		NakagatorVillagePieces.addPieces(register, random);
	}
}