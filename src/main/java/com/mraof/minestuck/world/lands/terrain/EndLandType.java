package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class EndLandType extends TerrainLandType
{
	public static final String END = "minestuck.end";
	public static final String DIMENSION = "minestuck.dimension";
	
	private static final Vec3 fogColor = new Vec3(0.0D, 0.4D, 0.2D);
	private static final Vec3 skyColor = new Vec3(0.3D, 0.1D, 0.5D);
	
	public EndLandType()
	{
		super(new Builder(() -> MSEntityTypes.NAKAGATOR).biomeSet(MSBiomes.NO_RAIN_LAND));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setGroundState(MSBlocks.COARSE_END_STONE.get().defaultBlockState(), MSFillerBlockTypes.COARSE_END_STONE);
		registry.setBlockState("surface", Blocks.END_STONE.defaultBlockState());
		registry.setBlockState("surface_rough", MSBlocks.END_GRASS.get().defaultBlockState());
		registry.setBlockState("upper", Blocks.END_STONE.defaultBlockState());
		registry.setBlockState("ocean", MSBlocks.ENDER.get().defaultBlockState());
		registry.setBlockState("structure_primary", Blocks.END_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.PURPUR_PILLAR.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
		registry.setBlockState("structure_primary_stairs", Blocks.END_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary", Blocks.PURPUR_BLOCK.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.PURPUR_STAIRS.defaultBlockState());
		registry.setBlockState("structure_planks", Blocks.BRICKS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.defaultBlockState());
		registry.setBlockState("village_path", MSBlocks.COARSE_END_STONE.get().defaultBlockState());
		registry.setBlockState("village_fence", Blocks.NETHER_BRICK_FENCE.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.GREEN_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.PURPLE_WOOL.defaultBlockState());
		registry.setBlockState("cruxite_ore", MSBlocks.END_STONE_CRUXITE_ORE.get().defaultBlockState());
		registry.setBlockState("uranium_ore", MSBlocks.END_STONE_URANIUM_ORE.get().defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{END, DIMENSION};
	}
	
	@Override
	public Biome.BiomeCategory getBiomeCategory()
	{
		return Biome.BiomeCategory.THEEND;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type != LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlacementUtils.inlinePlaced(MSCFeatures.END_TREE.getHolder().orElseThrow(),
					PlacementUtils.countExtra(2, 0.1F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
		}
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
					new DiskConfiguration(MSBlocks.END_GRASS.get().defaultBlockState(), UniformInt.of(2, 4), 1, List.of(blocks.getBlockState("surface"), MSBlocks.END_GRASS.get().defaultBlockState())),
					InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.TALL_END_GRASS_PATCH.getHolder().orElseThrow());
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
					new DiskConfiguration(Blocks.END_STONE.defaultBlockState(), UniformInt.of(2, 3), 1, List.of(blocks.getBlockState("surface_rough"), Blocks.END_STONE.defaultBlockState())),
					InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.TALL_END_GRASS_PATCH.getHolder().orElseThrow());
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.END_STONE_IRON_ORE.get().defaultBlockState(), 9),
				CountPlacement.of(20), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.END_STONE_REDSTONE_ORE.get().defaultBlockState(), 8),
				CountPlacement.of(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.END_STONE.defaultBlockState(), 36),
				CountPlacement.of(40), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
	}
	
	@Override
	public Vec3 getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public Vec3 getSkyColor()
	{
		return skyColor;
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addNakagatorVillageCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addNakagatorVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_END;
	}
}