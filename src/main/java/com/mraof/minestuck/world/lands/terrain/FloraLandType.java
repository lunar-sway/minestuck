package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MinestuckBiomeFeatures;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.ForestFlowerBlockStateProvider;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class FloraLandType extends TerrainLandType
{
	public static final String FLORA = "minestuck.flora";
	public static final String FLOWERS = "minestuck.flowers";
	public static final String THORNS = "minestuck.thorns";
	
	private static final Vector3d fogColor = new Vector3d(0.5D, 0.6D, 0.9D);
	private static final Vector3d skyColor = new Vector3d(0.6D, 0.8D, 0.6D);
	
	public FloraLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.GRASS_BLOCK.defaultBlockState());
		registry.setBlockState("surface_rough", Blocks.COARSE_DIRT.defaultBlockState());
		registry.setBlockState("upper", Blocks.DIRT.defaultBlockState());
		registry.setBlockState("ocean", MSBlocks.BLOOD.defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.MOSSY_DECREPIT_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_mossy", MSBlocks.MOSSY_DECREPIT_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", Blocks.MOSSY_COBBLESTONE.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.FLOWERY_MOSSY_COBBLESTONE.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.DARK_OAK_STAIRS.defaultBlockState());
		registry.setBlockState("village_path", Blocks.GRASS_PATH.defaultBlockState());
		registry.setBlockState("bush", Blocks.FERN.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.CYAN_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{FLORA, FLOWERS, THORNS};
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.FOREST;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_FOREST);
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER
					.configured((new BlockClusterFeatureConfig.Builder(ForestFlowerBlockStateProvider.INSTANCE, SimpleBlockPlacer.INSTANCE)).tries(64).build())
					.decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE).count(65));
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.DARK_OAK.decorated(Features.Placements.HEIGHTMAP_SQUARE).count(25));
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BROKEN_SWORD.configured(IFeatureConfig.NONE).chance(50));
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(MinestuckBiomeFeatures.STRAWBERRY_CONFIG)
					.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(60));
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE.configured(new BlockStateFeatureConfig(MSBlocks.BLOOD.defaultBlockState()))
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).count(25));
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.SURFACE_FOSSIL.configured(IFeatureConfig.NONE).chance(5));
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BROKEN_SWORD.configured(IFeatureConfig.NONE).chance(10));
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(MinestuckBiomeFeatures.STRAWBERRY_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE));
		}
		
		if(type == LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.CLAY.defaultBlockState(), FeatureSpread.of(2, 1), 1, Lists.newArrayList(blocks.getBlockState("ocean_surface"), Blocks.CLAY.defaultBlockState())))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE));
		}
		
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 33))
				.range(256).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33))
				.range(256).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17))
				.range(64).squared().count(13));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.EMERALD_ORE.defaultBlockState(), 3))
				.range(32).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 3))
				.range(32).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 3))
				.range(32).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.STONE_QUARTZ_ORE.defaultBlockState(), 5))
				.range(32).squared().count(8));
		
		builder.addCarver(GenerationStage.Carving.AIR, ConfiguredCarvers.CAVE);
	}
	
	@Override
	public Vector3d getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public Vector3d getSkyColor()
	{
		return skyColor;
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return MSEntityTypes.IGUANA;
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addIguanaVillageCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addIguanaVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_FLORA;
	}
}