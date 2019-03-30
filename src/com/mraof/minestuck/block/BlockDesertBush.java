package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDesertBush extends BlockDesertFlora
{
	public BlockDesertBush(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
	{
        return MinestuckItems.desertFruit;
    }
	
	@Override
	public int quantityDropped(IBlockState state, Random random)
	{
        return 3 + random.nextInt(3);
    }
}