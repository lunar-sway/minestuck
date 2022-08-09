package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class RainbowLandType extends TerrainLandType
{
	public static final String RAINBOW = "minestuck.rainbow";
	public static final String COLORS = "minestuck.colors";
	
	private static final Vec3 fogColor = new Vec3(0.0D, 0.6D, 0.8D);
	private static final Vec3 skyColor = new Vec3(0.9D, 0.6D, 0.8D);
	
	public RainbowLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("upper", Blocks.WHITE_TERRACOTTA.defaultBlockState());
		registry.setBlockState("surface", Blocks.WHITE_WOOL.defaultBlockState());
		registry.setBlockState("ocean", MSBlocks.WATER_COLORS.get().defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.RAINBOW_WOOD.get().defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.ACACIA_LOG.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", Blocks.DARK_OAK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.RAINBOW_PLANKS.get().defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", Blocks.SPRUCE_PLANKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.JUNGLE_STAIRS.defaultBlockState());
		registry.setBlockState("salamander_floor", Blocks.STONE_BRICKS.defaultBlockState());
		registry.setBlockState("light_block", MSBlocks.GLOWING_WOOD.get().defaultBlockState());
		BlockState rainbow_leaves = MSBlocks.RAINBOW_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
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
		properties.category = Biome.BiomeCategory.PLAINS; //I guess?
	}
	
	@Override
	public void setSpawnInfo(MobSpawnSettings.Builder builder, LandBiomeType type)
	{
		builder.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 2, 3, 5));
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		/*
		builder.surfaceBuilder(MSSurfaceBuilders.RAINBOW.get().configured(blocks.getSurfaceBuilderConfig(type)));
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSCFeatures.RAINBOW_TREE
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(4, 0.1F, 1))));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSCFeatures.RAINBOW_TREE
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
		}
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSFeatures.MESA.configured(IFeatureConfig.NONE)
				.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(25));
		
		//Each of these is associated with one of the primary colors in Minecraft: black, red, blue, yellow, green, brown, and white
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17))
				.range(128).squared().count(20));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreConfiguration(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 8))
				.range(32).squared().count(10));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreConfiguration(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 7))
				.range(24).squared().count(4));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreConfiguration(blocks.getGroundType(), Blocks.GOLD_ORE.defaultBlockState(), 9))
				.range(32).squared().count(4));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreConfiguration(blocks.getGroundType(), Blocks.EMERALD_ORE.defaultBlockState(), 8))
				.range(32).squared().count(10));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreConfiguration(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 24))
				.range(64).squared().count(3));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreConfiguration(blocks.getGroundType(), Blocks.DIORITE.defaultBlockState(), 8))
				.range(32).squared().count(10));
		*/
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
