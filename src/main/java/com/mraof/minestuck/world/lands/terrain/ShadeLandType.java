package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.SalamanderVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandProperties;
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

public class ShadeLandType extends TerrainLandType
{
	public static final String SHADE = "minestuck.shade";
	
	public ShadeLandType()
	{
		super(new Builder(MSEntityTypes.SALAMANDER).names(SHADE)
				.skylight(0F).fogColor(0.16, 0.38, 0.54)
				.music(MSSoundEvents.MUSIC_SHADE));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock(CRUXITE_ORE, MSBlocks.SHADE_STONE_CRUXITE_ORE);
		registry.setBlock(URANIUM_ORE, MSBlocks.SHADE_STONE_URANIUM_ORE);
		registry.setBlock(GROUND, MSBlocks.SHADE_STONE);
		registry.setBlock(UPPER, MSBlocks.BLUE_DIRT);
		registry.setBlock(OCEAN, MSBlocks.OIL);
		
		registry.setBlock(STRUCTURE_PRIMARY, MSBlocks.SHADE_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_DECORATIVE, MSBlocks.CHISELED_SHADE_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_CRACKED, MSBlocks.CRACKED_SHADE_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_COLUMN, MSBlocks.SHADE_COLUMN);
		registry.setBlock(STRUCTURE_PRIMARY_STAIRS, MSBlocks.SHADE_BRICK_STAIRS);
		registry.setBlock(STRUCTURE_PRIMARY_SLAB, MSBlocks.SHADE_BRICK_SLAB);
		registry.setBlock(STRUCTURE_PRIMARY_WALL, MSBlocks.SHADE_BRICK_WALL);
		
		//TODO was originally mossy shade brick, but chose tar for lore relevance. Was this a good trade?
		registry.setBlock(STRUCTURE_PRIMARY_MOSSY, MSBlocks.TAR_SHADE_BRICKS);
		registry.setBlock(STRUCTURE_PRIMARY_MOSSY_STAIRS, MSBlocks.TAR_SHADE_BRICK_STAIRS);
		registry.setBlock(STRUCTURE_PRIMARY_MOSSY_SLAB, MSBlocks.TAR_SHADE_BRICK_SLAB);
		registry.setBlock(STRUCTURE_PRIMARY_MOSSY_WALL, MSBlocks.TAR_SHADE_BRICK_WALL);
		
		registry.setBlock(STRUCTURE_SECONDARY, MSBlocks.SMOOTH_SHADE_STONE);
		registry.setBlock(STRUCTURE_SECONDARY_DECORATIVE, MSBlocks.MOSSY_SHADE_BRICKS);
		registry.setBlock(STRUCTURE_SECONDARY_STAIRS, MSBlocks.SMOOTH_SHADE_STONE_STAIRS);
		registry.setBlock(STRUCTURE_SECONDARY_SLAB, MSBlocks.SMOOTH_SHADE_STONE_SLAB);
		registry.setBlock(STRUCTURE_SECONDARY_WALL, MSBlocks.SMOOTH_SHADE_STONE_WALL);
		
		registry.setBlock(STRUCTURE_WOOD, MSBlocks.SHADEWOOD);
		registry.setBlock(STRUCTURE_LOG, MSBlocks.SHADEWOOD_LOG);
		registry.setBlock(STRUCTURE_STRIPPED_WOOD, MSBlocks.STRIPPED_SHADEWOOD);
		registry.setBlock(STRUCTURE_STRIPPED_LOG, MSBlocks.STRIPPED_SHADEWOOD_LOG);
		registry.setBlock(STRUCTURE_PLANKS, MSBlocks.SHADEWOOD_PLANKS);
		//TODO registry.setBlock(STRUCTURE_BOOKSHELF, MSBlocks.SHADEWOOD_BOOKSHELF);
		registry.setBlock(STRUCTURE_PLANKS_STAIRS, MSBlocks.SHADEWOOD_STAIRS);
		registry.setBlock(STRUCTURE_PLANKS_SLAB, MSBlocks.SHADEWOOD_SLAB);
		registry.setBlock(STRUCTURE_PLANKS_FENCE, MSBlocks.SHADEWOOD_FENCE);
		registry.setBlock(STRUCTURE_PLANKS_FENCE_GATE, MSBlocks.SHADEWOOD_FENCE_GATE);
		registry.setBlock(STRUCTURE_PLANKS_DOOR, MSBlocks.SHADEWOOD_DOOR);
		registry.setBlock(STRUCTURE_PLANKS_TRAPDOOR, MSBlocks.SHADEWOOD_TRAPDOOR);
		
		registry.setBlock(STRUCTURE_WOOL_1, Blocks.CYAN_WOOL);
		registry.setBlock(STRUCTURE_WOOL_3, Blocks.GRAY_WOOL);
		
		registry.setBlock(STRUCTURE_GROUND_COVER, MSBlocks.GLOWING_MUSHROOM);
		registry.setBlock(STRUCTURE_ROOF_COVER, MSBlocks.GLOWING_MUSHROOM_VINES);
		
		registry.setBlock(VILLAGE_PATH, Blocks.GRAVEL);
		
		registry.setBlock(LIGHT_BLOCK, MSBlocks.GLOWING_WOOD);
		registry.setBlock(TORCH, Blocks.REDSTONE_TORCH);
		registry.setBlock(WALL_TORCH, Blocks.REDSTONE_WALL_TORCH);
		registry.setBlock(MUSHROOM_1, MSBlocks.GLOWING_MUSHROOM);
		registry.setBlock(MUSHROOM_2, MSBlocks.GLOWING_MUSHROOM);
		registry.setBlock(BUSH, MSBlocks.GLOWING_MUSHROOM);
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.forceRain = LandProperties.ForceType.DEFAULT;
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.SHADE_STONE_BLOCK_BLOB, LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_GLOWING_MUSHROOM_PATCH, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.GLOWING_TREE, LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.GLOWING_MUSHROOM_PATCH, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.GLOWING_TREE, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SHADEWOOD_TREE, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SCARRED_SHADEWOOD_TREE, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.ORNATE_SHADEWOOD_TREE, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.OIL_POOL, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.BLUE_DIRT.get().defaultBlockState(), 25),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.SHADE_STONE_COAL_ORE.get().defaultBlockState(), 9),
						CountPlacement.of(48), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.SHADE_STONE_GOLD_ORE.get().defaultBlockState(), 7),
						CountPlacement.of(18), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.SHADE_STONE_CRUXITE_ORE.get().defaultBlockState(), 4),
						CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.SHADE_STONE_URANIUM_ORE.get().defaultBlockState(), 2),
						CountPlacement.of(12), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		SalamanderVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, RandomSource random)
	{
		SalamanderVillagePieces.addPieces(register, random);
	}
}
