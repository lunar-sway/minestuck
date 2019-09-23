package com.mraof.minestuck.world.lands.decorator.structure;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;

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
		BlockState block = provider.blockRegistry.getBlockState("structure_secondary");
		BlockState blockDecor = provider.blockRegistry.getBlockState("structure_secondary_decorative");
		
		BlockState stairsN = provider.blockRegistry.getStairs("structure_secondary_stairs", Direction.NORTH, false);
		BlockState stairsE = provider.blockRegistry.getStairs("structure_secondary_stairs", Direction.EAST, false);
		BlockState stairsS = provider.blockRegistry.getStairs("structure_secondary_stairs", Direction.SOUTH, false);
		BlockState stairsW = provider.blockRegistry.getStairs("structure_secondary_stairs", Direction.WEST, false);
		
		xCoord = pos.getX();
		zCoord = pos.getZ();
		yCoord = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).getY();
		
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
		
		BlockState cake = MSBlocks.FUCHSIA_CAKE.getDefaultState();
		
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