package com.mraof.minestuck.world.lands.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GateStructureMushroom implements IGateStructure {

	@Override
	public BlockPos generateGateStructure(World world, BlockPos pos, ChunkProviderLands provider)
	{
		IBlockState stem = Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
		IBlockState mushroom = Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE);
		Random rand = world.setRandomSeed(pos.getX(), pos.getZ(), 1849234152^world.provider.getDimension());
		pos = world.getTopSolidOrLiquidBlock(pos);
		pos = pos.up(20);
		BlockPos gatePos = pos.up(4);
		
		for(int i=0; i<20; i++) {
			world.setBlockState(pos.down(i+1), stem);
		}
		
		for(int x = -5; x <= 5; x++)
		{
		    for(int z = -5; z<= 5; z++)
		    {
		    	if(Math.abs(x) + Math.abs(z) >= 7)
		    	{
		    	    world.setBlockState(pos.north(x).west(z), Blocks.AIR.getDefaultState());
		    	} else
		    	{
		    	    world.setBlockState(pos.north(x).west(z), mushroom);
		    	}
		    }
		}
		
		
		return gatePos;
	}

}
