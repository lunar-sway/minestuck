package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageCenter;
import com.mraof.minestuck.world.gen.structure.village.NakagatorVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

public class RockLandType extends TerrainLandType
{
	public static final String ROCK = "minestuck.rock";
	public static final String STONE = "minestuck.stone";
	public static final String ORE = "minestuck.ore";
	public static final String PETRIFICATION = "minestuck.petrification";
	
	private final Variant type;
	
	public static TerrainLandType createRock()
	{
		return new RockLandType(Variant.ROCK, new Builder(MSEntityTypes.NAKAGATOR).names(ROCK, STONE, ORE)
				.skylight(7/8F).fogColor(0.5, 0.5, 0.55).skyColor(0.6, 0.6, 0.7)
				.music(MSSoundEvents.MUSIC_ROCK));
	}
	
	public static TerrainLandType createPetrification()
	{
		return new RockLandType(Variant.PETRIFICATION, new Builder(MSEntityTypes.NAKAGATOR).names(PETRIFICATION)
				.skylight(7/8F).fogColor(0.5, 0.5, 0.55).skyColor(0.6, 0.6, 0.7)
				.music(MSSoundEvents.MUSIC_PETRIFICATION));
	}
	
	private RockLandType(Variant variation, Builder builder)
	{
		super(builder);
		type = variation;
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.PETRIFICATION)
		{
			registry.setBlock("surface", Blocks.STONE);
		} else
		{
			registry.setBlock("surface", Blocks.GRAVEL);
		}
		
		registry.setBlock("upper", Blocks.COBBLESTONE);
		registry.setBlock("structure_primary", MSBlocks.COARSE_STONE_BRICKS);
		registry.setBlock("structure_primary_decorative", MSBlocks.CHISELED_COARSE_STONE_BRICKS);
		registry.setBlock("structure_primary_stairs", MSBlocks.COARSE_STONE_BRICK_STAIRS);
		registry.setBlock("structure_secondary", MSBlocks.COARSE_STONE);
		registry.setBlock("structure_secondary_decorative", MSBlocks.CHISELED_COARSE_STONE);
		registry.setBlock("structure_secondary_stairs", MSBlocks.COARSE_STONE_STAIRS);
		registry.setBlock("structure_planks_slab", Blocks.BRICK_SLAB);
		registry.setBlock("village_path", Blocks.MOSSY_COBBLESTONE);
		registry.setBlock("village_fence", Blocks.COBBLESTONE_WALL);
		registry.setBlock("structure_wool_1", Blocks.BROWN_WOOL);
		registry.setBlock("structure_wool_3", Blocks.GRAY_WOOL);
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = -0.3F;
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.DISK,
				new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.CLAY), BlockPredicate.matchesBlocks(blocks.getBlockState("ocean_surface").getBlock(), Blocks.CLAY), UniformInt.of(2, 5), 2),
				CountPlacement.of(25), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID), LandBiomeType.OCEAN);
		
		if(this.type == Variant.ROCK)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_TREE, LandBiomeType.ROUGH);
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.RANDOM_ROCK_BLOCK_BLOB, LandBiomeType.NORMAL);
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.LARGE_RANDOM_ROCK_BLOCK_BLOB, LandBiomeType.ROUGH);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_GRASS_PATCH, LandBiomeType.NORMAL);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_GRASS_PATCH, LandBiomeType.ROUGH);
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.COBBLESTONE_SURFACE_DISK, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.STONE_SURFACE_DISK, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.STONE_MOUND, LandBiomeType.ROUGH);
		} else if(this.type == Variant.PETRIFICATION)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_TREE, LandBiomeType.NORMAL);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_TREE, LandBiomeType.ROUGH);
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.COBBLESTONE_BLOCK_BLOB, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_GRASS_PATCH, LandBiomeType.NORMAL);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_POPPY_PATCH, LandBiomeType.NORMAL);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_GRASS_PATCH, LandBiomeType.ROUGH);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_POPPY_PATCH, LandBiomeType.ROUGH);
		}
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, MSPlacedFeatures.DRIPSTONE_CLUSTER, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, MSPlacedFeatures.OCEANIC_DRIPSTONE_CLUSTER, LandBiomeType.OCEAN);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, MSPlacedFeatures.LARGE_DRIPSTONE, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, MSPlacedFeatures.POINTED_DRIPSTONE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, MSPlacedFeatures.OCEANIC_POINTED_DRIPSTONE, LandBiomeType.OCEAN);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, CavePlacements.AMETHYST_GEODE, LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
						CountPlacement.of(12), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.INFESTED_STONE.defaultBlockState(), 9),
						CountPlacement.of(14), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());

		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRANITE_UPPER, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRANITE_LOWER, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIORITE_UPPER, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIORITE_LOWER, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_UPPER, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_LOWER, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_TUFF, LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 9),
						CountPlacement.of(30), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.IRON_ORE.defaultBlockState(), 5),
						CountPlacement.of(30), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 5),
						CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 4),
						CountPlacement.of(11), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.STONE_QUARTZ_ORE.get().defaultBlockState(), 4),
						CountPlacement.of(11), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.GOLD_ORE.defaultBlockState(), 5),
						CountPlacement.of(9), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 4),
						CountPlacement.of(7), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)), BiomeFilter.biome()),
				LandBiomeType.any());
		
		//cave carver that creates more cavernous sections, may end up as just a placeholder for noise based cave generation
		builder.addCarver(GenerationStep.Carving.AIR, WorldCarver.CAVE.configured(new CaveCarverConfiguration(0.08F,
						UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)),
						UniformFloat.of(0.1F, 0.9F), VerticalAnchor.aboveBottom(8), BuiltInRegistries.BLOCK.getOrCreateTag(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
						UniformFloat.of(0.7F, 4.4F), UniformFloat.of(0.8F, 4.3F), UniformFloat.of(-1.0F, -0.4F))),
				LandBiomeType.any());
		builder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE, LandBiomeType.any());
		builder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND, LandBiomeType.any());
		builder.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON, LandBiomeType.any());
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		NakagatorVillagePieces.addCenters(register);
		
		register.add(ConsortVillageCenter.RockCenter::new, 5);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, RandomSource random)
	{
		NakagatorVillagePieces.addPieces(register, random);
	}
	
	private enum Variant
	{
		ROCK,
		PETRIFICATION
	}
}