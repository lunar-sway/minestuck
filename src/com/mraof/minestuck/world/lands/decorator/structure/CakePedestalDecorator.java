package com.mraof.minestuck.world.lands.decorator.structure;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class CakePedestalDecorator extends SimpleStructureDecorator
{
	public CakePedestalDecorator(Biome... biomes)
	{
		super(biomes);
	}
	
	@Override
	protected BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		IBlockState block = provider.blockRegistry.getBlockState("structure_secondary");
		IBlockState blockDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
		
		IBlockState stairsN = provider.blockRegistry.getStairs("structure_secondary_stairs", EnumFacing.NORTH, false);
		IBlockState stairsE = provider.blockRegistry.getStairs("structure_secondary_stairs", EnumFacing.EAST, false);
		IBlockState stairsS = provider.blockRegistry.getStairs("structure_secondary_stairs", EnumFacing.SOUTH, false);
		IBlockState stairsW = provider.blockRegistry.getStairs("structure_secondary_stairs", EnumFacing.WEST, false);
		
		xCoord = pos.getX();
		zCoord = pos.getZ();
		yCoord = world.getPrecipitationHeight(pos).getY();
		
		if(world.getBlockState(new BlockPos(xCoord, yCoord - 1, zCoord)).getMaterial().isLiquid())
			return null;
		
		placeBlocks(world, block, -1, 0, 0, 1, 0, 0);
		placeBlock(world, block, 0, 0, -1);
		placeBlock(world, block, 0, 0, 1);
		placeBlock(world, blockDecor, 0, 1, 0);
		placeBlocks(world, stairsS, -1, 0, -2, 0, 0, -2);
		placeBlock(world, stairsE, -1, 0, -1);
		placeBlock(world, stairsS, -2, 0, -1);
		placeBlocks(world, stairsE, -2, 0, 0, -2, 0, 1);
		placeBlock(world, stairsN, -1, 0, 1);
		placeBlock(world, stairsE, -1, 0, 2);
		placeBlocks(world, stairsN, 0, 0, 2, 1, 0, 2);
		placeBlock(world, stairsW, 1, 0, 1);
		placeBlock(world, stairsN, 2, 0, 1);
		placeBlocks(world, stairsW, 2, 0, -1, 2, 0, 0);
		placeBlock(world, stairsS, 1, 0, -1);
		placeBlock(world, stairsW, 1, 0, -2);
		
		IBlockState cake = MinestuckBlocks.fuchsiaCake.getDefaultState();
		
		placeBlock(world, cake, 0, 2, 0);
		
		return null;
	}
	
	@Override
	public int getCount(Random random)
	{
		return random.nextInt(100) == 0 ? 1 : 0;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
}