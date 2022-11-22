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
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
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

public class HeatLandType extends TerrainLandType
{
	public static final String HEAT = "minestuck.heat";
	public static final String FLAME = "minestuck.flame";
	public static final String FIRE = "minestuck.fire";
	
	public HeatLandType()
	{
		super(new Builder(MSEntityTypes.NAKAGATOR).names(HEAT, FLAME, FIRE)
				.skylight(1/2F).fogColor(0.4, 0.0, 0.0)
				.biomeSet(MSBiomes.NO_RAIN_LAND).category(Biome.BiomeCategory.NETHER).music(MSSoundEvents.MUSIC_HEAT));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("ground", Blocks.NETHERRACK);
		registry.setBlock("upper", Blocks.NETHERRACK);
		registry.setBlock("ocean", Blocks.LAVA);
		registry.setBlock("structure_primary", MSBlocks.BLACK_STONE_BRICKS);
		registry.setBlock("structure_primary_decorative", MSBlocks.CHISELED_BLACK_STONE_BRICKS);
		registry.setBlock("structure_primary_cracked", MSBlocks.CRACKED_BLACK_STONE_BRICKS);
		registry.setBlock("structure_primary_column", MSBlocks.BLACK_STONE_COLUMN);
		registry.setBlock("structure_primary_stairs", MSBlocks.BLACK_STONE_BRICK_STAIRS);
		registry.setBlock("structure_secondary", MSBlocks.CAST_IRON);
		registry.setBlock("structure_secondary_decorative", MSBlocks.CHISELED_CAST_IRON);
		registry.setBlock("structure_secondary_stairs", MSBlocks.CAST_IRON_STAIRS);
		registry.setBlock("fall_fluid", Blocks.WATER);
		registry.setBlock("structure_planks", Blocks.BRICKS);
		registry.setBlock("structure_planks_slab", Blocks.BRICK_SLAB);
		registry.setBlock("village_path", Blocks.QUARTZ_BLOCK);
		registry.setBlock("village_fence", Blocks.NETHER_BRICK_FENCE);
		registry.setBlock("structure_wool_1", Blocks.YELLOW_WOOL);
		registry.setBlock("structure_wool_3", Blocks.PURPLE_WOOL);
		registry.setBlock("cruxite_ore", MSBlocks.NETHERRACK_CRUXITE_ORE);
		registry.setBlock("uranium_ore", MSBlocks.NETHERRACK_URANIUM_ORE);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MSPlacedFeatures.OCEAN_RUNDOWN, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.FIRE_FIELD, LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.EXTRA_FIRE_FIELD, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.BLACK_STONE.get().defaultBlockState(), 33),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), MSBlocks.NETHERRACK_COAL_ORE.get().defaultBlockState(), 17),
						CountPlacement.of(39), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128)), BiomeFilter.biome()),
				LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.NETHER_QUARTZ_ORE.defaultBlockState(), 8),
						CountPlacement.of(26), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), BiomeFilter.biome()),
				LandBiomeType.any());
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		NakagatorVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		NakagatorVillagePieces.addPieces(register, random);
	}
}