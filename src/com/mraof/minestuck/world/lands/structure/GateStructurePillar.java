package com.mraof.minestuck.world.lands.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GateStructurePillar implements IGateStructure
{

	@Override
	public BlockPos generateGateStructure(World world, BlockPos pos, ChunkProviderLands provider)
	{
		Random rand = world.setRandomSeed(pos.getX(), pos.getZ(), 1849234152^world.provider.getDimension());
		pos = world.getTopSolidOrLiquidBlock(pos);
		pos = pos.up(20);
		BlockPos gatePos = pos.up(4);
		
		do
		{
			world.setBlockState(pos, provider.groundBlock);
			world.setBlockState(pos.north(), provider.groundBlock);
			world.setBlockState(pos.east(), provider.groundBlock);
			world.setBlockState(pos.south(), provider.groundBlock);
			world.setBlockState(pos.west(), provider.groundBlock);
			
			if(rand.nextBoolean())
				world.setBlockState(pos.add(-1, 0, -1), provider.groundBlock);
			if(rand.nextBoolean())
				world.setBlockState(pos.add(-1, 0, 1), provider.groundBlock);
			if(rand.nextBoolean())
				world.setBlockState(pos.add(1, 0, 1), provider.groundBlock);
			if(rand.nextBoolean())
				world.setBlockState(pos.add(1, 0, -1), provider.groundBlock);
			
			pos = pos.down();
		} while(pos.getY() > 0 && !world.getBlockState(pos).equals(provider.groundBlock));
		
		return gatePos;
	}
}