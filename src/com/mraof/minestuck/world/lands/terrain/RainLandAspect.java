package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.blocks.MSFillerBlockTypes;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class RainLandAspect extends TerrainLandAspect
{
	private static final Vec3d skyColor = new Vec3d(0.3D, 0.5D, 0.98D);
	private static final Vec3d fogColor = new Vec3d(0.9D, 0.8D, 0.6D);
	
	//TODO:
	//Pink stone brick temples		Monsters in these temples tend to guard living trees, Magic Beans, and Fertile Soil.
	//Light Cloud Dungeons
	//Custom dungeon loot
	//Definitely nothing underwater
	//Giant beanstalks? Maybe some Paper Mario reference here
	
	
	public RainLandAspect()
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
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"rain", "islands", "sky"};
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.category = Biome.Category.BEACH;	//I guess?
		settings.downfall = 0.9F;
		settings.temperature = 0.5F;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 3/4F;
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.staticBiome == MSBiomes.LAND_NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(MSFeatures.LEAFLESS_TREE, new BushConfig(MSBlocks.DEAD_LOG.getDefaultState()), Placement.CHANCE_TOP_SOLID_HEIGHTMAP, new ChanceConfig(2)));
		}
		
		if(biome.staticBiome == MSBiomes.LAND_ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(MSFeatures.LEAFLESS_TREE, new BushConfig(MSBlocks.DEAD_LOG.getDefaultState()), Placement.CHANCE_TOP_SOLID_HEIGHTMAP, new ChanceConfig(4)));
		}
		
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(MSBlocks.POLISHED_PINK_STONE.getDefaultState(), 2, 1, Lists.newArrayList(blocks.getBlockState("ground"))), Placement.COUNT_TOP_SOLID, new FrequencyConfig(2)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_COAL_ORE.getDefaultState(), 17), Placement.COUNT_RANGE, new CountRangeConfig(13, 0, 0, 64)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_LAPIS_ORE.getDefaultState(), 7), Placement.COUNT_RANGE, new CountRangeConfig(4, 0, 0, 24)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_GOLD_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(4, 0, 0, 32)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.PINK_STONE_DIAMOND_ORE.getDefaultState(), 6), Placement.COUNT_RANGE, new CountRangeConfig(3, 0, 0, 24)));
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
		return MSEntityTypes.TURTLE;
	}
}