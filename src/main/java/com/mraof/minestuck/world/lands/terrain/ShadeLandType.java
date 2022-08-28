package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ShadeLandType extends TerrainLandType
{
	public static final String SHADE = "minestuck.shade";
	
	private static final Vec3 skyColor = new Vec3(0.16D, 0.38D, 0.54D);
	
	public ShadeLandType()
	{
		super(new Builder(() -> MSEntityTypes.SALAMANDER).skylight(0F));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setGroundState(MSBlocks.SHADE_STONE.get().defaultBlockState(), MSFillerBlockTypes.SHADE_STONE);
		registry.setBlockState("upper", MSBlocks.BLUE_DIRT.get().defaultBlockState());
		registry.setBlockState("ocean", MSBlocks.OIL.get().defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_decorative", MSBlocks.CHISELED_SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_cracked", MSBlocks.CRACKED_SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_mossy", MSBlocks.MOSSY_SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_column", MSBlocks.SHADE_COLUMN.get().defaultBlockState());
		registry.setBlockState("structure_primary_stairs", MSBlocks.SHADE_STAIRS.get().defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.SMOOTH_SHADE_STONE.get().defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.TAR_SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.SHADE_BRICK_STAIRS.get().defaultBlockState());
		registry.setBlockState("village_path", Blocks.GRAVEL.defaultBlockState());
		registry.setBlockState("light_block", MSBlocks.GLOWING_WOOD.get().defaultBlockState());
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.defaultBlockState());
		registry.setBlockState("wall_torch", Blocks.REDSTONE_WALL_TORCH.defaultBlockState());
		registry.setBlockState("mushroom_1", MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState());
		registry.setBlockState("mushroom_2", MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState());
		registry.setBlockState("bush", MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.CYAN_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.defaultBlockState());
		registry.setBlockState("cruxite_ore", MSBlocks.SHADE_STONE_CRUXITE_ORE.get().defaultBlockState());
		registry.setBlockState("uranium_ore", MSBlocks.SHADE_STONE_URANIUM_ORE.get().defaultBlockState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {SHADE};
	}
	
	@Override
	public Biome.BiomeCategory getBiomeCategory()
	{
		return Biome.BiomeCategory.MUSHROOM;
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
	public Vec3 getFogColor()
	{
		return skyColor;
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addSalamanderVillageCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addSalamanderVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_SHADE;
	}
}
