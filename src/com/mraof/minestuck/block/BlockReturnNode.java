package com.mraof.minestuck.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReturnNode extends BlockGate
{
	
	public BlockReturnNode(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValid(BlockPos pos, World world)
	{
		for(int x = -1; x <= 0; x++)
			for(int z = -1; z <= 0; z++)
				if(x != 0 || z != 0)
				{
					IBlockState block = world.getBlockState(pos.add(x, 0, z));
					if(block.getBlock() != this || block.get(MAIN))
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
					if(block.getBlock() == this && block.get(MAIN))
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
					world.setBlockState(pos.add(x, 0, z), Blocks.AIR.getDefaultState());
	}
}