package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.NakagatorVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import static com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry.*;

public class EndLandType extends TerrainLandType
{
	public static final String END = "minestuck.end";
	public static final String DIMENSION = "minestuck.dimension";
	
	public EndLandType()
	{
		super(new Builder(MSEntityTypes.NAKAGATOR).names(END, DIMENSION)
				.fogColor(0.0, 0.4, 0.2).skyColor(0.3, 0.1, 0.5)
				.biomeSet(MSBiomes.NO_RAIN_LAND).music(MSSoundEvents.MUSIC_END));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock(CRUXITE_ORE, MSBlocks.END_STONE_CRUXITE_ORE);
		registry.setBlock(URANIUM_ORE, MSBlocks.END_STONE_URANIUM_ORE);
		registry.setBlock(GROUND, MSBlocks.COARSE_END_STONE);
		registry.setBlock(UPPER, Blocks.END_STONE);
		registry.setBlock(SURFACE, Blocks.END_STONE);
		registry.setBlock(SURFACE_ROUGH, MSBlocks.END_GRASS);
		registry.setBlock(OCEAN, MSBlocks.ENDER);
		
		registry.setBlock(STRUCTURE_PRIMARY, Blocks.END_STONE_BRICKS);
		registry.setBlockState(STRUCTURE_PRIMARY_DECORATIVE, Blocks.PURPUR_PILLAR.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
		registry.setBlockState(STRUCTURE_PRIMARY_COLUMN, Blocks.PURPUR_PILLAR.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
		registry.setBlock(STRUCTURE_PRIMARY_STAIRS, Blocks.END_STONE_BRICK_STAIRS);
		registry.setBlock(STRUCTURE_PRIMARY_SLAB, Blocks.END_STONE_BRICK_SLAB);
		registry.setBlock(STRUCTURE_PRIMARY_WALL, Blocks.END_STONE_BRICK_WALL);
		
		registry.setBlock(STRUCTURE_SECONDARY, Blocks.PURPUR_BLOCK);
		registry.setBlock(STRUCTURE_SECONDARY_STAIRS, Blocks.PURPUR_STAIRS);
		registry.setBlock(STRUCTURE_SECONDARY_SLAB, Blocks.PURPUR_SLAB);
		
		registry.setBlock(STRUCTURE_WOOD, MSBlocks.END_WOOD);
		registry.setBlock(STRUCTURE_LOG, MSBlocks.END_LOG);
		registry.setBlock(STRUCTURE_STRIPPED_WOOD, MSBlocks.STRIPPED_END_WOOD);
		registry.setBlock(STRUCTURE_STRIPPED_LOG, MSBlocks.STRIPPED_END_LOG);
		registry.setBlock(STRUCTURE_PLANKS, MSBlocks.END_PLANKS);
		registry.setBlock(STRUCTURE_BOOKSHELF, MSBlocks.END_BOOKSHELF);
		registry.setBlock(STRUCTURE_PLANKS_STAIRS, MSBlocks.END_STAIRS);
		registry.setBlock(STRUCTURE_PLANKS_SLAB, MSBlocks.END_SLAB);
		registry.setBlock(STRUCTURE_PLANKS_FENCE, MSBlocks.END_FENCE);
		registry.setBlock(STRUCTURE_PLANKS_FENCE_GATE, MSBlocks.END_FENCE_GATE);
		registry.setBlock(STRUCTURE_PLANKS_DOOR, MSBlocks.END_DOOR);
		registry.setBlock(STRUCTURE_PLANKS_TRAPDOOR, MSBlocks.END_TRAPDOOR);
		
		registry.setBlock(VILLAGE_PATH, MSBlocks.COARSE_END_STONE);
		registry.setBlock(STRUCTURE_WOOL_1, Blocks.GREEN_WOOL);
		registry.setBlock(STRUCTURE_WOOL_3, Blocks.PURPLE_WOOL);
		
		registry.setBlock(STRUCTURE_GROUND_COVER, MSBlocks.TALL_END_GRASS);
		registry.setBlock(STRUCTURE_ROOF_COVER, MSBlocks.END_LEAVES);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.END_TREE,
				LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.END_GRASS_SURFACE_DISK, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.TALL_END_GRASS_PATCH, LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.END_STONE_SURFACE_DISK, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.TALL_END_GRASS_PATCH, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.END_STONE_IRON_ORE.get().defaultBlockState(), 9),
						CountPlacement.of(40), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.END_STONE_REDSTONE_ORE.get().defaultBlockState(), 8),
						CountPlacement.of(30), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.END_STONE.defaultBlockState(), 36),
						CountPlacement.of(80), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
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