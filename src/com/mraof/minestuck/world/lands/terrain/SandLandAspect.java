
package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.blocks.MSFillerBlockTypes;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.ArrayList;
import java.util.List;

public class SandLandAspect extends TerrainLandAspect
{
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "sand");
	private final Vec3d fogColor, skyColor;
	private final Variant type;
	
	public SandLandAspect(Variant variation)
	{
		super(GROUP_NAME);
		type = variation;
		
		if(type == Variant.SAND)
		{
			fogColor = new Vec3d(0.99D, 0.8D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.8D, 0.1D);
		} else
		{
			fogColor = new Vec3d(0.99D, 0.6D, 0.05D);
			skyColor = new Vec3d(0.8D, 0.6D, 0.1D);
		}
		
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SAND || type == Variant.LUSH_DESERTS)
		{
			registry.setGroundState(Blocks.SANDSTONE.getDefaultState(), MSFillerBlockTypes.SANDSTONE);
			registry.setBlockState("upper", Blocks.SAND.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.RED_SAND.getDefaultState());
		} else
		{
			registry.setGroundState(Blocks.RED_SANDSTONE.getDefaultState(), MSFillerBlockTypes.RED_SANDSTONE);
			registry.setBlockState("upper", Blocks.RED_SAND.getDefaultState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_RED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_RED_SANDSTONE.getDefaultState());
			registry.setBlockState("structure_primary_stairs", Blocks.RED_SANDSTONE_STAIRS.getDefaultState());
			registry.setBlockState("village_path", Blocks.SAND.getDefaultState());
		}
		registry.setBlockState("structure_secondary", Blocks.STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_planks", Blocks.ACACIA_PLANKS.getDefaultState());
		registry.setBlockState("structure_planks_slab", Blocks.ACACIA_SLAB.getDefaultState());
		registry.setBlockState("river", registry.getBlockState("upper"));
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.MAGENTA_WOOL.getDefaultState());
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.LUSH_DESERTS) {
			return new String[] {"lush_deserts"};
		} else {
			return new String[] {"sand", "dune", "desert"};
		}
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.category = Biome.Category.DESERT;
		settings.downfall = 0.0F;
		settings.temperature = 2.0F;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 0.0F;
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(blocks.getBlockState("upper"), 7, 2, Lists.newArrayList(blocks.getBlockState("ground"))), Placement.COUNT_TOP_SOLID, new FrequencyConfig(8)));
		if(type != Variant.RED_SAND)
		{
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SANDSTONE_IRON_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(24, 0, 0, 64)));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SANDSTONE_GOLD_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(6, 0, 0, 32)));
		} else
		{
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_IRON_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(24, 0, 0, 64)));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_GOLD_ORE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(6, 0, 0, 32)));
		}
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		/*list.add(new WorldGenDecorator(new WorldGenCactus(), 15, 0.4F, BiomeMinestuck.mediumNormal));
		list.add(new WorldGenDecorator(new WorldGenCactus(), 5, 0.4F, BiomeMinestuck.mediumRough));
		list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		list.add(new OasisDecorator());
		if(type == Variant.LUSH_DESERTS) {
			list.add(new WorldGenDecorator(new WorldGenCactus(), 45, 0.4F, BiomeMinestuck.mediumNormal));
			list.add(new WorldGenDecorator(new WorldGenCactus(), 30, 0.4F, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.bloomingCactus, true, 55, 15, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.bloomingCactus, true, 65, 15, BiomeMinestuck.mediumNormal));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.desertBush, true, 1, 1, BiomeMinestuck.mediumNormal));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.desertBush, true, 1, 1, BiomeMinestuck.mediumRough));
		} else {
			list.add(new WorldGenDecorator(new WorldGenCactus(), 15, 0.4F, BiomeMinestuck.mediumNormal));
			list.add(new WorldGenDecorator(new WorldGenCactus(), 5, 0.4F, BiomeMinestuck.mediumRough));
			list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
			list.add(new WorldGenDecorator(new WorldGenDeadBush(), 1, 0.4F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
			list.add(new SurfaceMushroomGenerator(MinestuckBlocks.bloomingCactus, true, 5, 32, BiomeMinestuck.mediumRough));
		}
		list.add(new OasisDecorator(BiomeMinestuck.mediumNormal));
		*/
		return list;
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
	
	public enum Variant
	{
		SAND,
		LUSH_DESERTS,
		RED_SAND;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}
