package com.mraof.minestuck.world.gen.feature;

import java.util.Random;

import com.mraof.minestuck.block.BlockMinestuckLeaves1;
import com.mraof.minestuck.block.BlockMinestuckLog1;
import com.mraof.minestuck.block.MinestuckBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenRainbowTree extends WorldGenAbstractTree
{
	private static final IBlockState LOG = MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.RAINBOW);
	private static final IBlockState LEAF = MinestuckBlocks.leaves1.getDefaultState().withProperty(BlockMinestuckLeaves1.VARIANT, BlockMinestuckLeaves1.BlockType.RAINBOW);
	
	public WorldGenRainbowTree(boolean notify)
	{
		super(notify);
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		int height = rand.nextInt(3) + 5;

		boolean flag = true;

		if (position.getY() >= 1 && position.getY() + height + 1 <= 256)
		{
			for (int j = position.getY(); j <= position.getY() + 1 + height; ++j)
			{
				int k = 1;

				if (j == position.getY())
				{
					k = 0;
				}

				if (j >= position.getY() + 1 + height - 2)
				{
					k = 2;
				}

				BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

				for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
				{
					for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
					{
						if (j >= 0 && j < worldIn.getHeight())
						{
							if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(l, j, i1)))
							{
								flag = false;
							}
						}
						else
						{
							flag = false;
						}
					}
				}
			}

			if (!flag)
			{
				return false;
			}
			else
			{
				boolean hasSoil = MinestuckBlocks.rainbowSapling.canPlaceBlockAt(worldIn, position);
				IBlockState state = worldIn.getBlockState(position.down());

				if (hasSoil && position.getY() < worldIn.getHeight() - height - 1)
				{
					state.getBlock().onPlantGrow(state, worldIn, position.down(), position);

					for (int i2 = position.getY() - 3 + height; i2 <= position.getY() + height; ++i2)
					{
						int k2 = i2 - (position.getY() + height);
						int l2 = 1 - k2 / 2;

						for (int i3 = position.getX() - l2; i3 <= position.getX() + l2; ++i3)
						{
							int j1 = i3 - position.getX();

							for (int k1 = position.getZ() - l2; k1 <= position.getZ() + l2; ++k1)
							{
								int l1 = k1 - position.getZ();

								if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0)
								{
									BlockPos blockpos = new BlockPos(i3, i2, k1);
									IBlockState state2 = worldIn.getBlockState(blockpos);

									if (state2.getBlock().isAir(state2, worldIn, blockpos) || state2.getBlock().isAir(state2, worldIn, blockpos))
									{
										this.setBlockAndNotifyAdequately(worldIn, blockpos, LEAF);
									}
								}
							}
						}
					}

					for (int j2 = 0; j2 < height; ++j2)
					{
						BlockPos upN = position.up(j2);
						IBlockState state2 = worldIn.getBlockState(upN);

						if (state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(state2, worldIn, upN))
						{
							this.setBlockAndNotifyAdequately(worldIn, position.up(j2), LOG);
						}
					}

					return true;
				}
				else
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
	}
}
