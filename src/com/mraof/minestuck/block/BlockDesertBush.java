package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.item.Item;

public class BlockDesertBush extends BlockDesertFlora {
	public BlockDesertBush(String name) {
		super(name);
	}

	public Item getItemDropped()
    {
        return MinestuckItems.desertFruit;
    }

	public int quantityDropped(Random random)
    {
        return 3 + random.nextInt(3);
    }
}
