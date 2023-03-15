package com.mraof.minestuck.block.machine;

public class MultiMachineBlock<T extends MachineMultiblock> extends MachineBlock
{
	protected final T machine;
	
	public MultiMachineBlock(T machine, Properties properties)
	{
		super(properties);
		this.machine = machine;
	}
	
	
	//TODO Is this class needed? Is there anything that subclasses do that is better off done here?
}