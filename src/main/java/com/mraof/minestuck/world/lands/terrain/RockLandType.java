package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.BiomeType;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.village.ConsortVillageCenter;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.*;

import java.util.Random;

public class RockLandType extends TerrainLandType
{
	public static final String ROCK = "minestuck.rock";
	public static final String STONE = "minestuck.stone";
	public static final String ORE = "minestuck.ore";
	public static final String PETRIFICATION = "minestuck.petrification";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "rock");
	private final Variant type;
	
	public RockLandType(Variant variation)
	{
		super(GROUP_NAME);
		type = variation;
	}

	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.PETRIFICATION) {
			registry.setBlockState("surface", Blocks.STONE.getDefaultState());
		} else {
			registry.setBlockState("surface", Blocks.GRAVEL.getDefaultState());
		}
		registry.setBlockState("upper", Blocks.COBBLESTONE.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MSBlocks.COARSE_STONE.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.CHISELED_COARSE_STONE.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.COARSE_STONE_STAIRS.getDefaultState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.getDefaultState());
		registry.setBlockState("village_path", Blocks.MOSSY_COBBLESTONE.getDefaultState());
		registry.setBlockState("village_fence", Blocks.COBBLESTONE_WALL.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.BROWN_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.getDefaultState());
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.PETRIFICATION) {
			return new String[] {PETRIFICATION};
		} else {
			return new String[] {ROCK, STONE, ORE};
		}
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.EXTREME_HILLS;
		properties.downfall = 0.2F;
		properties.temperature = 0.3F;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 1/4F;
	}
	
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type == BiomeType.OCEAN)
		{
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(Blocks.CLAY.getDefaultState(), 6, 2, Lists.newArrayList(blocks.getBlockState("ocean_surface"), Blocks.CLAY.getDefaultState()))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(25))));
		}
		
		if(biome.type == BiomeType.NORMAL)
		{
			if(type == Variant.ROCK)
			{
				biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BLOCK_BLOB.withConfiguration(new BlockBlobConfig(Blocks.COBBLESTONE.getDefaultState(), 0)).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(3, 0.1F, 2))));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.PETRIFIED_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(32).build()).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(4))));
			} else if(type == Variant.PETRIFICATION)
			{
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE.withConfiguration(new BlockStateFeatureConfig(MSBlocks.PETRIFIED_LOG.getDefaultState())).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(10))));
				biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BLOCK_BLOB.withConfiguration(new BlockBlobConfig(Blocks.COBBLESTONE.getDefaultState(), 0)).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure( new AtSurfaceWithExtraConfig(1, 0.1F, 1))));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.PETRIFIED_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(32).build()).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.PETRIFIED_POPPY.getDefaultState()), new SimpleBlockPlacer())).tries(32).build()).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(8))));
			}
		}
		
		if(biome.type == BiomeType.ROUGH)
		{
			if(type == Variant.ROCK)
			{
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE.withConfiguration(new BlockStateFeatureConfig(MSBlocks.PETRIFIED_LOG.getDefaultState())).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(20))));
				biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BLOCK_BLOB.withConfiguration(new BlockBlobConfig(Blocks.COBBLESTONE.getDefaultState(), 1)).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(4, 0.1F, 1))));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.PETRIFIED_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(32).build()).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(10))));
			} else if(type == Variant.PETRIFICATION)
			{
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE.withConfiguration(new BlockStateFeatureConfig(MSBlocks.PETRIFIED_LOG.getDefaultState())).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.5F, 1))));
				biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BLOCK_BLOB.withConfiguration(new BlockBlobConfig(Blocks.COBBLESTONE.getDefaultState(), 1)).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.PETRIFIED_GRASS.getDefaultState()), new SimpleBlockPlacer())).tries(32).build()).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MSBlocks.PETRIFIED_POPPY.getDefaultState()), new SimpleBlockPlacer())).tries(32).build()).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(16))));
			}
		}
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.getDefaultState(), 33)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 256))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.INFESTED_STONE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(7, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.COAL_ORE.getDefaultState(), 17)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 128))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.IRON_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.REDSTONE_ORE.getDefaultState(), 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 32))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.LAPIS_ORE.getDefaultState(), 7)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 24))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.GOLD_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 32))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIAMOND_ORE.getDefaultState(), 6)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 24))));
	}
	
	@Override
	public float getSkylightBase()
	{
		return 7/8F;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return new Vec3d(0.5, 0.5, 0.55);
	}
	
	@Override
	public Vec3d getSkyColor()
	{
		return new Vec3d(0.6D, 0.6D, 0.7D);
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
		
		register.add(ConsortVillageCenter.RockCenter::new, 5);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addNakagatorVillagePieces(register, random);
	}
	
	public enum Variant
	{
		ROCK,
		PETRIFICATION;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}