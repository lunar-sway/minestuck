package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class EndGrassBlock extends Block
{
	protected EndGrassBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if(!worldIn.isRemote)
		{
			if(!worldIn.isAreaLoaded(pos, 3))
				return;
			if(worldIn.getLight(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getOpacity(worldIn, pos.up()) > 2)
			{
				worldIn.setBlockState(pos, Blocks.END_STONE.getDefaultState());
			}
			else
			{
				if(worldIn.getLight(pos.up()) >= 9)
				{
					for (int i = 0; i < 4; ++i)
					{
						BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(5) - 3);	//End grass grows faster north-ways because magnets
						BlockState iblockstate = worldIn.getBlockState(blockpos);
						BlockState iblockstate1 = worldIn.getBlockState(blockpos.up());

						if (iblockstate.getBlock() == Blocks.END_STONE && worldIn.getLight(blockpos.up()) >= 4 && iblockstate1.getOpacity(worldIn, blockpos.up()) <= 2)
						{
							worldIn.setBlockState(blockpos, this.getDefaultState());
						}
					}
				}
			}
		}
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		super.animateTick(stateIn, worldIn, pos, rand);

		if (rand.nextInt(10) == 0)
			worldIn.addParticle(ParticleTypes.PORTAL, (float)pos.getX() + rand.nextFloat(), (float)pos.getY() + 1.1F, (float)pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
	}
	
	@Override
	public void onPlantGrow(BlockState state, IWorld world, BlockPos pos, BlockPos source)
	{
		world.setBlockState(pos, Blocks.END_STONE.getDefaultState(), 2);
	}
}