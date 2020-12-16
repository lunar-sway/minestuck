package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;

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
		registry.setGroundState(MSBlocks.PINK_STONE.getDefaultState(), MSFillerBlockTypes.PINK_STONE);
		registry.setBlockState("surface", MSBlocks.CHALK.getDefaultState());
		registry.setBlockState("upper", MSBlocks.CHALK.getDefaultState());
		registry.setBlockState("ocean", Blocks.WATER.getDefaultState());
		registry.setBlockState("structure_primary", MSBlocks.PINK_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_primary_stairs", MSBlocks.PINK_STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_primary_decorative", MSBlocks.CHISELED_PINK_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary", MSBlocks.POLISHED_PINK_STONE.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.CHALK_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.CHISELED_PINK_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_planks", MSBlocks.DEAD_PLANKS.getDefaultState());
		registry.setBlockState("structure_planks_slab", MSBlocks.DEAD_PLANKS_SLAB.getDefaultState());
		registry.setBlockState("bush", Blocks.DEAD_BUSH.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.MAGENTA_WOOL.getDefaultState());
		registry.setBlockState("cruxite_ore", MSBlocks.PINK_STONE_CRUXITE_ORE.getDefaultState());
		registry.setBlockState("uranium_ore", MSBlocks.PINK_STONE_URANIUM_ORE.getDefaultState());

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
	/*
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 3/4F;
	}
	
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type == BiomeType.NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE.withConfiguration(new BlockStateFeatureConfig(MSBlocks.DEAD_LOG.getDefaultState())).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(new ChanceConfig(2))));
		}
		
		if(biome.type == BiomeType.ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE.withConfiguration(new BlockStateFeatureConfig(MSBlocks.DEAD_LOG.getDefaultState())).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(new ChanceConfig(4))));
		}
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(MSBlocks.POLISHED_PINK_STONE.getDefaultState(), 4, 1, Lists.newArrayList(blocks.getBlockState("ground")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(2))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_COAL_ORE.getDefaultState(), 17)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(13, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_LAPIS_ORE.getDefaultState(), 7)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 24))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_GOLD_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 32))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_DIAMOND_ORE.getDefaultState(), 6)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 24))));
	}
	*/
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