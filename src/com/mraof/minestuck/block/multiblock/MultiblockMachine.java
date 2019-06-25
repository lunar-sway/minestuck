package com.mraof.minestuck.block.multiblock;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class MultiblockMachine implements IItemProvider    //An abstraction for large machines that might be expanded upon in the future
{
	
	public abstract Block getMainBlock();
	
	public abstract void registerBlocks(IForgeRegistry<Block> registry);
	
	
	@Override
	public Item asItem()
	{
		return getMainBlock().asItem();
	}
}