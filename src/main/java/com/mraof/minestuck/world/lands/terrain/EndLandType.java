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
		registry.setBlock("ground", MSBlocks.COARSE_END_STONE);
		registry.setBlock("surface", Blocks.END_STONE);
		registry.setBlock("surface_rough", MSBlocks.END_GRASS);
		registry.setBlock("upper", Blocks.END_STONE);
		registry.setBlock("ocean", MSBlocks.ENDER);
		registry.setBlock("structure_primary", Blocks.END_STONE_BRICKS);
		registry.setBlockState("structure_primary_decorative", Blocks.PURPUR_PILLAR.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
		registry.setBlock("structure_primary_stairs", Blocks.END_STONE_BRICKS);
		registry.setBlock("structure_secondary", Blocks.PURPUR_BLOCK);
		registry.setBlock("structure_secondary_stairs", Blocks.PURPUR_STAIRS);
		registry.setBlock("structure_planks", Blocks.BRICKS);
		registry.setBlock("structure_planks_slab", Blocks.BRICK_SLAB);
		registry.setBlock("village_path", MSBlocks.COARSE_END_STONE);
		registry.setBlock("village_fence", Blocks.NETHER_BRICK_FENCE);
		registry.setBlock("structure_wool_1", Blocks.GREEN_WOOL);
		registry.setBlock("structure_wool_3", Blocks.PURPLE_WOOL);
		registry.setBlock("cruxite_ore", MSBlocks.END_STONE_CRUXITE_ORE);
		registry.setBlock("uranium_ore", MSBlocks.END_STONE_URANIUM_ORE);
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