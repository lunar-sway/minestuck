package com.mraof.minestuck.world.lands.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GateStructurePillar implements IGateStructure
{

	@Override
	public BlockPos generateGateStructure(World world, BlockPos pos, ChunkProviderLands provider)
	{
		BlockState ground = provider.blockRegistry.getBlockState("ground");
		//Random rand = world.setRandomSeed(pos.getX(), pos.getZ(), 1849234152^world.provider.getDimension());
		//pos = world.getTopSolidOrLiquidBlock(pos);
		pos = pos.up(20);
		BlockPos gatePos = pos.up(4);
		
		do
		{
			world.setBlockState(pos, ground);
			world.setBlockState(pos.north(), ground);
			world.setBlockState(pos.east(), ground);
			world.setBlockState(pos.south(), ground);
			world.setBlockState(pos.west(), ground);
			
			/*if(rand.nextBoolean())
				world.setBlockState(pos.add(-1, 0, -1), ground);
			if(rand.nextBoolean())
				world.setBlockState(pos.add(-1, 0, 1), ground);
			if(rand.nextBoolean())
				world.setBlockState(pos.add(1, 0, 1), ground);
			if(rand.nextBoolean())
				world.setBlockState(pos.add(1, 0, -1), ground);*/
			
			pos = pos.down();
		} while(pos.getY() > 0 && !world.getBlockState(pos).equals(ground));
		
		return gatePos;
	}
}