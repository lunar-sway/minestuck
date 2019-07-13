package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MachineMultiblock;
import net.minecraft.item.Item;

public class MultiMachineBlock extends MachineBlock
{
	protected final MachineMultiblock machine;
	
	public MultiMachineBlock(MachineMultiblock machine, Properties properties)
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