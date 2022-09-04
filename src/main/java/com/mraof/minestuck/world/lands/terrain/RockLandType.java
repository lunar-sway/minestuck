package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageCenter;
import com.mraof.minestuck.world.gen.structure.village.NakagatorVillagePieces;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;
import java.util.Random;

public class RockLandType extends TerrainLandType
{
	public static final String ROCK = "minestuck.rock";
	public static final String STONE = "minestuck.stone";
	public static final String ORE = "minestuck.ore";
	public static final String PETRIFICATION = "minestuck.petrification";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "rock");
	private final Variant type;
	
	public static TerrainLandType createRock()
	{
		return new RockLandType(Variant.ROCK, new Builder(() -> MSEntityTypes.NAKAGATOR).group(GROUP_NAME).names(ROCK, STONE, ORE)
				.skylight(7/8F).fogColor(0.5, 0.5, 0.55).skyColor(0.6, 0.6, 0.7)
				.category(Biome.BiomeCategory.EXTREME_HILLS).music(() -> MSSoundEvents.MUSIC_ROCK));
	}
	
	public static TerrainLandType createPetrification()
	{
		return new RockLandType(Variant.PETRIFICATION, new Builder(() -> MSEntityTypes.NAKAGATOR).group(GROUP_NAME).names(PETRIFICATION)
				.skylight(7/8F).fogColor(0.5, 0.5, 0.55).skyColor(0.6, 0.6, 0.7)
				.category(Biome.BiomeCategory.EXTREME_HILLS).music(() -> MSSoundEvents.MUSIC_PETRIFICATION));
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
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		int biomeMultiplier = type == LandBiomeType.ROUGH || type == LandBiomeType.OCEAN ? 2 : 1;
		
		if(type == LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.CLAY.defaultBlockState(), UniformInt.of(2, 5), 2, List.of(blocks.getBlockState("ocean_surface"), Blocks.CLAY.defaultBlockState())),
					CountPlacement.of(25), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID));
		}
		
		if(type == LandBiomeType.NORMAL)
		{
			if(this.type == Variant.ROCK)
			{
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.RANDOM_ROCK_BLOCK_BLOB.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_GRASS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
						new DiskConfiguration(Blocks.COBBLESTONE.defaultBlockState(), UniformInt.of(2, 5), 1, List.of(blocks.getBlockState("surface"), Blocks.COBBLESTONE.defaultBlockState())),
						RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
				builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
						new DiskConfiguration(Blocks.STONE.defaultBlockState(), UniformInt.of(2, 4), 2, List.of(blocks.getBlockState("surface"), Blocks.STONE.defaultBlockState())),
						RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
			} else if(this.type == Variant.PETRIFICATION)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_TREE.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.COBBLESTONE_BLOCK_BLOB.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_GRASS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_POPPY_PATCH.getHolder().orElseThrow());
			}
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			if(this.type == Variant.ROCK)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_TREE.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.LARGE_RANDOM_ROCK_BLOCK_BLOB.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_GRASS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, PlacementUtils.inlinePlaced(MSFeatures.STONE_MOUND.get(),
						new BlockStateConfiguration(blocks.getBlockState("ground")),
						InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
				builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
						new DiskConfiguration(Blocks.COBBLESTONE.defaultBlockState(), UniformInt.of(2, 5), 1, List.of(blocks.getBlockState("surface"), Blocks.COBBLESTONE.defaultBlockState())),
						RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
				builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
						new DiskConfiguration(Blocks.STONE.defaultBlockState(), UniformInt.of(2, 4), 2, List.of(blocks.getBlockState("surface"), Blocks.STONE.defaultBlockState())),
						RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
			} else if(this.type == Variant.PETRIFICATION)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_TREE.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.COBBLESTONE_BLOCK_BLOB.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_GRASS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_POPPY_PATCH.getHolder().orElseThrow());
			}
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
				CountPlacement.of(10 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.INFESTED_STONE.defaultBlockState(), 9),
				CountPlacement.of(7 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 9),
				CountPlacement.of(15 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.IRON_ORE.defaultBlockState(), 5),
				CountPlacement.of(15 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 5),
				CountPlacement.of(8 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 4),
				CountPlacement.of(3 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.STONE_QUARTZ_ORE.get().defaultBlockState(), 4),
				CountPlacement.of(3 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.GOLD_ORE.defaultBlockState(), 5),
				CountPlacement.of(3 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 4),
				CountPlacement.of(2 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24 * biomeMultiplier)), BiomeFilter.biome()));
		
		builder.addCarver(GenerationStep.Carving.AIR, Holder.direct(WorldCarver.CANYON.configured(new CanyonCarverConfiguration(0.4F,
				BiasedToBottomHeight.of(VerticalAnchor.absolute(20), VerticalAnchor.absolute(67), 8),
				ConstantFloat.of(3), VerticalAnchor.absolute(10), CarverDebugSettings.DEFAULT, UniformFloat.of(-0.125F, 0.125F),
				new CanyonCarverConfiguration.CanyonShapeConfiguration(UniformFloat.of(0.75F, 1F), TrapezoidFloat.of(0, 6, 2), 3, UniformFloat.of(0.75F, 1F), 1F, 0F)))));
		
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		NakagatorVillagePieces.addCenters(register);
		
		register.add(ConsortVillageCenter.RockCenter::new, 5);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		NakagatorVillagePieces.addPieces(register, random);
	}
	
	private enum Variant
	{
		ROCK,
		PETRIFICATION
	}
}