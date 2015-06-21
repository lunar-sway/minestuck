package com.mraof.minestuck.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockReturnNode extends BlockGate
{
	
	public BlockReturnNode()
	{
		this.setBlockBounds(0F, 0.45F, 0F, 1F, 0.55F, 1F);
	}
	
	@Override
	protected boolean isValid(BlockPos pos, World world, IBlockState state)
	{
		if((Boolean) state.getValue(isMainComponent))
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
		} else
		{
			for(int x = 0; x <= 1; x++)
				for(int z = 0; z <= 1; z++)
					if(x != 0 || z != 0)
					{
						IBlockState block = world.getBlockState(pos.add(x, 0, z));
						if(block.getBlock() == this && (Boolean) block.getValue(isMainComponent))
							return this.isValid(pos.add(x, 0, z), world, block);
					}
			
			return false;
		}
	}
	
}
