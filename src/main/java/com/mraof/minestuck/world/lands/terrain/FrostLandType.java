package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.IguanaVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
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

import static com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry.*;

public class FrostLandType extends TerrainLandType
{
	public static final String FROST = "minestuck.frost";
	public static final String ICE = "minestuck.ice";
	public static final String SNOW = "minestuck.snow";
	
	public FrostLandType()
	{
		super(new Builder(MSEntityTypes.IGUANA).names(FROST, ICE, SNOW)
				.skylight(7/8F).fogColor(0.5, 0.6, 0.98).skyColor(0.6, 0.7, 0.9)
				.biomeSet(MSBiomes.SNOW_LAND).music(MSSoundEvents.MUSIC_FROST));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock(UPPER, Blocks.DIRT);
		registry.setBlock(SURFACE, Blocks.GRASS_BLOCK);
		registry.setBlock(RIVER, Blocks.ICE);
		
		registry.setBlock(STRUCTURE_PRIMARY, MSBlocks.FROST_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_DECORATIVE, MSBlocks.CHISELED_FROST_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_CRACKED, MSBlocks.CRACKED_FROST_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_COLUMN, MSBlocks.FROST_COLUMN);
		registry.setBlock(STRUCTURE_PRIMARY_STAIRS, MSBlocks.FROST_BRICK_STAIRS);
		registry.setBlock(STRUCTURE_PRIMARY_SLAB, MSBlocks.FROST_BRICK_SLAB);
		registry.setBlock(STRUCTURE_PRIMARY_WALL, MSBlocks.FROST_BRICK_WALL);
		
		registry.setBlock(STRUCTURE_PRIMARY_MOSSY, MSBlocks.FLOWERY_FROST_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_MOSSY_STAIRS, MSBlocks.FLOWERY_FROST_BRICK_STAIRS);
		registry.setBlock(STRUCTURE_PRIMARY_MOSSY_SLAB, MSBlocks.FLOWERY_FROST_BRICK_SLAB);
		registry.setBlock(STRUCTURE_PRIMARY_MOSSY_WALL, MSBlocks.FLOWERY_FROST_BRICK_WALL);
		
		registry.setBlock(STRUCTURE_SECONDARY, MSBlocks.FROST_TILE);
		registry.setBlock(STRUCTURE_SECONDARY_DECORATIVE, MSBlocks.CHISELED_FROST_TILE);
		registry.setBlock(STRUCTURE_SECONDARY_STAIRS, MSBlocks.FROST_TILE_STAIRS);
		registry.setBlock(STRUCTURE_SECONDARY_SLAB, MSBlocks.FROST_TILE_SLAB);
		registry.setBlock(STRUCTURE_SECONDARY_WALL, MSBlocks.FROST_TILE_WALL);
		
		registry.setBlock(STRUCTURE_PLANKS, Blocks.SPRUCE_PLANKS);
		registry.setBlock(STRUCTURE_PLANKS_SLAB, Blocks.SPRUCE_SLAB);
		
		registry.setBlock(STRUCTURE_WOOL_1, Blocks.WHITE_WOOL);
		registry.setBlock(STRUCTURE_WOOL_3, Blocks.CYAN_WOOL);
		
		registry.setBlock(LIGHT_BLOCK, Blocks.SEA_LANTERN);
		registry.setBlock(BUCKET_1, Blocks.SNOW_BLOCK);
		registry.setBlock(BUSH, Blocks.FERN);
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
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_PACKED, LandBiomeType.OCEAN);
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacements.ICEBERG_BLUE, LandBiomeType.OCEAN);
		
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.ICE_SPIKE, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA_FROZEN, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.FROST_TREE, LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.COARSE_DIRT_DISK, LandBiomeType.anyExcept(LandBiomeType.NORMAL));
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.SNOW_BLOCK_DISK, LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.SMALL_SNOW_BLOCK_DISK, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.ICE_DISK, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.PACKED_ICE.defaultBlockState(), 40),
						CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.SNOW_BLOCK.defaultBlockState(), 80),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 28),
						CountPlacement.of(6), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17),
						CountPlacement.of(26), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 6),
						CountPlacement.of(11), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)), BiomeFilter.biome()),
				LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MiscOverworldPlacements.FREEZE_TOP_LAYER, LandBiomeType.any());
		
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