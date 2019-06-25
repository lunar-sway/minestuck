package com.mraof.minestuck.block;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockGlowingMushroom extends BlockBush
{
	public BlockGlowingMushroom(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValidGround(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return state.isOpaqueCube(worldIn, pos);
	}
	
	@Override
	public void tick(IBlockState state, World worldIn, BlockPos pos, Random random)
	{
		super.tick(state, worldIn, pos, random);
		if(canSpread(worldIn, pos, state) && random.nextInt(25) == 0)
		{
			int count = 0;
			Iterable<BlockPos.MutableBlockPos> blocks = BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4));
			
			for(BlockPos checkPos : blocks)
				if(worldIn.getBlockState(checkPos).getBlock() == this)
				{
					count++;
					if (count >= 5)
						return;
				}
			
			for (int i = 0; i < 5; ++i)
			{
				BlockPos spreadPos = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
				if (worldIn.isAirBlock(spreadPos) && this.canSpread(worldIn, spreadPos, this.getDefaultState()))
				{
					worldIn.setBlockState(spreadPos, this.getDefaultState(), 2);
					return;
				}
			}
		}
	}
	
	public boolean canSpread(World world, BlockPos pos, IBlockState state)
	{
		IBlockState soil = world.getBlockState(pos.down());
		return soil.getBlock().equals(MinestuckBlocks.BLUE_DIRT);
	}
}