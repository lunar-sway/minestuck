package com.mraof.minestuck.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEndGrass extends Block
{
	protected BlockEndGrass(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void randomTick(IBlockState state, World worldIn, BlockPos pos, Random random)
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
						IBlockState iblockstate = worldIn.getBlockState(blockpos);
						IBlockState iblockstate1 = worldIn.getBlockState(blockpos.up());

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
	@OnlyIn(Dist.CLIENT)
	public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		super.animateTick(stateIn, worldIn, pos, rand);

		if (rand.nextInt(10) == 0)
			worldIn.addParticle(Particles.PORTAL, (double)((float)pos.getX() + rand.nextFloat()), (double)((float)pos.getY() + 1.1F), (double)((float)pos.getZ() + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
	}
	
	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
	{
		return Blocks.END_STONE;
	}
}