package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class GlowingMushroomBlock extends BushBlock
{
	public GlowingMushroomBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return state.isOpaqueCube(worldIn, pos);
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		super.tick(state, worldIn, pos, random);
		if(canSpread(worldIn, pos, state) && random.nextInt(25) == 0)
		{
			int count = 0;
			Iterable<BlockPos> blocks = BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4));
			
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
					worldIn.setBlockState(spreadPos, this.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
					return;
				}
			}
		}
	}
	
	@Override
	public PlantType getPlantType(IBlockReader world, BlockPos pos)
	{
		return PlantType.CAVE;
	}
	
	public boolean canSpread(World world, BlockPos pos, BlockState state)
	{
		BlockState soil = world.getBlockState(pos.down());
		return soil.getBlock().equals(MSBlocks.BLUE_DIRT);
	}
}