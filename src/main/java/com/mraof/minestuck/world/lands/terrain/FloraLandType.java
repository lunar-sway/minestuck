package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.IguanaVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

public class FloraLandType extends TerrainLandType
{
	public static final String FLORA = "minestuck.flora";
	public static final String FLOWERS = "minestuck.flowers";
	public static final String THORNS = "minestuck.thorns";
	
	public FloraLandType()
	{
		super(new Builder(MSEntityTypes.IGUANA).names(FLORA, FLOWERS, THORNS)
				.fogColor(0.5, 0.6, 0.9).skyColor(0.6, 0.8, 0.6)
				.music(MSSoundEvents.MUSIC_FLORA));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("surface", Blocks.GRASS_BLOCK);
		registry.setBlock("surface_rough", Blocks.MUD);
		registry.setBlock("upper", Blocks.DIRT);
		registry.setBlock("ocean", MSBlocks.BLOOD);
		registry.setBlock("ocean_surface", Blocks.MUD);
		registry.setBlock("structure_primary", MSBlocks.MOSSY_DECREPIT_STONE_BRICKS);
		registry.setBlock("structure_primary_decorative", MSBlocks.FLOWERY_MOSSY_STONE_BRICKS);
		registry.setBlock("structure_primary_mossy", MSBlocks.MOSSY_DECREPIT_STONE_BRICKS);
		registry.setBlock("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS);
		registry.setBlock("structure_secondary", Blocks.MOSSY_COBBLESTONE);
		registry.setBlock("structure_secondary_decorative", MSBlocks.FLOWERY_MOSSY_COBBLESTONE);
		registry.setBlock("structure_secondary_stairs", Blocks.DARK_OAK_STAIRS);
		registry.setBlock("village_path", Blocks.DIRT_PATH);
		registry.setBlock("bush", Blocks.FERN);
		registry.setBlock("structure_wool_1", Blocks.YELLOW_WOOL);
		registry.setBlock("structure_wool_3", Blocks.CYAN_WOOL);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FLOWER_FOREST, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.DARK_OAK, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.UNCOMMON_BROKEN_SWORD, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.RARE_STRAWBERRY_PATCH, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_MOSS_CARPET_PATCH, LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.BLOOD_POOL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.SURFACE_FOSSIL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.BROKEN_SWORD, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.STRAWBERRY_PATCH, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.ROOTED_AZALEA_TREE, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.CAVE_VINES, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, MSPlacedFeatures.LUSH_CAVES_CEILING_VEGETATION, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, MSPlacedFeatures.LUSH_CAVES_VEGETATION, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.SPORE_BLOSSOM, LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.DISK,
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.CLAY), BlockPredicate.matchesBlocks(blocks.getBlockState("ocean_surface").getBlock(), Blocks.CLAY), UniformInt.of(2, 3), 1),
				InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()), LandBiomeType.OCEAN);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 33),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17),
						CountPlacement.of(26), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.EMERALD_ORE.defaultBlockState(), 3),
						CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 3),
						CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 3),
						CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.STONE_QUARTZ_ORE.get().defaultBlockState(), 5),
						CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		
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
}