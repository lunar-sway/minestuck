package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.*;

public class ShadeLandAspect extends TerrainLandAspect
{
	
	private static final Vec3d skyColor = new Vec3d(0.16D, 0.38D, 0.54D);
	
	public ShadeLandAspect()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("upper", MSBlocks.BLUE_DIRT.getDefaultState());
		registry.setBlockState("ocean", MSBlocks.OIL.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MSBlocks.SHADE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.SMOOTH_SHADE_STONE.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.SHADE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("village_path", Blocks.GRAVEL.getDefaultState());
		registry.setBlockState("light_block", MSBlocks.GLOWING_WOOD.getDefaultState());
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
		registry.setBlockState("wall_torch", Blocks.REDSTONE_WALL_TORCH.getDefaultState());
		registry.setBlockState("mushroom_1", MSBlocks.GLOWING_MUSHROOM.getDefaultState());
		registry.setBlockState("mushroom_2", MSBlocks.GLOWING_MUSHROOM.getDefaultState());
		registry.setBlockState("bush", MSBlocks.GLOWING_MUSHROOM.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.CYAN_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.getDefaultState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"shade"};
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.category = Biome.Category.MUSHROOM;
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.staticBiome == MSBiomes.LAND_NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(MSBlocks.GLOWING_MUSHROOM.getDefaultState()), Placement.COUNT_CHANCE_HEIGHTMAP, new HeightWithChanceConfig(1, 1/2F)));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(MSFeatures.LEAFLESS_TREE, new BushConfig(MSBlocks.GLOWING_LOG.getDefaultState()), Placement.CHANCE_HEIGHTMAP, new ChanceConfig(2)));
		}
		if(biome.staticBiome == MSBiomes.LAND_ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(MSBlocks.GLOWING_MUSHROOM.getDefaultState()), Placement.COUNT_CHANCE_HEIGHTMAP, new HeightWithChanceConfig(1, 1/4F)));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(MSFeatures.LEAFLESS_TREE, new BushConfig(MSBlocks.GLOWING_LOG.getDefaultState()), Placement.COUNT_HEIGHTMAP, new FrequencyConfig(2)));
		}
		
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.getDefaultState(), 33), Placement.COUNT_RANGE, new CountRangeConfig(8, 0, 0, 256)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), Blocks.IRON_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(24, 0, 0, 64)));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), Blocks.LAPIS_ORE.getDefaultState(), 7), Placement.COUNT_RANGE, new CountRangeConfig(6, 0, 0, 32)));
		
	}
	
	@Override
	public float getSkylightBase()
	{
		return 0F;
	}
	
	@Override
	public Vec3d getFogColor() 
	{
		return skyColor;
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return MSEntityTypes.SALAMANDER;
	}
}
