package com.mraof.minestuck.world.lands.title;

import java.util.Random;

import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.decorator.SingleBlockDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectCake extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "cake";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"cake", "dessert"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new CakeDecorator());
			chunkProvider.sortDecorators();
			
		}
		
	}
	
	private static class CakeDecorator extends SingleBlockDecorator
	{
		@Override
		public IBlockState pickBlock(Random random)
		{
			int bites = Math.max(0, (int) (random.nextDouble()*10) - 6);
			return Blocks.cake.getDefaultState().withProperty(BlockCake.BITES, bites);
		}
		@Override
		public int getBlocksForChunk(int chunkX, int chunkZ, Random random)
		{
			if(random.nextDouble() < 0.2)
			{
				int blocks = 0;
				for(int i = 0; i < 10; i++)
					if(random.nextBoolean())
						blocks++;
				return blocks;
			}
			return 0;
		}
		@Override
		public boolean canPlace(BlockPos pos, World world)
		{
			return Blocks.cake.canPlaceBlockAt(world, pos) && !world.getBlockState(pos).getMaterial().isLiquid() && world.getBlockState(pos).getBlock().isReplaceable(world, pos);
		}
	}
}