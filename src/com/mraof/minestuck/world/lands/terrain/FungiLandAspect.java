package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.lands.structure.IGateStructure;
import com.mraof.minestuck.world.lands.structure.GateStructureMushroom;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class FungiLandAspect extends TerrainLandAspect
{
	private static final Vec3d fogColor = new Vec3d(0.69D, 0.76D, 0.61D);
	private static final Vec3d skyColor = new Vec3d(0.69D, 0.76D, 0.61D);
	
	public FungiLandAspect()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.MYCELIUM.getDefaultState());
		registry.setBlockState("upper", Blocks.DIRT.getDefaultState());
		registry.setBlockState("ocean", Blocks.WATER.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.MOSSY_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MSBlocks.MYCELIUM_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.MYCELIUM_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("village_path", Blocks.GRASS_PATH.getDefaultState());
		registry.setBlockState("light_block", MSBlocks.GLOWY_GOOP.getDefaultState());
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
		registry.setBlockState("mushroom_1", Blocks.RED_MUSHROOM.getDefaultState());
		registry.setBlockState("mushroom_2", Blocks.BROWN_MUSHROOM.getDefaultState());
		registry.setBlockState("bush", Blocks.BROWN_MUSHROOM.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.LIME_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.getDefaultState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"fungi", "dank", "must", "mold", "mildew", "mycelium"};
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.category = Biome.Category.MUSHROOM;
		settings.rainType = Biome.RainType.RAIN;
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.staticBiome == MSBiomes.LAND_NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.RANDOM_BOOLEAN_SELECTOR, new TwoFeatureChoiceConfig(Feature.HUGE_RED_MUSHROOM, new BigMushroomFeatureConfig(false), Feature.HUGE_BROWN_MUSHROOM, new BigMushroomFeatureConfig(false)), Placement.COUNT_HEIGHTMAP, new FrequencyConfig(1)));
			
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(blocks.getBlockState("slime"), 7, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))), Placement.COUNT_TOP_SOLID, new FrequencyConfig(30)));
		} else if(biome.staticBiome == MSBiomes.LAND_ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.RANDOM_BOOLEAN_SELECTOR, new TwoFeatureChoiceConfig(Feature.HUGE_RED_MUSHROOM, new BigMushroomFeatureConfig(false), Feature.HUGE_BROWN_MUSHROOM, new BigMushroomFeatureConfig(false)), Placement.COUNT_HEIGHTMAP, new FrequencyConfig(3)));
			
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(blocks.getBlockState("slime"), 7, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))), Placement.COUNT_TOP_SOLID, new FrequencyConfig(60)));
		}
		
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(Blocks.RED_MUSHROOM.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(2)));
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(Blocks.BROWN_MUSHROOM.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(2)));
		
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.getDefaultState(), 33), Placement.COUNT_RANGE, new CountRangeConfig(8, 0, 0, 256)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), Blocks.IRON_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(24, 0, 0, 64)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), Blocks.REDSTONE_ORE.getDefaultState(), 8), Placement.COUNT_RANGE, new CountRangeConfig(12, 0, 0, 32)));
		
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
	public IGateStructure getGateStructure()
	{
		return new GateStructureMushroom();
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return MSEntityTypes.SALAMANDER;
	}
}