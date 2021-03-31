package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.*;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.tree.EndTree;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.*;

import java.util.Random;

public class EndLandType extends TerrainLandType
{
	public static final String END = "minestuck.end";
	public static final String DIMENSION = "minestuck.dimension";
	
	private static final Vec3d fogColor = new Vec3d(0.0D, 0.4D, 0.2D);
	private static final Vec3d skyColor = new Vec3d(0.3D, 0.1D, 0.5D);
	
	public EndLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setGroundState(MSBlocks.COARSE_END_STONE.getDefaultState(), MSFillerBlockTypes.COARSE_END_STONE);
		registry.setBlockState("surface", Blocks.END_STONE.getDefaultState());
		registry.setBlockState("surface_rough", MSBlocks.END_GRASS.getDefaultState());
		registry.setBlockState("upper", Blocks.END_STONE.getDefaultState());
		registry.setBlockState("ocean", MSBlocks.ENDER.getDefaultState());
		registry.setBlockState("structure_primary", Blocks.END_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.PURPUR_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
		registry.setBlockState("structure_primary_stairs", Blocks.END_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary", Blocks.PURPUR_BLOCK.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.PURPUR_STAIRS.getDefaultState());
		registry.setBlockState("structure_planks", Blocks.BRICKS.getDefaultState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.getDefaultState());
		registry.setBlockState("village_path", MSBlocks.COARSE_END_STONE.getDefaultState());
		registry.setBlockState("village_fence", Blocks.NETHER_BRICK_FENCE.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.GREEN_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.PURPLE_WOOL.getDefaultState());
		registry.setBlockState("cruxite_ore", MSBlocks.END_STONE_CRUXITE_ORE.getDefaultState());
		registry.setBlockState("uranium_ore", MSBlocks.END_STONE_URANIUM_ORE.getDefaultState());
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{END, DIMENSION};
	}
	
	@Override
	public LandBiomeSet getBiomeSet()
	{
		return MSBiomes.NO_RAIN_LAND;
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.THEEND;
	}
	
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type != BiomeType.OCEAN)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.END_TREE.withConfiguration(EndTree.getDefaultConfig().build()).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
		}
		
		if(biome.type == BiomeType.NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.GRASSY_SURFACE_DISK.withConfiguration(new SphereReplaceConfig(MSBlocks.END_GRASS.getDefaultState(), 5, 1, Lists.newArrayList(blocks.getBlockState("surface"), MSBlocks.END_GRASS.getDefaultState()))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(MinestuckBiomeFeatures.TALL_END_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(8))));
		} else if(biome.type == BiomeType.ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.GRASSY_SURFACE_DISK.withConfiguration(new SphereReplaceConfig(Blocks.END_STONE.getDefaultState(), 4, 1, Lists.newArrayList(blocks.getBlockState("surface_rough"), Blocks.END_STONE.getDefaultState()))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.CHORUS_PLANT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHORUS_PLANT.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(MinestuckBiomeFeatures.TALL_END_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(10))));
		}
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.END_STONE_IRON_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.END_STONE_REDSTONE_ORE.getDefaultState(), 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 32))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.END_STONE.getDefaultState(), 36)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(40, 0, 0, 64))));
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public Vec3d getSkyColor()
	{
		return skyColor;
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return MSEntityTypes.NAKAGATOR;
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