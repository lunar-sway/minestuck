package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.FeatureModifier;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.gate.GateMushroomPiece;
import com.mraof.minestuck.world.gen.structure.village.SalamanderVillagePieces;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
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

public class FungiLandType extends TerrainLandType
{
	public static final String FUNGI = "minestuck.fungi";
	public static final String DANK = "minestuck.dank";
	public static final String MUST = "minestuck.must";
	public static final String MYCELIUM = "minestuck.mycelium";
	public static final String MOLD = "minestuck.mold";
	public static final String MILDEW = "minestuck.mildew";
	
	public FungiLandType()
	{
		super(new Builder(MSEntityTypes.SALAMANDER).names(FUNGI, DANK, MUST, MOLD, MILDEW, MYCELIUM)
				.fogColor(0.69, 0.76, 0.61).skyColor(0.69, 0.76, 0.61)
				.music(MSSoundEvents.MUSIC_FUNGI));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("ground", MSBlocks.MYCELIUM_STONE);
		registry.setBlock("surface", Blocks.MYCELIUM);
		registry.setBlock("upper", Blocks.DIRT);
		registry.setBlock("ocean", Blocks.WATER);
		registry.setBlock("structure_primary", MSBlocks.MYCELIUM_BRICKS);
		registry.setBlock("structure_primary_decorative", MSBlocks.CHISELED_MYCELIUM_BRICKS);
		registry.setBlock("structure_primary_cracked", MSBlocks.CRACKED_MYCELIUM_BRICKS);
		registry.setBlock("structure_primary_mossy", MSBlocks.MOSSY_MYCELIUM_BRICKS);
		registry.setBlock("structure_primary_column", MSBlocks.MYCELIUM_COLUMN);
		registry.setBlock("structure_primary_stairs", MSBlocks.MYCELIUM_BRICK_STAIRS);
		registry.setBlock("structure_secondary", MSBlocks.POLISHED_MYCELIUM_STONE);
		registry.setBlock("structure_secondary_decorative", MSBlocks.MYCELIUM_COBBLESTONE);
		registry.setBlock("structure_secondary_stairs", MSBlocks.MYCELIUM_STAIRS);
		registry.setBlock("village_path", Blocks.DIRT_PATH);
		registry.setBlock("light_block", MSBlocks.GLOWY_GOOP);
		registry.setBlock("torch", Blocks.REDSTONE_TORCH);
		registry.setBlock("wall_torch", Blocks.REDSTONE_WALL_TORCH);
		registry.setBlock("mushroom_1", Blocks.RED_MUSHROOM);
		registry.setBlock("mushroom_2", Blocks.BROWN_MUSHROOM);
		registry.setBlock("bush", Blocks.BROWN_MUSHROOM);
		registry.setBlock("structure_wool_1", Blocks.LIME_WOOL);
		registry.setBlock("structure_wool_3", Blocks.GRAY_WOOL);
		registry.setBlock("cruxite_ore", MSBlocks.MYCELIUM_STONE_CRUXITE_ORE);
		registry.setBlock("uranium_ore", MSBlocks.MYCELIUM_STONE_URANIUM_ORE);
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.forceRain = LandProperties.ForceType.ON;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.setGatePiece(GateMushroomPiece::new);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.MUSHROOM_ISLAND_VEGETATION, LandBiomeType.NORMAL);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.SLIME_DISK, FeatureModifier.withState(blocks.getBlockState("slime")), LandBiomeType.NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.HUGE_MUSHROOMS, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.EXTRA_SLIME_DISK, FeatureModifier.withState(blocks.getBlockState("slime")), LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BROWN_MUSHROOM_PATCH, LandBiomeType.any());
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.RED_MUSHROOM_PATCH, LandBiomeType.any());
		
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.CRIMSON_FUNGUS_PATCH, LandBiomeType.ROUGH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.WARPED_FUNGUS_PATCH, LandBiomeType.ROUGH);
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MSPlacedFeatures.inline(Feature.ORE,
						new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
						CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256)), BiomeFilter.biome()),
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