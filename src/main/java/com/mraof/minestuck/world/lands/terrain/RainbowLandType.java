package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.MSSurfaceRules;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.TurtleVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import java.util.stream.Stream;

import static com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry.*;

public class RainbowLandType extends TerrainLandType
{
	public static final String RAINBOW = "minestuck.rainbow";
	public static final String COLORS = "minestuck.colors";
	
	public RainbowLandType()
	{
		super(new Builder(MSEntityTypes.TURTLE).names(RAINBOW, COLORS)
				.fogColor(0.0, 0.6, 0.8).skyColor(0.9, 0.6, 0.8)
				.music(MSSoundEvents.MUSIC_RAINBOW));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock(UPPER, Blocks.WHITE_TERRACOTTA);
		registry.setBlock(SURFACE, Blocks.WHITE_WOOL);
		registry.setBlock(OCEAN, MSBlocks.WATER_COLORS);
		
		registry.setBlock(STRUCTURE_PRIMARY, MSBlocks.RAINBOW_PLANKS);
		registry.setBlock(STRUCTURE_PRIMARY_DECORATIVE, MSBlocks.RAINBOW_WOOD);
		registry.setBlock(STRUCTURE_PRIMARY_STAIRS, MSBlocks.RAINBOW_STAIRS);
		registry.setBlock(STRUCTURE_PRIMARY_SLAB, MSBlocks.RAINBOW_SLAB);
		registry.setBlock(STRUCTURE_PRIMARY_WALL, MSBlocks.RAINBOW_FENCE);
		
		registry.setBlock(STRUCTURE_SECONDARY, MSBlocks.GLOWING_PLANKS);
		registry.setBlock(STRUCTURE_SECONDARY_DECORATIVE, MSBlocks.GLOWING_WOOD);
		registry.setBlock(STRUCTURE_SECONDARY_STAIRS, MSBlocks.GLOWING_STAIRS);
		registry.setBlock(STRUCTURE_SECONDARY_SLAB, MSBlocks.GLOWING_SLAB);
		registry.setBlock(STRUCTURE_SECONDARY_WALL, MSBlocks.GLOWING_FENCE);
		
		registry.setBlock(STRUCTURE_WOOD, MSBlocks.RAINBOW_WOOD);
		registry.setBlock(STRUCTURE_LOG, MSBlocks.RAINBOW_LOG);
		registry.setBlock(STRUCTURE_STRIPPED_WOOD, MSBlocks.STRIPPED_RAINBOW_WOOD);
		registry.setBlock(STRUCTURE_STRIPPED_LOG, MSBlocks.STRIPPED_RAINBOW_LOG);
		registry.setBlock(STRUCTURE_PLANKS, MSBlocks.RAINBOW_PLANKS);
		registry.setBlock(STRUCTURE_BOOKSHELF, MSBlocks.RAINBOW_BOOKSHELF);
		registry.setBlock(STRUCTURE_PLANKS_STAIRS, MSBlocks.RAINBOW_STAIRS);
		registry.setBlock(STRUCTURE_PLANKS_SLAB, MSBlocks.RAINBOW_SLAB);
		registry.setBlock(STRUCTURE_PLANKS_FENCE, MSBlocks.RAINBOW_FENCE);
		registry.setBlock(STRUCTURE_PLANKS_FENCE_GATE, MSBlocks.RAINBOW_FENCE_GATE);
		registry.setBlock(STRUCTURE_PLANKS_DOOR, MSBlocks.RAINBOW_DOOR);
		registry.setBlock(STRUCTURE_PLANKS_TRAPDOOR, MSBlocks.RAINBOW_TRAPDOOR);
		
		registry.setBlock(STRUCTURE_WOOL_1, Blocks.YELLOW_WOOL);
		registry.setBlock(STRUCTURE_WOOL_3, Blocks.GREEN_WOOL);
		
		registry.setBlock(SALAMANDER_FLOOR, Blocks.STONE_BRICKS);
		
		registry.setBlock(LIGHT_BLOCK, MSBlocks.GLOWING_WOOD);
		BlockState rainbow_leaves = MSBlocks.RAINBOW_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
		registry.setBlockState(BUSH, rainbow_leaves);
		registry.setBlockState(MUSHROOM_1, rainbow_leaves);
		registry.setBlockState(MUSHROOM_2, rainbow_leaves);
	}
	
	@Override
	public void setSpawnInfo(MobSpawnSettings.Builder builder, LandBiomeType type)
	{
		builder.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 2, 3, 5));
	}
	
	@Override
	public SurfaceRules.RuleSource getSurfaceRule(StructureBlockRegistry blocks)
	{
		SurfaceRules.RuleSource wool = new MSSurfaceRules.CheckeredRuleSource(1,
				Stream.of(Blocks.RED_WOOL, Blocks.ORANGE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL,
								Blocks.LIGHT_BLUE_WOOL, Blocks.BLUE_WOOL, Blocks.PURPLE_WOOL, Blocks.MAGENTA_WOOL)
						.map(Block::defaultBlockState).map(SurfaceRules::state).toList());
		SurfaceRules.RuleSource terracotta = new MSSurfaceRules.CheckeredRuleSource(1,
				Stream.of(Blocks.RED_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA,
								Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA)
						.map(Block::defaultBlockState).map(SurfaceRules::state).toList());
		
		SurfaceRules.RuleSource surface = SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(0, 0), wool));
		SurfaceRules.RuleSource upper = SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, terracotta);
		
		return SurfaceRules.sequence(surface, upper);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.EXTRA_RAINBOW_TREE, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.RAINBOW_TREE, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.MESA, LandBiomeType.any());
		
		//Each of these is associated with one of the primary colors in Minecraft: black, red, blue, yellow, green, brown, and white
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17),
						CountPlacement.of(30), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 8),
						CountPlacement.of(30), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 7),
						CountPlacement.of(15), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.GOLD_ORE.defaultBlockState(), 9),
						CountPlacement.of(12), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.EMERALD_ORE.defaultBlockState(), 8),
						CountPlacement.of(30), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 24),
						CountPlacement.of(6), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.DIORITE.defaultBlockState(), 8),
						CountPlacement.of(30), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
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
}
