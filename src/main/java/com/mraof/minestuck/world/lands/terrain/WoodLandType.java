package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.SalamanderVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import java.util.List;
import java.util.Random;

public class WoodLandType extends TerrainLandType
{
	public static final String WOOD = "minestuck.wood";
	public static final String OAK = "minestuck.oak";
	public static final String LUMBER = "minestuck.lumber";
	
	public WoodLandType()
	{
		super(new Builder(MSEntityTypes.SALAMANDER).names(WOOD, OAK, LUMBER)
				.skylight(1/2F).fogColor(0.0, 0.16, 0.38).skyColor(0.0, 0.3, 0.4)
				.music(MSSoundEvents.MUSIC_WOOD));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("upper", Blocks.OAK_LOG);
		registry.setBlock("surface", MSBlocks.TREATED_PLANKS);
		registry.setBlock("structure_primary", Blocks.JUNGLE_WOOD);
		registry.setBlock("structure_primary_decorative", Blocks.DARK_OAK_LOG);
		registry.setBlock("structure_primary_stairs", Blocks.DARK_OAK_STAIRS);
		registry.setBlock("structure_secondary", Blocks.JUNGLE_PLANKS);
		registry.setBlock("structure_secondary_decorative", Blocks.DARK_OAK_PLANKS);
		registry.setBlock("structure_secondary_stairs", Blocks.JUNGLE_STAIRS);
		registry.setBlock("light_block", MSBlocks.GLOWING_WOOD);
		registry.setBlock("bush", Blocks.RED_MUSHROOM);
		registry.setBlock("structure_wool_1", Blocks.PURPLE_WOOL);
		registry.setBlock("structure_wool_3", Blocks.GREEN_WOOL);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.RED_MUSHROOM_PATCH, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BROWN_MUSHROOM_PATCH, LandBiomeType.NORMAL);
		
		builder.addModified(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.NETHERRACK_DISK,
				FeatureModifier.withTargets(List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))), LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.RED_MUSHROOM_PATCH, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BROWN_MUSHROOM_PATCH, LandBiomeType.ROUGH);
		
		builder.addModified(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.OAK_LEAVES_DISK,
				FeatureModifier.withTargets(List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))), LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 17),
						CountPlacement.of(27), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 7),
						CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.IRON_ORE.defaultBlockState(), 9),
						CountPlacement.of(48), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.EMERALD_ORE.defaultBlockState(), 3),
						CountPlacement.of(29), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(24)), BiomeFilter.biome()),
				LandBiomeType.any());
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
