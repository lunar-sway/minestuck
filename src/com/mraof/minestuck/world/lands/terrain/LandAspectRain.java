package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.decorator.LeaflessTreeDecorator;
import com.mraof.minestuck.world.lands.decorator.UndergroundDecoratorVein;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;

public class LandAspectRain extends TerrainLandAspect 
{
	static Vec3d skyColor = new Vec3d(0.3D, 0.5D, 0.98D);
	static Vec3d fogColor = new Vec3d(0.9D, 0.8D, 0.6D);
	
	//TODO:
	//Pink stone brick temples		Monsters in these temples tend to guard living trees, Magic Beans, and Fertile Soil.
	//Light Cloud Dungeons
	//Custom dungeon loot
	//Definitely nothing underwater
	//Giant beanstalks? Maybe some Paper Mario reference here
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", MinestuckBlocks.CHALK.getDefaultState());
		registry.setBlockState("upper", MinestuckBlocks.CHALK.getDefaultState());
		registry.setBlockState("ground", MinestuckBlocks.PINK_STONE.getDefaultState());
		registry.setBlockState("ocean", Blocks.WATER.getDefaultState());
		registry.setBlockState("structure_primary", MinestuckBlocks.PINK_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_primary_stairs", MinestuckBlocks.PINK_STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_primary_decorative", MinestuckBlocks.CHISELED_PINK_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_secondary", MinestuckBlocks.POLISHED_PINK_STONE.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", MinestuckBlocks.CHALK_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MinestuckBlocks.CHISELED_PINK_STONE_BRICKS.getDefaultState());
		registry.setBlockState("structure_planks", MinestuckBlocks.DEAD_PLANKS.getDefaultState());
		registry.setBlockState("structure_planks_slab", MinestuckBlocks.DEAD_PLANKS_SLAB.getDefaultState());
		registry.setBlockState("bush", Blocks.DEAD_BUSH.getDefaultState());
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.getDefaultState());
		registry.setBlockState("structure_wool_3", Blocks.MAGENTA_WOOL.getDefaultState());
	}
	
	@Override
	public String getPrimaryName()
	{
		return "rain";
	}
	
	@Override
	public String[] getNames() {
		return new String[] {"rain", "islands", "sky"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new LeaflessTreeDecorator(MinestuckBlocks.DEAD_LOG.getDefaultState(), 0.5F, ModBiomes.mediumNormal));
		list.add(new LeaflessTreeDecorator(MinestuckBlocks.DEAD_LOG.getDefaultState(), 0.25F, ModBiomes.mediumRough));
		
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.POLISHED_PINK_STONE.getDefaultState(), 2, 8, 64));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.COAL_ORE_PINK_STONE.getDefaultState(), 13, 17, 64));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.LAPIS_ORE_PINK_STONE.getDefaultState(), 4, 7, 24));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.GOLD_ORE_PINK_STONE.getDefaultState(), 4, 9, 32));
		list.add(new UndergroundDecoratorVein(MinestuckBlocks.DIAMOND_ORE_PINK_STONE.getDefaultState(), 3, 6, 24));
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
	public float getTemperature()
	{
		return 0.5F;
	}
	
	@Override
	public float getOceanChance()
	{
		return 3/4F;
	}
	
	@Override
	public float getRainfall()
	{
		return 0.9F;
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return ModEntityTypes.TURTLE;
	}
}