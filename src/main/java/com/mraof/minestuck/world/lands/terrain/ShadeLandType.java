package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.SalamanderVillagePieces;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import java.util.Random;

public class ShadeLandType extends TerrainLandType
{
	public static final String SHADE = "minestuck.shade";
	
	public ShadeLandType()
	{
		super(new Builder(() -> MSEntityTypes.SALAMANDER).names(SHADE)
				.skylight(0F).fogColor(0.16, 0.38, 0.54)
				.category(Biome.BiomeCategory.MUSHROOM).music(() -> MSSoundEvents.MUSIC_SHADE));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("ground", MSBlocks.SHADE_STONE);
		registry.setBlock("upper", MSBlocks.BLUE_DIRT);
		registry.setBlock("ocean", MSBlocks.OIL);
		registry.setBlock("structure_primary", MSBlocks.SHADE_BRICKS);
		registry.setBlock("structure_primary_decorative", MSBlocks.CHISELED_SHADE_BRICKS);
		registry.setBlock("structure_primary_cracked", MSBlocks.CRACKED_SHADE_BRICKS);
		registry.setBlock("structure_primary_mossy", MSBlocks.MOSSY_SHADE_BRICKS);
		registry.setBlock("structure_primary_column", MSBlocks.SHADE_COLUMN);
		registry.setBlock("structure_primary_stairs", MSBlocks.SHADE_STAIRS);
		registry.setBlock("structure_secondary", MSBlocks.SMOOTH_SHADE_STONE);
		registry.setBlock("structure_secondary_decorative", MSBlocks.TAR_SHADE_BRICKS);
		registry.setBlock("structure_secondary_stairs", MSBlocks.SHADE_BRICK_STAIRS);
		registry.setBlock("village_path", Blocks.GRAVEL);
		registry.setBlock("light_block", MSBlocks.GLOWING_WOOD);
		registry.setBlock("torch", Blocks.REDSTONE_TORCH);
		registry.setBlock("wall_torch", Blocks.REDSTONE_WALL_TORCH);
		registry.setBlock("mushroom_1", MSBlocks.GLOWING_MUSHROOM);
		registry.setBlock("mushroom_2", MSBlocks.GLOWING_MUSHROOM);
		registry.setBlock("bush", MSBlocks.GLOWING_MUSHROOM);
		registry.setBlock("structure_wool_1", Blocks.CYAN_WOOL);
		registry.setBlock("structure_wool_3", Blocks.GRAY_WOOL);
		registry.setBlock("cruxite_ore", MSBlocks.SHADE_STONE_CRUXITE_ORE);
		registry.setBlock("uranium_ore", MSBlocks.SHADE_STONE_URANIUM_ORE);
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.forceRain = LandProperties.ForceType.DEFAULT;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.GLOWING_MUSHROOM_PATCH.getHolder().orElseThrow());
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.GLOWING_TREE.getHolder().orElseThrow());
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_GLOWING_MUSHROOM_PATCH.getHolder().orElseThrow());
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.GLOWING_TREE.getHolder().orElseThrow());
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.BLUE_DIRT.get().defaultBlockState(), 25),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.SHADE_STONE_COAL_ORE.get().defaultBlockState(), 9),
				CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.SHADE_STONE_GOLD_ORE.get().defaultBlockState(), 7),
				CountPlacement.of(6), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.SHADE_STONE_CRUXITE_ORE.get().defaultBlockState(), 4),
				CountPlacement.of(12), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.SHADE_STONE_URANIUM_ORE.get().defaultBlockState(), 2),
				CountPlacement.of(4), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		SalamanderVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		SalamanderVillagePieces.addPieces(register, random);
	}
}
