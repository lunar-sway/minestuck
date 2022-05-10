package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class RainLandType extends TerrainLandType
{
	public static final String RAIN = "minestuck.rain";
	public static final String ISLANDS = "minestuck.islands";
	public static final String SKY = "minestuck.sky";
	
	private static final Vector3d skyColor = new Vector3d(0.3D, 0.5D, 0.98D);
	private static final Vector3d fogColor = new Vector3d(0.9D, 0.8D, 0.6D);
	
	//TODO:
	//Pink stone brick temples		Monsters in these temples tend to guard living trees, Magic Beans, and Fertile Soil.
	//Light Cloud Dungeons
	//Custom dungeon loot
	//Definitely nothing underwater
	//Giant beanstalks? Maybe some Paper Mario reference here
	
	
	public RainLandType()
	{
		super(false);
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setGroundState(MSBlocks.PINK_STONE.defaultBlockState(), MSFillerBlockTypes.PINK_STONE);
		registry.setBlockState("surface", MSBlocks.CHALK.defaultBlockState());
		registry.setBlockState("upper", MSBlocks.CHALK.defaultBlockState());
		registry.setBlockState("ocean", Blocks.WATER.defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.PINK_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", MSBlocks.PINK_STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", MSBlocks.CHISELED_PINK_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.POLISHED_PINK_STONE.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.CHALK_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.CHISELED_PINK_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_planks", MSBlocks.DEAD_PLANKS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", MSBlocks.DEAD_PLANKS_SLAB.defaultBlockState());
		registry.setBlockState("bush", Blocks.DEAD_BUSH.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.MAGENTA_WOOL.defaultBlockState());
		registry.setBlockState("cruxite_ore", MSBlocks.PINK_STONE_CRUXITE_ORE.defaultBlockState());
		registry.setBlockState("uranium_ore", MSBlocks.PINK_STONE_URANIUM_ORE.defaultBlockState());

	}
	
	@Override
	public String[] getNames() {
		return new String[] {RAIN, ISLANDS, SKY};
	}
	
	@Override
	public LandBiomeSet getBiomeSet()
	{
		return MSBiomes.HIGH_HUMID_LAND;
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.BEACH;	//I guess?
		
		properties.oceanBiomeScale += 0.1;
		properties.roughBiomeScale += 0.1;
		properties.roughBiomeDepth -= 0.2;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 3/4F;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE
					.configured(new BlockStateFeatureConfig(MSBlocks.DEAD_LOG.defaultBlockState()))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).chance(2));
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE
					.configured(new BlockStateFeatureConfig(MSBlocks.DEAD_LOG.defaultBlockState()))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).chance(4));
		}
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
				.configured(new SphereReplaceConfig(MSBlocks.POLISHED_PINK_STONE.defaultBlockState(), FeatureSpread.of(2, 1), 1, Lists.newArrayList(blocks.getBlockState("ground"))))
				.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(2));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_COAL_ORE.defaultBlockState(), 17))
				.range(64).squared().count(13));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_LAPIS_ORE.defaultBlockState(), 7))
				.range(24).squared().count(4));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_GOLD_ORE.defaultBlockState(), 9))
				.range(32).squared().count(4));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_DIAMOND_ORE.defaultBlockState(), 6))
				.range(24).squared().count(3));
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
		return MSSoundEvents.MUSIC_RAIN;
	}
}