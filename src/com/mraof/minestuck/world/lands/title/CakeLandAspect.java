package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.lands.decorator.SingleBlockDecorator;
import com.mraof.minestuck.world.lands.decorator.structure.CakePedestalDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class CakeLandAspect extends TitleLandAspect
{
	public CakeLandAspect()
	{
		super(EnumAspect.HEART);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"cake", "dessert"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.ORANGE_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.MAGENTA_CARPET.getDefaultState());
	}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.decorators.add(new CakeDecorator(chunkProvider.temperature));
		chunkProvider.decorators.add(new CakePedestalDecorator(MSBiomes.mediumNormal, MSBiomes.mediumRough));
		//chunkProvider.sortDecorators();
	}
	
	private static class CakeDecorator extends SingleBlockDecorator
	{
		public float redCakeChance;
		public CakeDecorator(float temperature)
		{
			redCakeChance = MathHelper.clamp(temperature/2, 0, 1);
		}
		
		@Override
		public BlockState pickBlock(Random random)
		{
			int bites = Math.max(0, (int) (random.nextDouble()*10) - 6);
			float f = random.nextFloat();
			if(f < 0.1F)
			{
				if(random.nextFloat() < redCakeChance)
					return (f < 0.05F ? MSBlocks.RED_CAKE : MSBlocks.HOT_CAKE).getDefaultState().with(CakeBlock.BITES, bites);
				else return (f < 0.05F ? MSBlocks.BLUE_CAKE : MSBlocks.COLD_CAKE).getDefaultState().with(CakeBlock.BITES, bites);
			}
			else if(f < 0.4F)
				return MSBlocks.APPLE_CAKE.getDefaultState().with(CakeBlock.BITES, bites);
			else if(random.nextFloat() < 0.01)
				return MSBlocks.REVERSE_CAKE.getDefaultState().with(CakeBlock.BITES, bites);
			else
				return Blocks.CAKE.getDefaultState().with(CakeBlock.BITES, bites);
		}
		
		@Override
		public int getCount(Random random)
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
		{//TODO
			return true;//Blocks.CAKE.canPlaceBlockAt(world, pos) && !world.getBlockState(pos).getMaterial().isLiquid() && world.getBlockState(pos).getBlock().isReplaceable(world, pos);
		}
	}
}