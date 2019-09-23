package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mraof.minestuck.block.GlowingMushroomBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class SurfaceMushroomGenerator extends BiomeSpecificDecorator
{
	private int tries;
	private int count;
	private Block block = MSBlocks.GLOWING_MUSHROOM;
	private boolean lightOverride = true;
	
	public SurfaceMushroomGenerator(int tries, int count, Biome... biomes)
	{
		super(biomes);
		this.tries = tries;
		this.count = count;
	}
	
	public SurfaceMushroomGenerator(Block block, boolean lightOverride, int tries, int count, Biome... biomes)
	{
		this(tries, count, biomes);
		this.block = block;
		this.lightOverride = lightOverride;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		for (int i = 0; i < tries; ++i)
		{
			BlockPos pos1 = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
			if (world.isAirBlock(pos1) && canMushroomStay(world, pos1, block.getDefaultState()))
				world.setBlockState(pos1, block.getDefaultState(), 2);
		}
		
		return null;
	}
	
	public boolean canMushroomStay(World worldIn, BlockPos pos, BlockState state)
	{
		boolean out = false;
		if (pos.getY() >= 0 && pos.getY() < 256)
		{
			BlockState soil = worldIn.getBlockState(pos.down());
			if(state.getBlock() instanceof GlowingMushroomBlock)
			{
				out = soil.getBlock().equals(MSBlocks.BLUE_DIRT);
			} else if(soil.getBlock() == Blocks.MYCELIUM)
			{
				out = true;
			} else
			{
				/*if(soil.getBlock() == Blocks.DIRT && soil.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL)
				{
					out = true;
				} else
				{
					out = soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, Blocks.BROWN_MUSHROOM);
					if(lightOverride)
					{
						out = out && worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) < 13;
					} else
					{
						out = out && worldIn.getLight(pos) < 13;
					}
				}*/
			}
		}
		return out;
	}
	
	@Override
	public int getCount(Random random)
	{
		return count;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
}