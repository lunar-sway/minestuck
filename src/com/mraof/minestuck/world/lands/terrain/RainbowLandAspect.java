package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.gen.feature.ModFeatures;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.MesaDecorator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;

public class RainbowLandAspect extends TerrainLandAspect
{
	private static final Vec3d fogColor = new Vec3d(0.0D, 0.6D, 0.8D);
	private static final Vec3d skyColor = new Vec3d(0.9D, 0.6D, 0.8D);
	
	public RainbowLandAspect()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("ground", Blocks.STONE.getDefaultState());
		registry.setBlockState("upper", Blocks.WHITE_TERRACOTTA.getDefaultState());
		registry.setBlockState("surface", Blocks.WHITE_WOOL.getDefaultState());
		registry.setBlockState("ocean", MinestuckBlocks.WATER_COLORS.getDefaultState());
		registry.setBlockState("structure_primary", MinestuckBlocks.RAINBOW_WOOD.getDefaultState());
		registry.setBlockState("structure_primary_decorative", Blocks.ACACIA_LOG.getDefaultState());
		registry.setBlockState("structure_primary_stairs", Blocks.DARK_OAK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.RAINBOW_PLANKS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", Blocks.SPRUCE_PLANKS.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.JUNGLE_STAIRS.getDefaultState());
		registry.setBlockState("salamander_floor", Blocks.STONE_BRICKS.getDefaultState());
		registry.setBlockState("light_block", MinestuckBlocks.GLOWING_WOOD.getDefaultState());
		BlockState rainbow_leaves = MinestuckBlocks.RAINBOW_LEAVES.getDefaultState();
		//	rainbow_leaves = rainbow_leaves.with(BlockMinestuckLeaves1.CHECK_DECAY, false).withProperty(BlockMinestuckLeaves1.DECAYABLE, false);
		registry.setBlockState("bush", rainbow_leaves);
		registry.setBlockState("mushroom_1", rainbow_leaves);
		registry.setBlockState("mushroom_2", rainbow_leaves);
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.GREEN_WOOL.getDefaultState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"rainbow", "colors"};
	}
	
	@Override
	public void setBiomeSettings(LandBiomeHolder settings)
	{
		settings.category = Biome.Category.PLAINS; //I guess?
		settings.downfall = 0.6F;
		settings.temperature = 1.0F;
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blockRegistry)
	{
		if(biome.staticBiome == ModBiomes.LAND_NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(ModFeatures.RAINBOW_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(4, 0.1F, 1)));
		} else if(biome.staticBiome == ModBiomes.LAND_ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(ModFeatures.RAINBOW_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(2, 0.1F, 1)));
		}
		
		biome.addSpawn(EntityClassification.WATER_CREATURE, new SpawnListEntry(EntityType.SQUID, 2, 3, 5));
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		
		list.add(new MesaDecorator().setAltFrequency(.01F));
		
		//Each of these is associated with one of the primary colors in Minecraft: black, red, blue, yellow, green, brown, and white
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 20, 17, 128));
		list.add(new UndergroundDecoratorVein(Blocks.REDSTONE_ORE.getDefaultState(), 10, 8, 32));
		list.add(new UndergroundDecoratorVein(Blocks.LAPIS_ORE.getDefaultState(), 4, 7, 24));
		list.add(new UndergroundDecoratorVein(Blocks.GOLD_ORE.getDefaultState(), 4, 9, 32));
		list.add(new UndergroundDecoratorVein(Blocks.EMERALD_ORE.getDefaultState(), 10, 8, 32));
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 3, 24, 64));
		list.add(new UndergroundDecoratorVein(Blocks.DIORITE.getDefaultState(), 10, 8, 32));
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
		return ModEntityTypes.TURTLE;
	}
}
