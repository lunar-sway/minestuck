package com.mraof.minestuck.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReturnNode extends BlockGate
{
	
	public BlockReturnNode()
	{
		blockResistance = 25.0F;
	}
	
	@Override
	protected boolean isValid(BlockPos pos, World world)
	{
		for(int x = -1; x <= 0; x++)
			for(int z = -1; z <= 0; z++)
				if(x != 0 || z != 0)
				{
					IBlockState block = world.getBlockState(pos.add(x, 0, z));
					if(block.getBlock() != this || (Boolean) block.getValue(isMainComponent))
						return false;
				}
		
		return true;
	}
	
	@Override
	protected BlockPos findMainComponent(BlockPos pos, World world)
	{
		for(int x = 0; x <= 1; x++)
			for(int z = 0; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					IBlockState block = world.getBlockState(pos.add(x, 0, z));
					if(block.getBlock() == this && (Boolean) block.getValue(isMainComponent))
						return pos.add(x, 0, z);
				}
		
		return null;
	}
	
	@Override
	protected void removePortal(BlockPos pos, World world)
	{
		for(int x = -1; x <= 0; x++)
			for(int z = -1; z <= 0; z++)
				if(world.getBlockState(pos.add(x, 0, z)).getBlock() == this)
					world.setBlockToAir(pos.add(x, 0, z));
	}
	
}
