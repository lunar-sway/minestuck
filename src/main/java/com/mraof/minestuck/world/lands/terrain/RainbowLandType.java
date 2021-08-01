package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.MSSurfaceBuilders;
import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;

public class RainbowLandType extends TerrainLandType
{
	public static final String RAINBOW = "minestuck.rainbow";
	public static final String COLORS = "minestuck.colors";
	
	private static final Vector3d fogColor = new Vector3d(0.0D, 0.6D, 0.8D);
	private static final Vector3d skyColor = new Vector3d(0.9D, 0.6D, 0.8D);
	
	public RainbowLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("upper", Blocks.WHITE_TERRACOTTA.defaultBlockState());
		registry.setBlockState("surface", Blocks.WHITE_WOOL.defaultBlockState());
		registry.setBlockState("ocean", MSBlocks.WATER_COLORS.defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.RAINBOW_WOOD.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.ACACIA_LOG.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", Blocks.DARK_OAK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.RAINBOW_PLANKS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", Blocks.SPRUCE_PLANKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.JUNGLE_STAIRS.defaultBlockState());
		registry.setBlockState("salamander_floor", Blocks.STONE_BRICKS.defaultBlockState());
		registry.setBlockState("light_block", MSBlocks.GLOWING_WOOD.defaultBlockState());
		BlockState rainbow_leaves = MSBlocks.RAINBOW_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
		registry.setBlockState("bush", rainbow_leaves);
		registry.setBlockState("mushroom_1", rainbow_leaves);
		registry.setBlockState("mushroom_2", rainbow_leaves);
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.GREEN_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {RAINBOW, COLORS};
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.PLAINS; //I guess?
	}
	
	@Override
	public void setSpawnInfo(MobSpawnInfo.Builder builder, LandBiomeType type)
	{
		builder.addSpawn(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(EntityType.SQUID, 2, 3, 5));
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		builder.surfaceBuilder(MSSurfaceBuilders.RAINBOW.get().configured(blocks.getSurfaceBuilderConfig(type)));
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSCFeatures.get().RAINBOW_TREE
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(4, 0.1F, 1))));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSCFeatures.get().RAINBOW_TREE
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
		}
		builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.MESA.configured(IFeatureConfig.NONE)
				.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(25));
		
		//Each of these is associated with one of the primary colors in Minecraft: black, red, blue, yellow, green, brown, and white
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17))
				.range(128).squared().count(20));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 8))
				.range(32).squared().count(10));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 7))
				.range(24).squared().count(4));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.GOLD_ORE.defaultBlockState(), 9))
				.range(32).squared().count(4));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.EMERALD_ORE.defaultBlockState(), 8))
				.range(32).squared().count(10));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 24))
				.range(64).squared().count(3));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIORITE.defaultBlockState(), 8))
				.range(32).squared().count(10));
		
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
		return MSEntityTypes.TURTLE;
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addTurtleVillageCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addTurtleVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_RAINBOW;
	}
}
