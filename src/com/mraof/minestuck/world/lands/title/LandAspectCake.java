package com.mraof.minestuck.world.lands.title;

import java.util.Random;

import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.decorator.SingleBlockDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectCake extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Cake";
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
			
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.cake, 1, 0), 1, 1, 6));	//Yes, of course
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.cookie, 1, 0), 2, 8, 5));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.sugar, 1, 0), 1, 5, 4));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.milk_bucket, 1, 0), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(MinestuckItems.silverSpoon, 1, 0), 1, 1, 4));
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
			return Blocks.cake.canPlaceBlockAt(world, pos) && !world.getBlockState(pos).getBlock().getMaterial().isLiquid() && world.getBlockState(pos).getBlock().isReplaceable(world, pos);
		}
	}
}