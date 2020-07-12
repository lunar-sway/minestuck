package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MachineMultiblock;

public class MultiMachineBlock extends MachineBlock
{
	protected final MachineMultiblock machine;
	
	public MultiMachineBlock(MachineMultiblock machine, Properties properties)
	{
		super(properties);
		this.machine = machine;
	}
	//TODO Is this class needed? Is there anything that subclasses do that is better off done here?
}