package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.IguanaVillagePieces;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
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

public class FrostLandType extends TerrainLandType
{
	public static final String FROST = "minestuck.frost";
	public static final String ICE = "minestuck.ice";
	public static final String SNOW = "minestuck.snow";
	
	public FrostLandType()
	{
		super(new Builder(MSEntityTypes.IGUANA::get).names(FROST, ICE, SNOW)
				.skylight(7/8F).fogColor(0.5, 0.6, 0.98).skyColor(0.6, 0.7, 0.9)
				.biomeSet(MSBiomes.SNOW_LAND).category(Biome.BiomeCategory.ICY).music(() -> MSSoundEvents.MUSIC_FROST));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("surface", Blocks.GRASS_BLOCK);
		registry.setBlock("upper", Blocks.DIRT);
		registry.setBlock("structure_primary", MSBlocks.FROST_TILE);
		registry.setBlock("structure_primary_decorative", MSBlocks.CHISELED_FROST_BRICKS);
		registry.setBlock("structure_primary_cracked", MSBlocks.CRACKED_FROST_BRICKS);
		registry.setBlock("structure_primary_column", MSBlocks.FROST_COLUMN);
		registry.setBlock("structure_primary_stairs", MSBlocks.FROST_TILE_STAIRS);
		registry.setBlock("structure_secondary", MSBlocks.FROST_BRICKS);
		registry.setBlock("structure_secondary_stairs", MSBlocks.FROST_BRICK_STAIRS);
		registry.setBlock("structure_secondary_decorative", MSBlocks.CHISELED_FROST_BRICKS);
		registry.setBlock("structure_planks", Blocks.SPRUCE_PLANKS);
		registry.setBlock("structure_planks_slab", Blocks.SPRUCE_SLAB);
		registry.setBlock("river", Blocks.ICE);
		registry.setBlock("light_block", Blocks.SEA_LANTERN);
		registry.setBlock("bucket_1", Blocks.SNOW_BLOCK);
		registry.setBlock("bush", Blocks.FERN);
		registry.setBlock("structure_wool_1", Blocks.WHITE_WOOL);
		registry.setBlock("structure_wool_3", Blocks.CYAN_WOOL);
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.forceRain = LandProperties.ForceType.ON;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = -0.3F;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		
		if(type != LandBiomeType.NORMAL)
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.COARSE_DIRT.defaultBlockState(), UniformInt.of(2, 6), 2, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.SNOW_BLOCK.defaultBlockState(), UniformInt.of(2, 4), 2, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(15), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.SNOW_BLOCK.defaultBlockState(), UniformInt.of(2, 3), 2, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.ICE.defaultBlockState(), UniformInt.of(2, 3), 1, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		}
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.PACKED_ICE.defaultBlockState(), 8),
				CountPlacement.of(2), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.SNOW_BLOCK.defaultBlockState(), 16),
				CountPlacement.of(3), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 28),
				CountPlacement.of(3), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17),
				CountPlacement.of(13), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 6),
				CountPlacement.of(3), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24)), BiomeFilter.biome()));

		BiomeDefaultFeatures.addSurfaceFreezing(builder);
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		IguanaVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		IguanaVillagePieces.addPieces(register, random);
	}
}