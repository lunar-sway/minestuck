package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.TurtleVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import static com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry.*;

public class SandstoneLandType extends TerrainLandType
{
	public static final String SANDSTONE = "minestuck.sandstone";
	public static final String STONY_DESERTS = "minestuck.stony_deserts";
	
	private final Variant type;
	
	public static TerrainLandType createSandstone()
	{
		return new SandstoneLandType(Variant.SANDSTONE, new Builder(MSEntityTypes.TURTLE).names(SANDSTONE, STONY_DESERTS)
				.skylight(3/4F).fogColor(0.9, 0.7, 0.05).skyColor(0.8, 0.6, 0.2)
				.music(MSSoundEvents.MUSIC_SANDSTONE));
	}
	
	public static TerrainLandType createRedSandstone()
	{
		return new SandstoneLandType(Variant.RED_SANDSTONE, new Builder(MSEntityTypes.TURTLE).names(SANDSTONE, STONY_DESERTS)
				.skylight(3/4F).fogColor(0.7, 0.4, 0.05).skyColor(0.8, 0.5, 0.1)
				.music(MSSoundEvents.MUSIC_SANDSTONE));
	}
	
	private SandstoneLandType(Variant type, Builder builder)
	{
		super(builder);
		this.type = type;
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SANDSTONE)
		{
			registry.setBlock(SAND, Blocks.SAND);
			registry.setBlock(OCEAN_SURFACE, Blocks.SAND);
			registry.setBlock(UPPER, Blocks.SANDSTONE);
			registry.setBlock(STRUCTURE_PRIMARY, Blocks.SMOOTH_SANDSTONE);
			registry.setBlock(STRUCTURE_PRIMARY_DECORATIVE, Blocks.CHISELED_SANDSTONE);
			registry.setBlock(STRUCTURE_PRIMARY_STAIRS, Blocks.SANDSTONE_STAIRS);
			registry.setBlock(VILLAGE_PATH, Blocks.RED_SAND);
		} else
		{
			registry.setBlock(SAND, Blocks.RED_SAND);
			registry.setBlock(OCEAN_SURFACE, Blocks.RED_SAND);
			registry.setBlock(UPPER, Blocks.RED_SANDSTONE);
			registry.setBlock(STRUCTURE_PRIMARY, Blocks.SMOOTH_RED_SANDSTONE);
			registry.setBlock(STRUCTURE_PRIMARY_DECORATIVE, Blocks.CHISELED_RED_SANDSTONE);
			registry.setBlock(STRUCTURE_PRIMARY_STAIRS, Blocks.RED_SANDSTONE_STAIRS);
			registry.setBlock(VILLAGE_PATH, Blocks.SAND);
		}
		registry.setBlock(STRUCTURE_SECONDARY, Blocks.STONE_BRICKS);
		registry.setBlock(STRUCTURE_SECONDARY_DECORATIVE, Blocks.CHISELED_STONE_BRICKS);
		registry.setBlock(STRUCTURE_SECONDARY_STAIRS, Blocks.STONE_BRICK_STAIRS);
		
		registry.setBlock(STRUCTURE_WOOD, Blocks.ACACIA_WOOD);
		registry.setBlock(STRUCTURE_LOG, Blocks.ACACIA_LOG);
		registry.setBlock(STRUCTURE_STRIPPED_WOOD, Blocks.STRIPPED_ACACIA_WOOD);
		registry.setBlock(STRUCTURE_STRIPPED_LOG, Blocks.STRIPPED_ACACIA_LOG);
		registry.setBlock(STRUCTURE_PLANKS, Blocks.ACACIA_PLANKS);
		registry.setBlock(STRUCTURE_PLANKS_STAIRS, Blocks.ACACIA_STAIRS);
		registry.setBlock(STRUCTURE_PLANKS_SLAB, Blocks.ACACIA_SLAB);
		registry.setBlock(STRUCTURE_PLANKS_FENCE, Blocks.ACACIA_FENCE);
		registry.setBlock(STRUCTURE_PLANKS_FENCE_GATE, Blocks.ACACIA_FENCE_GATE);
		registry.setBlock(STRUCTURE_PLANKS_DOOR, Blocks.ACACIA_DOOR);
		registry.setBlock(STRUCTURE_PLANKS_TRAPDOOR, Blocks.ACACIA_TRAPDOOR);
		
		registry.setBlock(STRUCTURE_WOOL_1, Blocks.WHITE_WOOL);
		registry.setBlock(STRUCTURE_WOOL_3, Blocks.MAGENTA_WOOL);
		
		registry.setBlock(TORCH, Blocks.REDSTONE_TORCH);
		registry.setBlock(WALL_TORCH, Blocks.REDSTONE_WALL_TORCH);
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = -0.6F;
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		BlockState sandstone = blocks.getBlockState(UPPER);
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		switch(this.type)
		{
			case SANDSTONE -> {
				builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.SAND_DISK, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.SANDSTONE_BLOCK_BLOB, LandBiomeType.NORMAL);
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.EXTRA_SANDSTONE_BLOCK_BLOB, LandBiomeType.ROUGH);
			}
			case RED_SANDSTONE -> {
				builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.RED_SAND_DISK, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.RED_SANDSTONE_BLOCK_BLOB, LandBiomeType.NORMAL);
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.EXTRA_RED_SANDSTONE_BLOCK_BLOB, LandBiomeType.ROUGH);
			}
		}
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), sandstone, 28),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.IRON_ORE.defaultBlockState(), 9),
						CountPlacement.of(48), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 8),
						CountPlacement.of(36), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		TurtleVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, RandomSource random)
	{
		TurtleVillagePieces.addPieces(register, random);
	}
	
	private enum Variant
	{
		SANDSTONE,
		RED_SANDSTONE
	}
}