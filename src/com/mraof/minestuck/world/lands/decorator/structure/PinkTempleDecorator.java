package com.mraof.minestuck.world.lands.decorator.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class PinkTempleDecorator extends SimpleStructureDecorator
{
	
	public PinkTempleDecorator(Biome... biomes)
	{
		super(biomes);
	}
	
	@Override
	protected BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		rotation = random.nextBoolean();
		
		xCoord = pos.getX();
		zCoord = pos.getZ();
		yCoord = getAverageHeight(world);
		if(yCoord == -1)
			return null;
		if(provider.isBBInSpawn(new StructureBoundingBox(xCoord - 4, zCoord - 4, xCoord + 4, zCoord + 4)))
			return null;
		
		IBlockState wall	= provider.blockRegistry.getBlockState("structure_primary");
		IBlockState wallDec	= provider.blockRegistry.getBlockState("structure_primary_decorative");
		IBlockState floor	= provider.blockRegistry.getBlockState("structure_secondary");
		IBlockState floorDec= provider.blockRegistry.getBlockState("structure_secondary_decorative");
		IBlockState stairs0	= provider.blockRegistry.getStairs("structure_secondary_stairs", rotation ? EnumFacing.EAST : EnumFacing.SOUTH, false);
		IBlockState stairs1	= provider.blockRegistry.getStairs("structure_secondary_stairs", rotation ? EnumFacing.WEST : EnumFacing.NORTH, false);
		
		for(int x = -3; x <= 3; x++)
			for(int z = -2; z <= 3; z++)
				placeFloor(world, floor, x, 0, z);
		
		for(int x = -1; x <= 1; x++)
		{
			placeFloor(world, floor, x, 0, -3);
			placeFloor(world, floor, x, 0, 4);
			placeBlock(world, stairs0, x, 0, -3);
			placeBlock(world, stairs1, x, 0, 4);
		}
		placeBlocks(world, floorDec, -1, 0, -2, 1, 0, -2);
		placeBlocks(world, floorDec, -1, 0, 3, 1, 0, 3);
		
		placeBlocks(world, wallDec, -3, 1, -2, -3, 3, -2);
		placeBlocks(world, wallDec, 3, 1, -2, 3, 3, -2);
		placeBlocks(world, wallDec, 3, 1, 3, 3, 3, 3);
		placeBlocks(world, wallDec, -3, 1, 3, -3, 3, 3);
		
		placeBlocks(world, wall, -3, 1, -1, -3, 3, 2);
		placeBlocks(world, wall, 3, 1, -1, 3, 3, 2);
		placeBlocks(world, wall, -2, 1, -2, -2, 3, -2);
		placeBlocks(world, wall, -2, 1, 3, -2, 3, 3);
		placeBlocks(world, wall, 2, 1, 3, 2, 3, 3);
		placeBlocks(world, wall, 2, 1, -2, 2, 3, -2);
		
		placeBlocks(world, wall, -2, 4, -2, -2, 4, 3);
		placeBlocks(world, wall, 2, 4, -2, 2, 4, 3);
		placeBlocks(world, wall, -1, 5, -2, 1, 5, 3);
		
		placeBlocks(world, Blocks.AIR.getDefaultState(), -2, 1, -1, 2, 3, 2);
		
		for(int z = -1; z <= 2; z++)
			for(int y = 1; y <= 3; y++)
			{
				if(random.nextFloat() < 0.7F)
					placeBlock(world, Blocks.BOOKSHELF.getDefaultState(), -2, y, z);
				if(random.nextFloat() < 0.7F)
					placeBlock(world, Blocks.BOOKSHELF.getDefaultState(), 2, y, z);
			}
		
		IBlockState stairs2 = provider.blockRegistry.getStairs("structure_primary_stairs", rotation ? EnumFacing.SOUTH : EnumFacing.EAST, false);
		IBlockState stairs3 = provider.blockRegistry.getStairs("structure_primary_stairs", rotation ? EnumFacing.NORTH : EnumFacing.WEST, false);
		IBlockState stairs4 = provider.blockRegistry.getStairs("structure_primary_stairs", rotation ? EnumFacing.SOUTH : EnumFacing.EAST, true);
		IBlockState stairs5 = provider.blockRegistry.getStairs("structure_primary_stairs", rotation ? EnumFacing.NORTH : EnumFacing.WEST, true);
		
		placeBlocks(world, stairs2, -3, 4, -2, -3, 4, 3);
		placeBlocks(world, stairs2, -2, 5, -2, -2, 5, 3);
		placeBlocks(world, stairs3, 3, 4, -2, 3, 4, 3);
		placeBlocks(world, stairs3, 2, 5, -2, 2, 5, 3);
		
		placeBlock(world, stairs4, 1, 4, -2);
		placeBlock(world, stairs4, 1, 4, 3);
		placeBlock(world, stairs5, -1, 4, -2);
		placeBlock(world, stairs5, -1, 4, 3);
		
		return null;
	}
	
	protected int getAverageHeight(World world)
	{
		int value = 0;
		int minVal = Integer.MAX_VALUE, maxVal = Integer.MIN_VALUE;
		int minDepth = Integer.MAX_VALUE;
		
		for(int x = (rotation ? -2 : -3); x < 4; x++)
			for(int z = (rotation ? -3 : -2); z < 4; z++)
			{
				int height = world.getPrecipitationHeight(new BlockPos(xCoord + x, 0, zCoord + z)).getY();
				value += height;
				minVal = Math.min(minVal, height);
				maxVal = Math.max(maxVal, height);
				minDepth = Math.min(minDepth, world.getTopSolidOrLiquidBlock(new BlockPos(xCoord + x, 0, zCoord + z)).getY());
			}
		
		if(maxVal - minVal > 4 || minVal - minDepth > 3)
			return -1;
		value /= 42;
		value += 1;
		return value;
	}
	
	@Override
	public int getCount(Random random)
	{
		return random.nextFloat() < 0.02F ? 1 : 0;
	}
	
	@Override
	public float getPriority()
	{
		return 0.4F;
	}
}