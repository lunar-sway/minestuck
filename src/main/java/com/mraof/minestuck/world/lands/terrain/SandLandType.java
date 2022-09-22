
package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageCenter;
import com.mraof.minestuck.world.gen.structure.village.TurtleVillagePieces;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import java.util.List;
import java.util.Random;

public class SandLandType extends TerrainLandType
{
	public static final String SAND = "minestuck.sand";
	public static final String DUNES = "minestuck.dunes";
	public static final String DESERTS = "minestuck.deserts";
	public static final String LUSH_DESERTS = "minestuck.lush_deserts";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "sand");
	private final Variant type;
	
	public static TerrainLandType createSand()
	{
		return new SandLandType(Variant.SAND, new Builder(MSEntityTypes.TURTLE).group(GROUP_NAME).names(SAND, DUNES, DESERTS)
				.fogColor(0.99, 0.8, 0.05).skyColor(0.8, 0.8, 0.1)
				.biomeSet(MSBiomes.NO_RAIN_LAND).category(Biome.BiomeCategory.DESERT).music(() -> MSSoundEvents.MUSIC_SAND));
	}
	
	public static TerrainLandType createLushDeserts()
	{
		return new SandLandType(Variant.LUSH_DESERTS, new Builder(MSEntityTypes.TURTLE).group(GROUP_NAME).names(LUSH_DESERTS)
				.fogColor(0.99, 0.8, 0.05).skyColor(0.8, 0.8, 0.1)
				.biomeSet(MSBiomes.NO_RAIN_LAND).category(Biome.BiomeCategory.DESERT).music(() -> MSSoundEvents.MUSIC_LUSH_DESERTS));
	}
	
	public static TerrainLandType createRedSand()
	{
		return new SandLandType(Variant.RED_SAND, new Builder(MSEntityTypes.TURTLE).group(GROUP_NAME).names(SAND, DUNES, DESERTS)
				.fogColor(0.99, 0.6, 0.05).skyColor(0.8, 0.6, 0.1)
				.biomeSet(MSBiomes.NO_RAIN_LAND).category(Biome.BiomeCategory.DESERT).music(() -> MSSoundEvents.MUSIC_SAND));
	}
	
	private SandLandType(Variant variation, Builder builder)
	{
		super(builder);
		type = variation;
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SAND || type == Variant.LUSH_DESERTS)
		{
			registry.setBlock("ground", Blocks.SANDSTONE);
			registry.setBlock("upper", Blocks.SAND);
			registry.setBlock("structure_primary", Blocks.SMOOTH_SANDSTONE);
			registry.setBlock("structure_primary_decorative", Blocks.CHISELED_SANDSTONE);
			registry.setBlock("structure_primary_stairs", Blocks.SANDSTONE_STAIRS);
			registry.setBlock("village_path", Blocks.RED_SAND);
			registry.setBlock("cruxite_ore", MSBlocks.SANDSTONE_CRUXITE_ORE);
			registry.setBlock("uranium_ore", MSBlocks.SANDSTONE_URANIUM_ORE);
		} else
		{
			registry.setBlock("ground", Blocks.RED_SANDSTONE);
			registry.setBlock("upper", Blocks.RED_SAND);
			registry.setBlock("structure_primary", Blocks.SMOOTH_RED_SANDSTONE);
			registry.setBlock("structure_primary_decorative", Blocks.CHISELED_RED_SANDSTONE);
			registry.setBlock("structure_primary_stairs", Blocks.RED_SANDSTONE_STAIRS);
			registry.setBlock("village_path", Blocks.SAND);
			registry.setBlock("cruxite_ore", MSBlocks.RED_SANDSTONE_CRUXITE_ORE);
			registry.setBlock("uranium_ore", MSBlocks.RED_SANDSTONE_URANIUM_ORE);
		}
		registry.setBlock("structure_secondary", Blocks.STONE_BRICKS);
		registry.setBlock("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS);
		registry.setBlock("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS);
		registry.setBlock("structure_planks", Blocks.ACACIA_PLANKS);
		registry.setBlock("structure_planks_slab", Blocks.ACACIA_SLAB);
		registry.setBlockState("river", registry.getBlockState("upper"));
		registry.setBlock("structure_wool_1", Blocks.YELLOW_WOOL);
		registry.setBlock("structure_wool_3", Blocks.MAGENTA_WOOL);
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = -1;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(this.type == Variant.LUSH_DESERTS)
		{
			
			if(type == LandBiomeType.NORMAL)
			{
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.OASIS.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.DESERT_BUSH_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
			}
			if(type == LandBiomeType.ROUGH)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_DESERT_BUSH_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BLOOMING_CACTUS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
			}
		} else
		{
			BiomeDefaultFeatures.addDesertVegetation(builder);
			if(type == LandBiomeType.NORMAL)
			{
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.OASIS.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
			}
			if(type == LandBiomeType.ROUGH)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BLOOMING_CACTUS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
			}
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
				new DiskConfiguration(blocks.getBlockState("upper"), UniformInt.of(2, 6), 2, List.of(blocks.getBlockState("ground"))),
				CountPlacement.of(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		if(this.type != Variant.RED_SAND)
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), MSBlocks.SANDSTONE_IRON_ORE.get().defaultBlockState(), 9),
					CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), MSBlocks.SANDSTONE_GOLD_ORE.get().defaultBlockState(), 9),
					CountPlacement.of(6), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		} else
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_IRON_ORE.get().defaultBlockState(), 9),
					CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_GOLD_ORE.get().defaultBlockState(), 9),
					CountPlacement.of(6), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		}
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		TurtleVillagePieces.addCenters(register);
		
		register.add(ConsortVillageCenter.CactusPyramidCenter::new, 5);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		TurtleVillagePieces.addPieces(register, random);
	}
	
	private enum Variant
	{
		SAND,
		LUSH_DESERTS,
		RED_SAND
	}
}
