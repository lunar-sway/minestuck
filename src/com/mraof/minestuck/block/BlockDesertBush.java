package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockDesertBush extends BlockDesertFlora {
	public BlockDesertBush(String name) {
		super(name);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return MinestuckItems.desertFruit;
    }
	
	@Override
	public int quantityDropped(Random random)
    {
        return 3 + random.nextInt(3);
    }
}
