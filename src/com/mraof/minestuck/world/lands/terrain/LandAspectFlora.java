package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.BlockMinestuckLog;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.*;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class LandAspectFlora extends TerrainLandAspect
{
	//TODO:
	//Swords
	//Strawberries
	//Consort dialogue
	//Blood oceans? Some kind of tie between the plants and blood or death. Blood oceans are good, actually.
	//Custom dungeon loot
	//Uranium blocks, maybe as viable fuel for the uranium cooker
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.GRASS.getDefaultState());
		registry.setBlockState("upper", Blocks.DIRT.getDefaultState());
		registry.setBlockState("structure_primary", Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY));
		registry.setBlockState("structure_primary_decorative", MinestuckBlocks.floweryMossBrick.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		registry.setBlockState("structure_secondary", Blocks.MOSSY_COBBLESTONE.getDefaultState());
		registry.setBlockState("structure_secondary_decorative", MinestuckBlocks.floweryMossStone.getDefaultState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_STAIRS.getDefaultState());
		registry.setBlockState("village_path", Blocks.GRASS_PATH.getDefaultState());
		registry.setBlockState("bush", Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN));
		registry.setBlockState("structure_wool_1", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW));
		registry.setBlockState("structure_wool_3", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.CYAN));
	}
	
	@Override
	public String getPrimaryName()
	{
		return "flora";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"flora", "flowers", "thorns"};
	}
	
	@Override
	public List<ILandDecorator> getDecorators()
	{
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new CanopyTreeDecorator(25, BiomeMinestuck.mediumNormal));
		list.add(new CanopyTreeDecorator(3, BiomeMinestuck.mediumRough));
		list.add(new TallGrassDecorator(0.3F, BiomeMinestuck.mediumNormal));
		list.add(new TallGrassDecorator(0.5F, 0.2F, BiomeMinestuck.mediumRough));
		list.add(new FlowerDecorator(0.5F, 0.2F, BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		
		list.add(new UndergroundDecoratorVein(Blocks.DIRT.getDefaultState(), 3, 33, 64));
		list.add(new UndergroundDecoratorVein(Blocks.GRAVEL.getDefaultState(), 2, 28, 64));
		list.add(new UndergroundDecoratorVein(Blocks.COAL_ORE.getDefaultState(), 13, 17, 64));
		list.add(new UndergroundDecoratorVein(Blocks.EMERALD_ORE.getDefaultState(), 8, 3, 32));
		list.add(new UndergroundDecoratorVein(Blocks.LAPIS_ORE.getDefaultState(), 8, 3, 32));
		list.add(new SurfaceDecoratorVein(Blocks.CLAY.getDefaultState(), 15, 10, BiomeMinestuck.mediumOcean));
		return list;
	}
	
	@Override
	public int getDayCycleMode()
	{
		return 0;
	}
	
	@Override
	public Vec3d getFogColor()
	{
		return new Vec3d(0.0D, 1.0D, 0.6D);
	}
	
	@Override
	public float getRainfall()
	{
		return 0.4F;
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.IGUANA;
	}
}