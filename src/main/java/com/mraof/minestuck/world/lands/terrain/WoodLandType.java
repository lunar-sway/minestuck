package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.SalamanderVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.function.Consumer;

public class WoodLandType extends TerrainLandType
{
	public static final String WOOD = "minestuck.wood";
	public static final String PLANKS = "minestuck.planks";
	public static final String CARVINGS = "minestuck.carvings";
	
	public WoodLandType()
	{
		super(new Builder(MSEntityTypes.SALAMANDER).names(WOOD, PLANKS, CARVINGS)
				.skylight(1/2F).fogColor(0.0, 0.16, 0.38).skyColor(0.0, 0.3, 0.4)
				.music(MSSoundEvents.MUSIC_WOOD));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("ground", MSBlocks.UNCARVED_WOOD);
		registry.setBlock("upper", MSBlocks.UNCARVED_WOOD);
		registry.setBlock("surface", MSBlocks.UNCARVED_WOOD);
		registry.setBlock("ocean", MSBlocks.CAULK);
		registry.setBlock("village_path", MSBlocks.WOOD_SHAVINGS);
		registry.setBlock("structure_primary", MSBlocks.TREATED_HEAVY_PLANKS);
		registry.setBlock("structure_primary_decorative", MSBlocks.POLISHED_TREATED_UNCARVED_WOOD);
		registry.setBlock("structure_primary_cracked", MSBlocks.TREATED_CHIPBOARD);
		registry.setBlock("structure_primary_mossy", MSBlocks.TREATED_WOOD_SHAVINGS);
		registry.setBlock("structure_primary_column", MSBlocks.CARVED_LOG);
		registry.setBlock("structure_primary_stairs", MSBlocks.TREATED_HEAVY_PLANK_STAIRS);
		registry.setBlock("structure_secondary", MSBlocks.POLISHED_LACQUERED_UNCARVED_WOOD);
		registry.setBlock("structure_secondary_decorative", MSBlocks.TREATED_PLANKS);
		registry.setBlock("structure_secondary_stairs", MSBlocks.POLISHED_LACQUERED_UNCARVED_STAIRS);
		registry.setBlock("light_block", MSBlocks.WOODEN_LAMP);
		registry.setBlock("bush", MSBlocks.CARVED_BUSH);
		registry.setBlock("village_door", MSBlocks.CARVED_DOOR);
		registry.setBlock("structure_wool_1", Blocks.PURPLE_WOOL);
		registry.setBlock("structure_wool_3", Blocks.GREEN_WOOL);
		registry.setBlock("cruxite_ore", MSBlocks.UNCARVED_WOOD_CRUXITE_ORE);
		registry.setBlock("uranium_ore", MSBlocks.UNCARVED_WOOD_URANIUM_ORE);
	}
	
	@Override
	public void addStructureSets(Consumer<StructureSet> consumer, HolderGetter<Structure> structureLookup)
	{
		super.addStructureSets(consumer, structureLookup);
		consumer.accept(new StructureSet(structureLookup.getOrThrow(MSStructures.LARGE_WOOD_OBJECT), new RandomSpreadStructurePlacement(5, 3, RandomSpreadType.LINEAR, 17524013)));
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.WOODEN_GRASS_PATCH, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.TREATED_WOODEN_GRASS_PATCH, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.CARVED_BUSH_PATCH, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.CARVED_CHERRY_TREE, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.CARVED_HOUSE, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.CARVED_LOG, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.LARGE_CARVED_LOG, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.SPARSE_TREATED_CHAIR, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.SPARSE_TREATED_TABLE, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.UNFINISHED_CARVED_BOOKSHELF, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.SPARSE_UNFINISHED_CARVED_CHAIR, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.UNFINISHED_CARVED_CREEPER, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.SPARSE_UNFINISHED_CARVED_DRAWER, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.UNFINISHED_CARVED_SALAMANDER, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.SPARSE_UNFINISHED_CARVED_TABLE, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.UNFINISHED_CARVED_TREE, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.UNFINISHED_CARVED_TUNNEL, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.WOOD_SHAVINGS_PILE, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.WOODEN_CACTUS_PAIR, LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.TREATED_PLANKS_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.CARVED_PLANKS_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.CHIPBOARD_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.NORMAL, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.LACQUERED_WOODEN_MUSHROOM_PATCH, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.TREATED_CHAIR, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.TREATED_TABLE, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.UNFINISHED_CARVED_CHAIR, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.UNFINISHED_CARVED_DRAWER, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.UNFINISHED_CARVED_TABLE, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.MASSIVE_CHAIR, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.MASSIVE_STOOL, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.LACQUERED_PLANKS_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.TREATED_UNCARVED_WOOD_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.TREATED_CHIPBOARD_DISK,
				FeatureModifier.withTargets(BlockPredicate.matchesBlocks(blocks.getBlockState("surface").getBlock(), blocks.getBlockState("upper").getBlock())), LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.WOOD_SHAVINGS.get().defaultBlockState(), 33),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.CHIPBOARD.get().defaultBlockState(), 17),
						CountPlacement.of(27), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.UNCARVED_WOOD_REDSTONE_ORE.get().defaultBlockState(), 7),
						CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.UNCARVED_WOOD_IRON_ORE.get().defaultBlockState(), 9),
						CountPlacement.of(48), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.UNCARVED_WOOD_EMERALD_ORE.get().defaultBlockState(), 3),
						CountPlacement.of(29), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)), BiomeFilter.biome()),
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
