package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MinestuckBiomeFeatures;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.RandomRockBlockBlobConfig;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.village.ConsortVillageCenter;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;

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
		if(type == Variant.PETRIFICATION)
		{
			registry.setBlockState("surface", Blocks.STONE.defaultBlockState());
		} else
		{
			registry.setBlockState("surface", Blocks.GRAVEL.defaultBlockState());
		}
		
		registry.setBlockState("upper", Blocks.COBBLESTONE.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.COARSE_STONE.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.CHISELED_COARSE_STONE.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.COARSE_STONE_STAIRS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.defaultBlockState());
		registry.setBlockState("village_path", Blocks.MOSSY_COBBLESTONE.defaultBlockState());
		registry.setBlockState("village_fence", Blocks.COBBLESTONE_WALL.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.BROWN_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.PETRIFICATION)
		{
			return new String[]{PETRIFICATION};
		} else
		{
			return new String[]{ROCK, STONE, ORE};
		}
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.EXTREME_HILLS;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 1 / 4F;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		int biomeMultiplier = type == LandBiomeType.ROUGH || type == LandBiomeType.OCEAN ? 2 : 1;
		
		if(type == LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.CLAY.defaultBlockState(), FeatureSpread.of(2, 3), 2, Lists.newArrayList(blocks.getBlockState("ocean_surface"), Blocks.CLAY.defaultBlockState())))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(25));
		}
		
		if(type == LandBiomeType.NORMAL)
		{
			if(this.type == Variant.ROCK)
			{
				builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.RANDOM_ROCK_BLOCK_BLOB
						.configured(new RandomRockBlockBlobConfig(3)).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(20));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.PETRIFIED_GRASS_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE));
				builder.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.GRASSY_SURFACE_DISK
						.configured(new SphereReplaceConfig(Blocks.COBBLESTONE.defaultBlockState(), FeatureSpread.of(2, 3), 1, Lists.newArrayList(blocks.getBlockState("surface"), Blocks.COBBLESTONE.defaultBlockState())))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(20));
				builder.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.GRASSY_SURFACE_DISK
						.configured(new SphereReplaceConfig(Blocks.STONE.defaultBlockState(), FeatureSpread.of(2, 2), 2, Lists.newArrayList(blocks.getBlockState("surface"), Blocks.STONE.defaultBlockState())))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(20));
			} else if(this.type == Variant.PETRIFICATION)
			{
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE
						.configured(new BlockStateFeatureConfig(MSBlocks.PETRIFIED_LOG.defaultBlockState()))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(10));
				builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BLOCK_BLOB
						.configured(new BlockStateFeatureConfig(Blocks.COBBLESTONE.defaultBlockState()))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(20));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.PETRIFIED_GRASS_CONFIG)
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.PETRIFIED_POPPY_CONFIG)
						.decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE).count(2));
			}
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			if(this.type == Variant.ROCK)
			{
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE
						.configured(new BlockStateFeatureConfig(MSBlocks.PETRIFIED_LOG.defaultBlockState()))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(20));
				builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.RANDOM_ROCK_BLOCK_BLOB
						.configured(new RandomRockBlockBlobConfig(5))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(30));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.PETRIFIED_GRASS_CONFIG)
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2));
				builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.STONE_MOUND
						.configured(new BlockStateFeatureConfig(blocks.getBlockState("ground")))
						.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE));
				builder.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.GRASSY_SURFACE_DISK
						.configured(new SphereReplaceConfig(Blocks.COBBLESTONE.defaultBlockState(), FeatureSpread.of(2, 3), 1, Lists.newArrayList(blocks.getBlockState("surface"), Blocks.COBBLESTONE.defaultBlockState())))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(20));
				builder.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.GRASSY_SURFACE_DISK
						.configured(new SphereReplaceConfig(Blocks.STONE.defaultBlockState(), FeatureSpread.of(2, 2), 2, Lists.newArrayList(blocks.getBlockState("surface"), Blocks.STONE.defaultBlockState())))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(20));
			} else if(this.type == Variant.PETRIFICATION)
			{
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE
						.configured(new BlockStateFeatureConfig(MSBlocks.PETRIFIED_LOG.defaultBlockState()))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(2, 0.5F, 1))));
				builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BLOCK_BLOB
						.configured(new BlockStateFeatureConfig(Blocks.COBBLESTONE.defaultBlockState()))
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(30));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.PETRIFIED_GRASS_CONFIG)
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.PETRIFIED_POPPY_CONFIG)
						.decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE));
			}
		}
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33))
				.range(256).squared().count(10 * biomeMultiplier));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.INFESTED_STONE.defaultBlockState(), 9))
				.range(64 * biomeMultiplier).squared().count(7 * biomeMultiplier));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 9))
				.range(64 * biomeMultiplier).squared().count(15 * biomeMultiplier));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.IRON_ORE.defaultBlockState(), 5))
				.range(64 * biomeMultiplier).squared().count(15 * biomeMultiplier));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 5))
				.range(32 * biomeMultiplier).squared().count(8 * biomeMultiplier));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 4))
				.range(24 * biomeMultiplier).squared().count(3 * biomeMultiplier));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.STONE_QUARTZ_ORE.defaultBlockState(), 4))
				.range(24 * biomeMultiplier).squared().count(3 * biomeMultiplier));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.GOLD_ORE.defaultBlockState(), 5))
				.range(32 * biomeMultiplier).squared().count(3 * biomeMultiplier));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 4))
				.range(24 * biomeMultiplier).squared().count(2 * biomeMultiplier));
		
		builder.addCarver(GenerationStage.Carving.AIR, WorldCarver.CANYON.configured(new ProbabilityConfig(0.4F)));
		
	}
	
	@Override
	public float getSkylightBase()
	{
		return 7 / 8F;
	}
	
	@Override
	public Vector3d getFogColor()
	{
		return new Vector3d(0.5, 0.5, 0.55);
	}
	
	@Override
	public Vector3d getSkyColor()
	{
		return new Vector3d(0.6D, 0.6D, 0.7D);
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
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return type == Variant.PETRIFICATION ? MSSoundEvents.MUSIC_PETRIFICATION : MSSoundEvents.MUSIC_ROCK;
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