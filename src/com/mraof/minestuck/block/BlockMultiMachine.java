package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MultiblockMachine;
import net.minecraft.item.Item;

public class BlockMultiMachine extends BlockMachine
{
	protected final MultiblockMachine machine;
	
	public BlockMultiMachine(MultiblockMachine machine, Properties properties)
	{
		super(properties);
		this.machine = machine;
	}
	
	@Override
	public Item asItem()
	{
		return this == machine.getMainBlock() ? super.asItem() : machine.asItem();
	}
}