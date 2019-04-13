package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.SingleBlockDecorator;
import com.mraof.minestuck.world.lands.decorator.structure.CakePedestalDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

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
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.CARPET.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.MAGENTA));
		
		chunkProvider.decorators.add(new CakeDecorator(chunkProvider.temperature));
		chunkProvider.decorators.add(new CakePedestalDecorator(BiomeMinestuck.mediumNormal, BiomeMinestuck.mediumRough));
		chunkProvider.sortDecorators();
	}
	
	private static class CakeDecorator extends SingleBlockDecorator
	{
		public float redCakeChance;
		public CakeDecorator(float temperature)
		{
			redCakeChance = MathHelper.clamp(temperature/2, 0, 1);
		}
		
		@Override
		public IBlockState pickBlock(Random random)
		{
			int bites = Math.max(0, (int) (random.nextDouble()*10) - 6);
			float f = random.nextFloat();
			if(f < 0.1F)
			{
				if(random.nextFloat() < redCakeChance)
					return (f < 0.05F ? MinestuckBlocks.redCake : MinestuckBlocks.hotCake).getDefaultState().withProperty(BlockCake.BITES, bites);
				else return (f < 0.05F ? MinestuckBlocks.blueCake : MinestuckBlocks.coldCake).getDefaultState().withProperty(BlockCake.BITES, bites);
			}
			else if(f < 0.4F)
				return MinestuckBlocks.appleCake.getDefaultState().withProperty(BlockCake.BITES, bites);
			else if(random.nextFloat() < 0.01)
				return MinestuckBlocks.reverseCake.getDefaultState().withProperty(BlockCake.BITES, bites);
			else
				return Blocks.CAKE.getDefaultState().withProperty(BlockCake.BITES, bites);
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
		{
			return Blocks.CAKE.canPlaceBlockAt(world, pos) && !world.getBlockState(pos).getMaterial().isLiquid() && world.getBlockState(pos).getBlock().isReplaceable(world, pos);
		}
	}
}